package com.kombuchagrande.dailyhabit.oidc.toekn;

import com.kombuchagrande.dailyhabit.entity.enums.ProviderType;
import com.kombuchagrande.dailyhabit.exception.KidNotFoundException;
import com.kombuchagrande.dailyhabit.oidc.keys.dto.JwkDto;
import com.kombuchagrande.dailyhabit.oidc.keys.dto.JwksSetDto;
import com.kombuchagrande.dailyhabit.oidc.provider.AllowedAlg;
import com.kombuchagrande.dailyhabit.oidc.toekn.header.JwtHeaderDecoder;
import com.kombuchagrande.dailyhabit.oidc.toekn.header.JwtHeaderDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.Collection;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OidcIdTokenValidator {  //검증 책임 클래스

    private final JwtHeaderDecoder headerDecoder;

    // 헤더 선검증 (alg allowlist, kid 존재)
    public JwtHeaderDto precheckHeader(String idToken, Set<AllowedAlg> allowlist) {
        JwtHeaderDto header = headerDecoder.decode(idToken);
        if (header.kid() == null || header.kid().isBlank()) {
            throw bad("KID_MISSING", "kid missing");
        }
        if (header.alg() == null || header.alg().isBlank()) {
            throw bad("ALG_MISSING", "alg missing");
        }
        AllowedAlg alg = AllowedAlg.fromHeader(header.alg());
        if (!allowlist.contains(alg)) {
            throw bad("ALG_NOT_ALLOWED", "alg not allowed: " + header.alg());
        }
        return header;
    }

    // JWKS, kid로 PublicKey 생성
    public PublicKey publicKeyFromJwks(JwksSetDto jwksSet, String kid) {
        JwkDto jwk = jwksSet.keys().stream()
                .filter(j -> kid.equals(j.kid()))
                .findFirst()
                .orElseThrow(() -> new KidNotFoundException(kid));
        return toRsaPublicKey(jwk); // RSA 전용
    }

    // 서명/시간 검증 + Claims 파싱 (PublicKey를 인자로 받음)
    public Claims verifySignatureAndParse(String idToken, PublicKey key, Duration clockSkew) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(key)
                    .clockSkewSeconds(clockSkew.getSeconds())
                    .build()
                    .parseSignedClaims(idToken);
            return jws.getPayload();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw bad("TOKEN_EXPIRED", "token expired");
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw bad("SIGNATURE_INVALID", "invalid signature");
        } catch (io.jsonwebtoken.JwtException e) {
            throw bad("JWT_INVALID", "invalid jwt: " + e.getMessage());
        }
    }

    // 표준 클레임(iss/aud) 검증
    public void validateStandardClaims(Claims claims, String expectedIss, String expectedAud) {
        if (!expectedIss.equals(claims.getIssuer())) {
            throw bad("BAD_ISS", "issuer mismatch");
        }
        Object aud = claims.get("aud");
        if (!audOk(aud, expectedAud)) {
            throw bad("BAD_AUD", "audience mismatch");
        }
    }

    // 공급자별 추가 규칙 훅
    public void validateProviderSpecific(ProviderType type, Claims claims) {
        // 필요할 때 스위치로 분기 추가
        // ex) GOOGLE azp 체크, NAVER 특이 클레임 등
    }

    private PublicKey toRsaPublicKey(JwkDto jwk) {
        try {
            if (!"RSA".equalsIgnoreCase(jwk.kty())) {
                throw bad("UNSUPPORTED_KTY", "unsupported kty: " + jwk.kty());
            }
            if (jwk.n() == null || jwk.e() == null) {
                throw bad("MISSING_RSA_PARAMS", "missing RSA n/e for kid=" + jwk.kid());
            }
            BigInteger n = new BigInteger(1, Base64.getUrlDecoder().decode(jwk.n()));
            BigInteger e = new BigInteger(1, Base64.getUrlDecoder().decode(jwk.e()));
            RSAPublicKeySpec spec = new RSAPublicKeySpec(n, e);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception ex) {
            throw bad("RSA_BUILD_FAILED", "failed to build RSA key for kid=" + jwk.kid());
        }
    }

    private boolean audOk(Object audClaim, String expected) {
        if (audClaim == null) return false;
        if (audClaim instanceof String s) return expected.equals(s);
        if (audClaim instanceof Collection<?> c) return c.stream().anyMatch(v -> expected.equals(String.valueOf(v)));
        return expected.equals(String.valueOf(audClaim));
    }

    //Todo 커스텀 예외처리
    private IllegalArgumentException bad(String code, String msg) {
        return new IllegalArgumentException(code + ": " + msg);
    }
}