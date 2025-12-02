package org.example.ecommerceorderandinventory.security;

import org.example.ecommerceorderandinventory.entity.user.User;
import org.example.ecommerceorderandinventory.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username));

        return new UserDetailsImpl(user.getUsername(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())), user.getId());
    }
}
