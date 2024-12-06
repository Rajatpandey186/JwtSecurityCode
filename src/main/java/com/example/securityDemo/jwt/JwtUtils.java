package com.example.securityDemo.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
	
	private static final Logger logger= LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${spring.app.jwtSecret}")
	private String jwtSecret;
	
	
	@Value("${spring.app.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	
	//Method to retrieve Jwt token from the HTTP header.
	public String getJwtFromHeader(HttpServletRequest request)
	{
		String bearerToken= request.getHeader("Authorization");
		logger.debug("Authorization Header : {}" , bearerToken);
		
		if(bearerToken!=null && bearerToken.startsWith("Bearer "))
		{
			return bearerToken.substring(7);
		}
		return null;
	}
	
	
	//Method to retrieve token from Username.
	public String generateTokenFromUsername(UserDetails userDetails)
	{
		String username= userDetails.getUsername();
		return Jwts.builder()
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(key())
                .compact();		
	}
	
	//This is the key used to signing in the Jwts.
	private Key key()
	{
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	
	//Getting the Username from Token.    //subject means username.
	public String getUserNameFromJwtToken(String token)
	{
		return Jwts.parser()
				.verifyWith((SecretKey) key()).build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}
	
	
	//public boolean validate Jwt Token
	public boolean validateJwtToken(String authToken)
	{
		try {
			  System.out.println("validate");
			  Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
			  return true;
		}
		catch(MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
			
		}
		catch(ExpiredJwtException e)
		{
			logger.error("JWT token is expired: {}", e.getMessage());
		}
		catch(UnsupportedJwtException e)
		{ 
			logger.error("JWT token is unsuppoorted: {}", e.getMessage());
		}
		catch(IllegalArgumentException e)
		{
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}
		
		return false;
	}

}
