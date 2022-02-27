package ru.antizep.lifequestserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.antizep.lifequestserver.entity.UserEntity;
import ru.antizep.lifequestserver.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class UserDetailService implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public UserDetailService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        UserEntity user = repository.getByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if(!user.isEnabled()) {
            throw new UsernameNotFoundException("Позьзователь " + username + "заблокирован");
        }
        List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(user.getAuthority()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }
}
