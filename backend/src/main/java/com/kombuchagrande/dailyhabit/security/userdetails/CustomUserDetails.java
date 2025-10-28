package com.kombuchagrande.dailyhabit.security.userdetails;


import com.kombuchagrande.dailyhabit.security.jwt.dto.JwtPayloadDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Objects;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final JwtPayloadDto payload;

    public Long getUserId() { return payload.userId(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + payload.role().name()));
    }

    @Override
    public String getPassword() { return ""; } // OIDC+JWT 흐름에서는 불필요/ 비밀번호 인증 사용 안함.

    @Override
    public String getUsername() {
        return String.valueOf(payload.userId());
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomUserDetails that)) return false;
        return Objects.equals(payload.userId(), that.payload.userId());
    }

    @Override
    public int hashCode() { return Objects.hash(payload.userId()); }
}