package com.demo.authserver.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.authserver.user.details.DemoUserPrincipal;
import com.demo.authserver.user.model.User;
import com.demo.authserver.user.repo.UserRepository;

@Service
public class DemoUserDetailsService implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;
 
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new DemoUserPrincipal(user);
    }
    
    
}
