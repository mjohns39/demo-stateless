package com.demo.authserver.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

public class TokenAuthentication extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private String token;
	private final UserDetails principal;
	
	public TokenAuthentication(UserDetails principal) {
		super(principal.getAuthorities());
		this.principal = principal;
	}

	@Override
    public boolean isAuthenticated() {
        return true;
    }
	
	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

}
