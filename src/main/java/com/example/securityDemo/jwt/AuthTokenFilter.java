package com.example.securityDemo.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	
	private static final Logger logger= LoggerFactory.getLogger(AuthTokenFilter.class);
	
	
	
	
	//This method intercepts each request to validate it.
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException
	{
		
		logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
		try {
			String jwt= parseJwt(request);
			if(jwt!=null  && jwtUtils.validateJwtToken(jwt))
			{
				String username = jwtUtils.getUserNameFromJwtToken(jwt);
				UserDetails userDetails= userDetailsService.loadUserByUsername(username);
				
				UsernamePasswordAuthenticationToken authentication=
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
			    logger.debug("Roles from JWT: {}", userDetails.getAuthorities());
			  
			    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			    
			    SecurityContextHolder.getContext().setAuthentication(authentication);
			
			}
		}
		
		catch(Exception e)
		{
			logger.error("Cannot set user authentication: {}" ,e);
		}
		
		  filterChain.doFilter(request, response);  //this means keep filtering the request and response in chain.
		
	}
	
	private String  parseJwt(HttpServletRequest request)
	{
		String jwt= jwtUtils.getJwtFromHeader(request);
		logger.debug("AuthenticationFilter.java: {}", jwt);
		return jwt;
	}
	
	
	
	

}
