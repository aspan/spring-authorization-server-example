package com.example.security.persistence.service;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.security.persistence.repository.UserEntityRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserEntityRepository userEntityRepository;

    public UserDetailsService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        var entityOptional = userEntityRepository.findByUsername(username);
        if (entityOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        var entity = entityOptional.get();
        return new User(entity.getUsername(), entity.getPassword(), entity.getEnabled(), true, true, true, entity.getAuthorities().stream().map(a -> new SimpleGrantedAuthority(a.getAuthority())).toList());
    }
}
