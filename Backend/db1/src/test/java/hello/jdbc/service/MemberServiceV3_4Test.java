package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜잭션 - DataSource, TransactionManager 자동 등록
 */
@Slf4j
@SpringBootTest
class MemberServiceV3_4Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV3 memberRepositoryV3;
    private MemberServiceV3_3 memberServiceV3_3;

    @Autowired
    public MemberServiceV3_4Test(MemberRepositoryV3 memberRepositoryV3, MemberServiceV3_3 memberServiceV3_3) {
        this.memberRepositoryV3 = memberRepositoryV3;
        this.memberServiceV3_3 = memberServiceV3_3;
    }

    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;

        TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        MemberRepositoryV3 memberRepositoryV3() {
            return new MemberRepositoryV3(dataSource);
        }

        @Bean
        MemberServiceV3_3 memberServiceV3_3() {
            return new MemberServiceV3_3(memberRepositoryV3());
        }
    }

    @AfterEach
    void after() throws SQLException {
        memberRepositoryV3.deleteById(MEMBER_A);
        memberRepositoryV3.deleteById(MEMBER_B);
        memberRepositoryV3.deleteById(MEMBER_EX);
    }


    /**
     * memberService class=class hello.jdbc.service.MemberServiceV3_3$$EnhancerBySpringCGLIB$$5e3ea0da // 실제 memberService 가 아니라 proxy 임
     * memberRepository class=class hello.jdbc.repository.MemberRepositoryV3
     */
    @Test
    void AopCheck() {
        log.info("memberService class={}", memberServiceV3_3.getClass());
        log.info("memberRepository class={}", memberRepositoryV3.getClass());

        assertThat(AopUtils.isAopProxy(memberRepositoryV3)).isFalse();
        assertThat(AopUtils.isAopProxy(memberServiceV3_3)).isTrue();
    }


    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepositoryV3.save(memberA);
        memberRepositoryV3.save(memberB);

        // when
        memberServiceV3_3.accountTransfer(MEMBER_A, MEMBER_B, 2000);

        // then
        Member findMemberA = memberRepositoryV3.findById(MEMBER_A);
        Member findMemberB = memberRepositoryV3.findById(MEMBER_B);

        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체 중 예외 발생")
    void accountTransferEx() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 10000);
        memberRepositoryV3.save(memberA);
        memberRepositoryV3.save(memberEx);

        // when
        log.info("Tx Start");
        assertThatThrownBy(() -> {
            memberServiceV3_3.accountTransfer(MEMBER_A, MEMBER_EX, 2000);
        }).isInstanceOf(IllegalStateException.class);
        log.info("Tx End");

        // then
        Member findMemberA = memberRepositoryV3.findById(MEMBER_A);
        Member findMemberEx = memberRepositoryV3.findById(MEMBER_EX);

        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberEx.getMoney()).isEqualTo(10000);
    }
}