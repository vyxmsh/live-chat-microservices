package com.slackclone.api_gateway.filter;

import com.slackclone.api_gateway.util.JwtUtil;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;

import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered{
    
    private final JwtUtil jwtUtil;

    //paths that bypass Jwt Validation
   private static final List<String> OPEN_ROUTES = List.of(
    "/api/auth/login",
    "/api/auth/register",
    "/ws"
); 

public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
}

@Override
public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
    String path = exchange.getRequest().getPath().value();

    //Allow open routes through with a token 
    if(OPEN_ROUTES.stream().anyMatch(path::startsWith))
    {
        return chain.filter(exchange);
    }

    //Web socket upgrade - token comes via Stomp connect, Handled by WebSocketAuthInterceptor
    //We still need an authorization header here for initial http upgrade request
    String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

    System.out.println("================================");
    System.out.println("PATH: " + path);
    System.out.println("AUTH HEADER: " + authHeader);

    if(authHeader==null || !authHeader.startsWith("Bearer "))
    {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    String token = authHeader.substring(7);

    System.out.println("TOKEN: " + token);
    System.out.println("VALID TOKEN: " + jwtUtil.validateToken(token));

    if(!jwtUtil.validateToken(token)){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    //Extract user info and forawrd as headers to downstream services
    String username = jwtUtil.extractUsername(token);
    String userId = jwtUtil.extractUserId(token);

    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                       .header("X-UserName", username)
                                       .header("X-User-Id", userId != null ? userId:" ")
                                       .build();
    
    return chain.filter(exchange.mutate().request(mutatedRequest).build());
}


@Override
public int getOrder(){
    return -1; //Run before all other filters
}
}