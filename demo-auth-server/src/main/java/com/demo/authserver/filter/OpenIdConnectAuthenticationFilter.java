package com.demo.authserver.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class OpenIdConnectAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	protected OpenIdConnectAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
        setAuthenticationManager(authenticationManager);
	}

}
