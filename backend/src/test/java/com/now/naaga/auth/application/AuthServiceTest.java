package com.now.naaga.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.now.naaga.auth.application.dto.AuthCommand;
import com.now.naaga.auth.application.dto.RefreshTokenCommand;
import com.now.naaga.auth.domain.AuthToken;
import com.now.naaga.auth.exception.AuthException;
import com.now.naaga.auth.exception.AuthExceptionType;
import com.now.naaga.auth.infrastructure.AuthType;
import com.now.naaga.auth.infrastructure.dto.AuthInfo;
import com.now.naaga.auth.infrastructure.dto.MemberAuth;
import com.now.naaga.auth.infrastructure.jwt.AuthTokenGenerator;
import com.now.naaga.common.ServiceTest;
import com.now.naaga.common.exception.BaseExceptionType;
import com.now.naaga.member.domain.Member;
import com.now.naaga.player.domain.Player;
import com.now.naaga.score.domain.Score;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class AuthServiceTest extends ServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Test
    void 존재하지_않는_멤버는_저장_후_토큰을_발급한다() {
        // given
        final Member member = new Member("chae@chae.com");
        final Player player = new Player("chae", new Score(0), member);
        final AuthCommand authCommand = new AuthCommand("1234", AuthType.KAKAO);

        when(authClient.requestOauthInfo(any())).thenReturn(AuthInfo.of(member.getEmail(), player.getNickname()));

        // when
        final AuthToken actual = authService.login(authCommand);
        final Optional<Member> maybeMember = memberRepository.findByEmail(member.getEmail());

        //given
        assertSoftly(softly -> {
            softly.assertThat(maybeMember).isPresent();
            softly.assertThat(actual).isNotNull();
        });
    }

    @Test
    void 존재하는_멤버는_저장_후_토큰을_발급한다() {
        // given
        final Player player = playerBuilder.init()
                                           .build();
        final AuthCommand authCommand = new AuthCommand("1234", AuthType.KAKAO);

        when(authClient.requestOauthInfo(any())).thenReturn(AuthInfo.of(player.getMember().getEmail(), player.getNickname()));

        // when
        final AuthToken actual = authService.login(authCommand);

        //given
        assertThat(actual).isNotNull();
    }

    @Test
    void 탈퇴한다() {
        // given
        final Player player = playerBuilder.init()
                                           .build();
        final long authId = 1L;
        final MemberAuth memberAuth = new MemberAuth(player.getMember().getId(), authId, AuthType.KAKAO);
        doNothing().when(authClient).requestUnlink(authId);
        playerRepository.flush();

        // when
        authService.deleteAccount(memberAuth);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(memberRepository.findById(player.getMember().getId())).isEmpty();
            softly.assertThat(playerRepository.findById(player.getId())).isEmpty();
        });
    }


    @Test
    void 로그아웃한다() {
        // given
        final Player player = playerBuilder.init()
                                           .build();
        final long authId = 1L;
        final MemberAuth memberAuth = new MemberAuth(player.getMember().getId(), authId, AuthType.KAKAO);
        doNothing().when(authClient).requestUnlink(authId);
        playerRepository.flush();

        // when
        authService.logout(memberAuth);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(memberRepository.findById(player.getMember().getId())).isPresent();
            softly.assertThat(playerRepository.findById(player.getId())).isPresent();
        });
    }

    @Test
    void 액세스_토큰이_만료되면_리프레시_토큰을_소모해_새로운_액세스_토큰을_발급받는다() throws InterruptedException {
        // given
        final Player player = playerBuilder.init()
                                           .build();

        final AuthToken authToken = authTokenGenerator.generate(player.getMember(), 1L, AuthType.KAKAO);
        authTokenRepository.save(authToken);
        Thread.sleep(5000L);

        // when
        final AuthToken newAuthToken = authService.refreshLogin(new RefreshTokenCommand(authToken.getRefreshToken()));

        // then
        assertThat(newAuthToken.getAccessToken()).isNotEqualTo(authToken.getAccessToken());
    }
}
