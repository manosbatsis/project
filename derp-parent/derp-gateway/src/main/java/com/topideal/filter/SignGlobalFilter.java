package com.topideal.filter;
/**已废弃*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 签名校验过滤器
 * 全局过滤器无需在配置文件或配置类做配置
 */
//@Component
//public class SignGlobalFilter implements GlobalFilter, Ordered {
//
//	@Autowired
//	private RequestBody requestBody;
//
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//         //读取body 验签 生成新的body
//         Mono<String> modifiedBody = requestBody.newReqestBody(exchange, chain);
//         //重写body
//         return requestBody.resetRquestBody(exchange, chain, modifiedBody);
//     }
//
//     @Override
//     public int getOrder() {
//         return -99;
//     }
//
//
//}
