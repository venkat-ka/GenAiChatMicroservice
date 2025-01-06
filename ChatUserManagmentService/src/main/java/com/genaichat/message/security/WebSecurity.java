package com.genaichat.message.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

import com.genaichat.message.data.UsersRepository;
import com.genaichat.message.service.UsersService;



@Configuration
@EnableWebSecurity
public class WebSecurity {
	private Environment environment;
	private UsersService usersService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private UsersRepository userRepo;
	private SimpMessagingTemplate simpleMsgTemp;
	
	
//	public WebSecurity(Environment environment, UsersService usersService,
//			BCryptPasswordEncoder bCryptPasswordEncoder) {
//		this.environment = environment;
//		this.usersService = usersService;
//		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//	}

	  public WebSecurity(Environment environment, UsersService usersService, BCryptPasswordEncoder bCryptPasswordEncoder,
			UsersRepository userRepo, SimpMessagingTemplate simpleMsgTemp) {
		super();
		this.environment = environment;
		this.usersService = usersService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userRepo = userRepo;
		this.simpleMsgTemp = simpleMsgTemp;
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
	    	
	    	/* public AuthenticationFilter(UsersService usersService, Environment environment, UsersRepository userRepo,
			SimpMessagingTemplate template) */

	    	// Create AuthenticationFilter
	    	AuthenticationFilter authenticationFilter = 
	    			new AuthenticationFilter(usersService,authenticationManager, environment, userRepo, simpleMsgTemp);
	    	authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
	    	/*(UsersService usersService, AuthenticationManager authenticationManager, Environment environment, UsersRepository userRepo,
			SimpMessagingTemplate template)
*/
	        http.csrf((csrf) -> csrf.disable());
	  //'"+environment.getProperty("gateway.ip")+"'

	    	http.authorizeHttpRequests((auth)->auth.requestMatchers(new AntPathRequestMatcher("/ws-chat/**"))
	    			.access(new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')")));
	    	
	        http.authorizeHttpRequests((authz) -> authz
	        .requestMatchers(new AntPathRequestMatcher("/users/**"))
	        
	        .access(
			new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')"))
	        //.permitAll()
	        //.requestMatchers(new AntPathRequestMatcher("/users/status/check")).permitAll())
	        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
	        .requestMatchers(HttpMethod.POST, "/users").permitAll())
	        
	        
	        .addFilter(new AuthorizationFilter(authenticationManager, environment))
	        .addFilter(authenticationFilter)
	        .authenticationManager(authenticationManager)
	        .sessionManagement((session) -> session
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	 
	        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));
	        return http.build();

	    }
}
