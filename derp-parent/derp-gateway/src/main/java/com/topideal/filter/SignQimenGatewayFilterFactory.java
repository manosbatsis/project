package com.topideal.filter;

import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.XmlConverUtil;
import com.topideal.system.SystemLog;
import com.topideal.tools.QimenSignUtils;
import net.sf.json.JSONObject;
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
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 奇门验签过滤器
 * 局部过滤器
 * */
@Component
public class SignQimenGatewayFilterFactory extends AbstractGatewayFilterFactory<SignQimenGatewayFilterFactory.Config>{

	private  static  final Logger logger = LoggerFactory.getLogger(SignQimenGatewayFilterFactory.class);

	@Autowired
	private SystemLog systemLog;

	/**
	 * 默认构造方法 这里需要将自定义的config传过去，否则会报告ClassCastException
	 */
	public SignQimenGatewayFilterFactory() {
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
		try {
			ServerHttpRequest request = exchange.getRequest();
			String data = URLDecoder.decode(body, "UTF-8");
			String xml = null ;
			if (data.indexOf("<") >= 0) {
				xml = data.substring(data.indexOf("<"), data.lastIndexOf(">") + 1);
			}

			MultiValueMap<String, String> queryParams = request.getQueryParams();
			Map<String, String> map = queryParams.toSingleValueMap() ;
			String signTemp = map.remove("sign");
			String sign = QimenSignUtils.sign(map, xml, ApolloUtil.ywmsSecret);
			if(!sign.equals(signTemp)) {
				signTag = false ;
			}

		}catch (Exception e) {
			signTag = false;
			e.printStackTrace();
		}
		if(signTag!=true) {
			return errorQM(exchange,body,"验签失败");
		}
		return Mono.just(body);
	}
	/**
	 * 网关拦截
	 * 1、中止路由
	 * 2、返回错误信息
	 * @param responseCodeEnum
	 */
	public Mono<Void> errorQM(ServerWebExchange exchange,String body,String msg){
		ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
		JSONObject requestMessage = new JSONObject() ;
		requestMessage.put("xml", body) ;

		JSONObject responseMessage = new JSONObject() ;
		responseMessage.put("flag", "failure") ;
		responseMessage.put("code", 400) ;
		responseMessage.put("message", msg) ;

		JSONObject responseJson = new JSONObject() ;
		responseJson.put("response", responseMessage) ;
		String xml = XmlConverUtil.JsonToXml(responseJson.toString());
		//发送日志
		systemLog.sendLog(request, requestMessage.toString(), msg, responseMessage.toString());

		byte[] bits = xml.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bits);
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		//指定编码，否则在浏览器中会中文乱码
		exchange.getResponse().getHeaders().add("Content-Type", "text/xml;charset=UTF-8");
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
