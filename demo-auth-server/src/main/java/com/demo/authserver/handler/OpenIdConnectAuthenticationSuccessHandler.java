package com.demo.authserver.handler;

import java.io.IOException;
import java.security.KeyPair;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.demo.authserver.config.properties.TokenProperties;
import com.demo.authserver.jwt.JwtUtil;

@Component
@EnableConfigurationProperties(TokenProperties.class)
public class OpenIdConnectAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final TokenProperties tokenProperties;
	private final KeyPair idTokenSignature;
	private final KeyPair walletTokenSignature;
	
	
	public OpenIdConnectAuthenticationSuccessHandler(TokenProperties tokenProperties,
			KeyPair idTokenSignature, KeyPair walletTokenSignature) {
		this.tokenProperties = tokenProperties;
		this.idTokenSignature = idTokenSignature;
		this.walletTokenSignature = walletTokenSignature;
	}
	

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		
		//generate id token
		//generate wallet token
		//add wallet token in Authorization header on response	
		
		String userid = authentication.getName();
		
		String idToken = JwtUtil.generateJws(userid,
				tokenProperties.getIdToken().getAudience(), 
				tokenProperties.getIdToken().getIssuer(),
				tokenProperties.getIdToken().getExpirationTime(), 
				idTokenSignature.getPrivate());
		
		
		
		String walletToken = JwtUtil.generateJws(idToken,
				tokenProperties.getWalletToken().getAudience(), 
				tokenProperties.getWalletToken().getIssuer(),
				tokenProperties.getWalletToken().getExpirationTime(), 
				walletTokenSignature.getPrivate());
		
		response.setHeader("Authorization", "Bearer " + walletToken);
		
		
		
	}
	
	

}
