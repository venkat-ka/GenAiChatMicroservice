package com.genaichat.message.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.genaichat.message.service.UsersService;



@Configuration
@EnableWebSecurity
public class WebSecurity {
	private Environment environment;
	private UsersService usersService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurity(Environment environment, UsersService usersService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.environment = environment;
		this.usersService = usersService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	  @Bean
	  protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
	    	System.out.println(environment.getProperty("gateway.ip"));
	    	// Configure AuthenticationManagerBuilder
	    	AuthenticationManagerBuilder authenticationManagerBuilder = 
	    			http.getSharedObject(AuthenticationManagerBuilder.class);
	    	
	    	authenticationManagerBuilder.userDetailsService(usersService)
	    	.passwordEncoder(bCryptPasswordEncoder);
	    	
	    	AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
	    	
	    	// Create AuthenticationFilter
	    	AuthenticationFilter authenticationFilter = 
	    			new AuthenticationFilter(usersService, environment, authenticationManager);
	    	authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
	    	
	        http.csrf((csrf) -> csrf.disable());
	  //'"+environment.getProperty("gateway.ip")+"'
	        http.authorizeHttpRequests((authz) -> authz
	        .requestMatchers(new AntPathRequestMatcher("/users/**"))
	        .access(
			new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')"))
	        //.permitAll()
	        //.requestMatchers(new AntPathRequestMatcher("/users/status/check")).permitAll())
	        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll())
	        
	        .addFilter(new AuthorizationFilter(authenticationManager, environment))
	        .addFilter(authenticationFilter)
	        .authenticationManager(authenticationManager)
	        .sessionManagement((session) -> session
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	 
	        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));
	        return http.build();

	    }
}
