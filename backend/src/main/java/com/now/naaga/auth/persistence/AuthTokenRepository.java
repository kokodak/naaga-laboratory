package com.now.naaga.auth.persistence;

import com.now.naaga.auth.domain.AuthToken;
import org.springframework.data.repository.CrudRepository;

public interface AuthTokenRepository extends CrudRepository<AuthToken, String> {

    void deleteByMemberId(Long memberId);
}
