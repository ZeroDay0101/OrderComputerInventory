package org.example.ecommerceorderandinventory.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class UserDetailsImpl extends User {
    private final long userId;

    public UserDetailsImpl(String username, String password, Collection<? extends GrantedAuthority> authorities, long userId) {
        super(username, password, authorities);
        this.userId = userId;
    }


}
