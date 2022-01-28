package com.topideal.filter;

import com.topideal.mongo.dao.ApiSecretConfigMongoDao;
import com.topideal.mongo.entity.ApiSecretConfigMongo;
import com.topideal.system.SystemLog;
import com.topideal.tools.SignUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 跨境宝验签过滤器
 * 局部过滤器
 * */
@Component
public class SignKJBGatewayFilterFactory extends AbstractGatewayFilterFactory<SignKJBGatewayFilterFactory.Config>{

	private  static  final Logger logger = LoggerFactory.getLogger(SignKJBGatewayFilterFactory.class);

	@Autowired
	private ApiSecretConfigMongoDao apiSecretConfigMongoDao;
	@Autowired
	private SystemLog systemLog;

	/**
	 * 默认构造方法 这里需要将自定义的config传过去，否则会报告ClassCastException
	 */
	public SignKJBGatewayFilterFactory() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
			Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
					.flatMap(body -> {
						/**生成新的body及验签*/
						return newBodyAndverifySign(exchange,body);
					});
			/**重置body*/
			return resetRquestBody(exchange, chain, modifiedBody);
		};
	}

	/**
	 * 内置类
	 * */
	public static class Config {
		//可以配置属性
	}
	/*******************************************自定义方法****************************************/
	/**
	 * 1.生成新的requestBody
	 * 2.验签
	 * */
	public Mono newBodyAndverifySign(ServerWebExchange exchange, String body) {
		boolean signTag = true;
		logger.info("body参数:"+body);
		JSONObject jsonData=new JSONObject();
		try {
			jsonData = JSONObject.fromObject(body);
			String appkey = (String) jsonData.get("app_key");
			if(StringUtils.isBlank(appkey)){
				return errorKJB(exchange,body,"app_key为空");
			}
			//获取秘钥
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("appKey", appkey);
			ApiSecretConfigMongo apiSecretConfigMongo = apiSecretConfigMongoDao.findOne(paramMap);
			if(apiSecretConfigMongo==null) {
				return errorKJB(exchange, body,"秘钥不存在");
			}
			String appSecret = apiSecretConfigMongo.getAppSecret();
			signTag = SignUtil.verifyForOuter(body, appSecret);
		}catch (Exception e) {
			signTag = false;
			e.printStackTrace();
		}
		if(signTag!=true) {
			return errorKJB(exchange,body,"验签失败");
		}
		return Mono.just(body);
	}
	/**
	 * 重置请求体
	 * */
	public  Mono resetRquestBody(ServerWebExchange exchange, GatewayFilterChain chain, Mono<String> modifiedBody) {
		/**写入新的body 因body在只能被读取一次所以要重新写入*/
		BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
		HttpHeaders headers = new HttpHeaders();
		headers.putAll(exchange.getRequest().getHeaders());
		/**重新写content length*/
		headers.remove(HttpHeaders.CONTENT_LENGTH);

		CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
		return bodyInserter.insert(outputMessage,  new BodyInserterContext())
				.then(Mono.defer(() -> {
					ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
							exchange.getRequest()) {
						@Override
						public HttpHeaders getHeaders() {
							long contentLength = headers.getContentLength();
							HttpHeaders httpHeaders = new HttpHeaders();
							httpHeaders.putAll(super.getHeaders());
							if (contentLength > 0) {
								httpHeaders.setContentLength(contentLength);
							} else {
								httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
							}
							return httpHeaders;
						}

						@Override
						public Flux<DataBuffer> getBody() {
							return outputMessage.getBody();
						}
					};
					return chain.filter(exchange.mutate().request(decorator).build());
				}));
	}
	/**
	 * 网关拦截
	 * 1、中止路由
	 * 2、返回错误信息
	 */
	public Mono<Void> errorKJB(ServerWebExchange exchange,String body,String msg){
		ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
		JSONObject jsonData=new JSONObject();
		jsonData.put("status", "2");//1-提交成功 2-提交失败
		jsonData.put("notes", msg);
		//发送日志
		systemLog.sendLog(request, body,msg,jsonData.toString());

		byte[] bits = jsonData.toString().getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bits);
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		//指定编码，否则在浏览器中会中文乱码
		exchange.getResponse().getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
		return exchange.getResponse().writeWith(Mono.just(buffer));
	}

}
