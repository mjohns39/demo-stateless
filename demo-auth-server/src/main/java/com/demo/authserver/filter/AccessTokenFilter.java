package com.demo.authserver.filter;

import java.io.IOException;
import java.security.KeyPair;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.authserver.authentication.TokenAuthentication;
import com.demo.authserver.jwt.JwtUtil;
import com.demo.authserver.user.service.DemoUserDetailsService;

@Component
public class AccessTokenFilter extends OncePerRequestFilter {

	private final KeyPair idTokenSignature;
	private final KeyPair walletTokenSignature;
	private final DemoUserDetailsService userDetailsService;
	
	public AccessTokenFilter(KeyPair idTokenSignature, KeyPair walletTokenSignature, 
			DemoUserDetailsService userDetailsService) {
		
		this.idTokenSignature = idTokenSignature;
		this.walletTokenSignature = walletTokenSignature;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
    protected void doFilterInternal(HttpServletRequest httpRequest,
                                    HttpServletResponse httpResponse,
                                    FilterChain chain) throws IOException, ServletException { 
		
		//get wallet token
		//get id token
		//parse and verify id token
		//read user id from id token
		//
		String walletToken = httpRequest.getHeader("Authorization").substring("Bearer ".length());
		
		
		if (walletToken != null) {

			String idToken = JwtUtil.getSubjectFromJws(walletToken, walletTokenSignature.getPublic());
			if(idToken != null) {
				String userId = JwtUtil.getSubjectFromJws(idToken, idTokenSignature.getPublic());
				if ( userId != null ) {
					
					// Get user
					UserDetails userDetails = userDetailsService.loadUserByUsername( userId );
					
					// Create authentication
					TokenAuthentication authentication = new TokenAuthentication( userDetails );
					authentication.setToken( idToken );
					SecurityContextHolder.getContext().setAuthentication( authentication );
				} 
				
			}
	    } 
		chain.doFilter(httpRequest, httpResponse);
	}

}
