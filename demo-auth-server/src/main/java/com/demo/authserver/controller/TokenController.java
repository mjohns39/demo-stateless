package com.demo.authserver.controller;

import java.security.KeyPair;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.authserver.authentication.TokenAuthentication;
import com.demo.authserver.config.properties.TokenProperties;
import com.demo.authserver.jwt.JwtUtil;

@EnableConfigurationProperties(TokenProperties.class)
@RestController
@RequestMapping("/oauth")
public class TokenController {
	
	private final TokenProperties tokenProperties;
	private final KeyPair walletTokenSignature;
	private final KeyPair accessTokenSignature;
		
	public TokenController(TokenProperties tokenProperties,
			KeyPair walletTokenSignature, KeyPair accessTokenSignature) {
		this.tokenProperties = tokenProperties;
		this.walletTokenSignature = walletTokenSignature;
		this.accessTokenSignature = accessTokenSignature;
	}

	@PostMapping("/token")
	public String token(HttpServletResponse httpResponse) {
		
		String idToken = ((TokenAuthentication) SecurityContextHolder.getContext().getAuthentication()).getToken();
		
		String walletToken = JwtUtil.generateJws(idToken,
				tokenProperties.getWalletToken().getAudience(), 
				tokenProperties.getWalletToken().getIssuer(),
				tokenProperties.getWalletToken().getExpirationTime(), 
				walletTokenSignature.getPrivate());
		
		String accessToken = JwtUtil.generateJws(idToken,
				tokenProperties.getAccessToken().getAudience(), 
				tokenProperties.getAccessToken().getIssuer(),
				tokenProperties.getAccessToken().getExpirationTime(), 
				accessTokenSignature.getPrivate());
		httpResponse.setHeader("Authorization", "Bearer " + walletToken);
		return accessToken;
	}
	
//	@PostMapping("/authorize")
//	public String authorize() {
//		return "authorize";
//	}
}
