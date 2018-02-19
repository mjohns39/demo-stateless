package com.demo.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.demo.authserver.filter.AccessTokenFilter;
import com.demo.authserver.handler.OpenIdConnectAuthenticationSuccessHandler;
import com.demo.authserver.user.service.DemoUserDetailsService;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired 
	OpenIdConnectAuthenticationSuccessHandler openIdConnectAuthenticationSuccessHandler;
	
	@Autowired
	DemoUserDetailsService userDetailsService;
	
	@Autowired
	AccessTokenFilter accessTokenFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {		
	    http	
	    		.csrf().disable()
	    		.formLogin().permitAll().successHandler(openIdConnectAuthenticationSuccessHandler)
	    		.and()
	    		.anonymous().disable()
	    		.httpBasic()
	    		.and()
	    		.authorizeRequests().anyRequest().authenticated()
	    		.and()
	    		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    		.and()
	    		.addFilterBefore(accessTokenFilter, BasicAuthenticationFilter.class);
//	    		.and()
//	    		.openidLogin()
//	    		.successHandler(successHandler)
	    		;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//			.withUser("user").password("pass").roles("USER")
//			.and()
//			.withUser("admin").password("adminpass").authorities("ROLE_ADMIN");
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider
	      = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(encoder());
	    return authProvider;
	}
	 
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder(11);
	}
	
	
}
