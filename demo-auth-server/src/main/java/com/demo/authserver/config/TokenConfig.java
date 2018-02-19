package com.demo.authserver.config;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.demo.authserver.config.properties.TokenProperties;

@Configuration
@EnableConfigurationProperties(TokenProperties.class)
public class TokenConfig {
	
	private final TokenProperties tokenProperties;
	private final KeyStoreKeyFactory keyStore;
	
	public TokenConfig(TokenProperties tokenProperties) {
		this.tokenProperties = tokenProperties;
		keyStore = new KeyStoreKeyFactory(
			new ClassPathResource(tokenProperties.getKeyStoreLocation()), 
			tokenProperties.getKeyPass().toCharArray());
	}
	
	
	@Bean
	public KeyPair idTokenSignature() {
		return keyStore.getKeyPair(tokenProperties.getIdToken().getKeyAlias());
	}
	
	@Bean
	public KeyPair walletTokenSignature() {
		return keyStore.getKeyPair(tokenProperties.getWalletToken().getKeyAlias());
	}
	
	@Bean
	public KeyPair accessTokenSignature() {
		return keyStore.getKeyPair(tokenProperties.getAccessToken().getKeyAlias());
	}

}
