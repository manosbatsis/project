package com.topideal.filter;

/**已废弃*/
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RequestBody extends RequestUtil{
	
    private  static  final Logger logger = LoggerFactory.getLogger(RequestBody.class);
    
	/**1.获取reqestBody
	 * 2.验签
	 * 3.生成新的reqestBody
	 * */
	public Mono<String> newReqestBody(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders()); 
        /**读取body生成新的body*/
        Mono<String> modifiedBody = request.bodyToMono(String.class)
                .flatMap(body -> {
                	return newBodyAndverifySign(exchange,body);
                });
        return modifiedBody;
	}
	
	public Mono newBodyAndverifySign(ServerWebExchange exchange,String body) {
		  logger.info("body参数:"+body);
		  /**请求方式*/
          MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
	      if(MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
	    	    /**请求参数转换为map*/
	            Map<String, Object> bodyMap = decodeBody(body);
	            /**验证令牌/做要处理的事情*/
//	            Map<String, Object> newBodyMap = new HashMap<>();
//	            return Mono.just(body);
	    	  
	    	  	return verifySignForJson(exchange, body);
	      }else{
	    	    /**验签*/
	     	    return verifySignForJson(exchange, body);
	      }
  	
    }
	public  Mono resetRquestBody(ServerWebExchange exchange, GatewayFilterChain chain,Mono<String> modifiedBody) {
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
	
	public Map<String, Object> decodeBody(String body) {
        return Arrays.stream(body.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
    }

    public String encodeBody(Map<String, Object> map) {
        return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
    }

    
}
