package com.demo.authserver.jwt;

import java.security.Key;
import java.security.KeyPair;
import java.util.Date;
import java.util.UUID;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import com.demo.authserver.config.properties.TokenProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class JwtUtil {

	
	public static String generateJws(String sub, String aud, String iss, long exp, Key key) {
		String jws = Jwts.builder()
				.setSubject(sub)
				.setExpiration(new Date(System.currentTimeMillis() + exp))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setId(UUID.randomUUID().toString())
				.setAudience(aud)
				.setIssuer(iss)
				.signWith(SignatureAlgorithm.RS256, key)
				.compact();
		
		return jws;
	}
	
	public static Jws<Claims> validateJws(String jws, Key key) {
		Jws<Claims> result = null;
		try {
			result = Jwts.parser().setSigningKey(key).parseClaimsJws(jws);
		} catch (SignatureException ex) {
			
		} catch (Exception ex) {
			
		}
		
		return result;
	}
	
	public static String getSubjectFromJws(String jws, Key key) {
		return validateJws(jws, key).getBody().getSubject();
	}

}
