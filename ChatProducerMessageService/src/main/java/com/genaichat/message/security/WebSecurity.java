package com.genaichat.message.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableWebSecurity
public class WebSecurity {

	private Environment environment;
	
	
    
    public WebSecurity(Environment environment) {
		super();
		this.environment = environment;
	}



	@Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
    	System.out.println(environment.getProperty("gateway.ip"));
    	// Configure AuthenticationManagerBuilder
    	http.csrf((csrf) -> csrf.disable());
    	http.authorizeHttpRequests((auth)->auth.requestMatchers(new AntPathRequestMatcher("/chat/**"))
    			.access(new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')")));
    	
    	
//    	
//    	http.authorizeHttpRequests((auth)->auth.requestMatchers(HttpMethod.GET, "/actuator/health")
//    			.access(new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')")
//    					));
//    	
//    	http.authorizeHttpRequests((auth)->auth.requestMatchers(HttpMethod.GET, "/actuator/circuitbreakerevents")
//    			.access(new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')")
//    					));
    	
    	//http.authorizeHttpRequests(vl -> vl.requestMatchers(new AntPathRequestMatcher("/apis/**")).permitAll());
		http.sessionManagement((session) -> session
		        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
//		
//        http.csrf((csrf) -> csrf.disable());
//        //http.formLogin(k->k.disable());
//  //'"+environment.getProperty("gateway.ip")+"'
//        http.authorizeHttpRequests((authz) -> authz
//        .requestMatchers(new AntPathRequestMatcher("/api/**"))
//        .access(
//		new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')"))
//        
//        //.requestMatchers(new AntPathRequestMatcher("/users/status/check")).permitAll())
//        )
//        .sessionManagement((session) -> session
//        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));
        return http.build();

    }
}
