package com.kombuchagrande.dailyhabit.security.userdetails;

import com.kombuchagrande.dailyhabit.entity.User;
import com.kombuchagrande.dailyhabit.entity.enums.Role;
import com.kombuchagrande.dailyhabit.oidc.dto.VerifiedOidc;
import com.kombuchagrande.dailyhabit.repository.UserRepository;
import com.kombuchagrande.dailyhabit.security.jwt.dto.JwtPayloadDto;
import com.kombuchagrande.dailyhabit.security.jwt.dto.LoadOrCreateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OidcUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 이 서비스는 username/password 로그인용이 아니다.
    // 만약 누가 실수로 loadUserByUsername()을 호출하면 즉시 실패처리
    @Override
    public UserDetails loadUserByUsername(String username) {
        throw new UnsupportedOperationException("Use loadOrCreate(VerifiedOidc) for OIDC flow.");
    }

    public LoadOrCreateResult loadOrCreate(VerifiedOidc verifiedOidc) {
        Optional<User> found = userRepository.findByProviderTypeAndProviderId(verifiedOidc.providerType(), verifiedOidc.sub());


        boolean isNew = found.isEmpty();
        User user = found.orElseGet(() -> {
            User saveuser = User.builder()
                    .providerType(verifiedOidc.providerType())
                    .providerId(verifiedOidc.sub())
                    .nickname("user-" + UUID.randomUUID().toString().substring(0, 8))
                    .role(Role.USER)
                    .notificationEnabled(false) // Todo 알림 설정도 초기 회원가입에서 허용 여부 값 받기
                    .build();
            return userRepository.save(saveuser);
        });

        JwtPayloadDto payload = JwtPayloadDto.fromUser(user);
        return new LoadOrCreateResult(new CustomUserDetails(payload), isNew);
    }
}