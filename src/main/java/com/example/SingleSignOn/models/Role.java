package com.example.SingleSignOn.models;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.SingleSignOn.models.Permission.*;


@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_CREATE,
                    ADMIN_UPDATE,
                    ADMIN_DELETE
            )),
    USER(
            Set.of(
                    USER_READ,
                    USER_CREATE,
                    USER_UPDATE
            )),
    ;

    @Getter
    private final Set<Permission> permissionSet;

    public List<SimpleGrantedAuthority> grantedAuthorities () {
        var authorites = getPermissionSet().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList();
        authorites.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorites;
    }
}

