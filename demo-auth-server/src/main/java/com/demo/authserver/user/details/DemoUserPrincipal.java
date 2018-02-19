package com.demo.authserver.user.details;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.demo.authserver.user.model.Privilege;
import com.demo.authserver.user.model.User;

import lombok.Getter;

public class DemoUserPrincipal implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	@Getter
	private User user;
	 
    public DemoUserPrincipal(User user) {
        this.user = user;
    }
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (final Privilege privilege : user.getPrivileges()) {
            authorities.add(new SimpleGrantedAuthority(privilege.getName()));
        }
        return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
