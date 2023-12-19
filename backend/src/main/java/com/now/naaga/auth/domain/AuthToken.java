package com.now.naaga.auth.domain;

import com.now.naaga.member.domain.Member;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "authToken", timeToLive = 60)
public class AuthToken {

    @Id
    private String refreshToken;

    private String accessToken;

    private Member member;

    public AuthToken() {
    }

    public AuthToken(final String accessToken, final String refreshToken, final Member member) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AuthToken authToken = (AuthToken) o;
        return Objects.equals(refreshToken, authToken.refreshToken) && Objects.equals(accessToken, authToken.accessToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, refreshToken);
    }

    @Override
    public String toString() {
        return "AuthTokens{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
