package com.demo.authserver.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("token")
@Getter
public class TokenProperties {
	
	@Setter
	private String keyStoreLocation;
	@Setter
	private String keyPass;
	private final Token idToken = new Token();
	private final Token walletToken = new Token();
	private final Token accessToken = new Token();
	

	@Data
	public static class Token {
		private long expirationTime;	
		private String issuer;		
		private String audience;
		private String keyAlias;
	}
}
