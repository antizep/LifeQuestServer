package ru.antizep.lifequestserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.antizep.lifequestserver.entity.UserEntity;
import ru.antizep.lifequestserver.repository.UserRepository;

import java.util.Collections;
import java.util.List;

// todo покрыть микросервис тестами
@Component
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        UserEntity user = repository.getByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if(!user.isEnabled()) {
            throw new UsernameNotFoundException("User " + username + " blocked");
        }
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getAuthority()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }
}
