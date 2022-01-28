package com.topideal.filter;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.tools.DPSign;
import com.topideal.mongo.dao.ApiExternalConfigMongoDao;
import com.topideal.mongo.entity.ApiExternalConfigMongo;
import com.topideal.system.SystemLog;
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
 * 经分销验签过滤器
 * 局部过滤器
 * */
@Component
public class SignDerpGatewayFilterFactory extends AbstractGatewayFilterFactory<SignDerpGatewayFilterFactory.Config>{

	private  static  final Logger logger = LoggerFactory.getLogger(SignDerpGatewayFilterFactory.class);

	@Autowired
	private ApiExternalConfigMongoDao apiExternalConfigMongoDao;
	@Autowired
	private SystemLog systemLog;

	/**
	 * 默认构造方法 这里需要将自定义的config传过去，否则会报告ClassCastException
	 */
	public SignDerpGatewayFilterFactory() {
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
		JSONObject jsonData=new JSONObject();
		try {
			jsonData = jsonData.fromObject(body);
			String appKey = (String)jsonData.get("appKey");//商家appkey
			if(StringUtils.isBlank(appKey)) {
				return error(exchange, body,"appKey为空");
			}
			//查询商家秘钥
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("appKey", appKey);
			paramMap.put("status", DERP_SYS.APIEXTERNALCONFIG_STATUS_1);//状态状态(1使用中,0已禁用)
			ApiExternalConfigMongo configModel = apiExternalConfigMongoDao.findOne(paramMap);
			if(configModel==null){
				return error(exchange, body,"秘钥不存在");
			}

			String sign = (String)jsonData.get("sign");
			signTag = DPSign.signVerify(sign, jsonData, configModel.getAppSecret(),"sign");
		}catch (Exception e) {
			signTag = false;
			e.printStackTrace();
		}

		if(signTag!=true) {
			return error(exchange,body,"验签失败");
		}
		return Mono.just(body);
	}
	/**
	 * 网关拦截
	 * 1、中止路由
	 * 2、返回错误信息
	 * @param responseCodeEnum
	 */
	public Mono<Void> error(ServerWebExchange exchange,String body,String msg){
		ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
		JSONObject jsonData=new JSONObject();
		jsonData.put("mCode", "9999");//0000-成功 9999-失败
		jsonData.put("message", msg);

		//发送日志
		systemLog.sendLog(request, body,msg,jsonData.toString());

		byte[] bits = jsonData.toString().getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bits);
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		exchange.getResponse().getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
		return exchange.getResponse().writeWith(Mono.just(buffer));
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

}
