package com.example.securityDemo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.example.securityDemo.jwt.AuthEntryPointJwt;
import com.example.securityDemo.jwt.AuthTokenFilter;


@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
	
	@Autowired
	DataSource datasource;
	
	@Autowired 
	private AuthEntryPointJwt unauthorizedHandler;
	
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter()
	{
		return new AuthTokenFilter();
	}
	
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception
	 {

		
		
		http.authorizeHttpRequests(authorizeRequests-> 
		                           authorizeRequests.requestMatchers("/h2-console/**").permitAll()
		                           .requestMatchers("/signin").permitAll()
		                           .anyRequest().authenticated());
		
		http.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.exceptionHandling(exception-> exception.authenticationEntryPoint(unauthorizedHandler));
		http.headers(headers->headers.frameOptions(frameOptions-> frameOptions.sameOrigin()));
				
				            
		http.csrf(csrf->csrf.disable());
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
		 
	 }

//	@Bean
//	 public UserDetailsService userDetailsService()
//	 {
//		 UserDetails admin=User.withUsername("admin")
//				              .password(passwordEncoder().encode("adminpass"))
//				              .roles("ADMIN")
//				              .build();
//		 
//		 
//		 UserDetails user= User.withUsername("user")
//				               .password(passwordEncoder().encode("userpass")) 
//				               .roles("USER")
//				               .build();
//		 
//		 JdbcUserDetailsManager userDetailsManager= new JdbcUserDetailsManager(datasource);
//		 userDetailsManager.createUser(user);
//		 userDetailsManager.createUser(admin);
//		 
//		 return userDetailsManager;
//		 
//		// return new InMemoryUserDetailsManager(admin, user);
//		 
//	 }
//	
	
	@Bean
	public UserDetailsService userDetailsService(DataSource datasource)
	{
		return new JdbcUserDetailsManager(datasource);
				
	}
	
	
	@Bean
	public CommandLineRunner initData(UserDetailsService userDetailsService)
	{
		return args-> {
			JdbcUserDetailsManager manager= (JdbcUserDetailsManager) userDetailsService;
			UserDetails admin=User.withUsername("admin")
		              .password(passwordEncoder().encode("adminpass"))
		              .roles("ADMIN")
		              .build();


			UserDetails user= User.withUsername("user")
		               .password(passwordEncoder().encode("userpass")) 
		               .roles("USER")
		               .build();
			JdbcUserDetailsManager userDetailsManager= new JdbcUserDetailsManager(datasource);
			 userDetailsManager.createUser(user);
			 userDetailsManager.createUser(admin);
			 
			 
			
		};
	}
	
	
	
	@Bean
	 public PasswordEncoder passwordEncoder()
	 {
		 return new BCryptPasswordEncoder();
	 }


	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception
	{
		return builder.getAuthenticationManager();
	}

}
