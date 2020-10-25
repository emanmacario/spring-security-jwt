package com.emanmacario.springsecurityjwt.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

// TODO: Maybe change this to DatabaseUserDetailsService
// https://reflectoring.io/spring-security-password-handling/

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO: In a real world production, would connect this to a UserRepository to return user from database
        // TODO: Would also need to hash the database
        return new User("foo", "foo", new ArrayList<>());
    }
}
