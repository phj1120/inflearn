package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    LogRepository logRepository;

    /**
     * memberService @Transactional: OFF
     * memberRepository @Transactional:ON
     * logRepository @Transactional:ON
     */
    @Test
    void outerTxOff_success() {
        // given
        String username = "outerTxOff_success";

        // when
        memberService.joinV1(username);

        // then
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService @Transactional: OFF
     * memberRepository @Transactional:ON
     * logRepository @Transactional:ON Exception
     */
    @Test
    void outerTxOff_fail() {
        // given
        String username = "로그예외_outerTxOff_success";

        // when
        assertThatThrownBy(() -> {
            memberService.joinV1(username);
        }).isInstanceOf(RuntimeException.class);

        // then
        assertTrue(memberRepository.find(username).isPresent()); // member 저장
        assertTrue(logRepository.find(username).isEmpty());      // log rollback
    }

    /**
     * memberService @Transactional: ON
     * memberRepository @Transactional:OFF
     * logRepository @Transactional:OFF
     */
    @Test
    void singleTx() {
        // given
        String username = "singleTx";

        // when
        memberService.joinV1(username);

        // then
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService @Transactional: ON
     * memberRepository @Transactional:ON
     * logRepository @Transactional:ON
     */
    @Test
    void outerTxOn_Success() {
        // given
        String username = "outerTxOn_Success";

        // when
        memberService.joinV1(username);

        // then
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService @Transactional: ON
     * memberRepository @Transactional:ON
     * logRepository @Transactional:ON Exception
     */
    @Test
    void outerTxOn_fail() {
        // given
        String username = "로그예외_outerTxOn_fail";

        // when
        assertThatThrownBy(() -> {
            memberService.joinV1(username);
        }).isInstanceOf(RuntimeException.class);

        // then
        assertTrue(memberRepository.find(username).isEmpty()); // member rollback
        assertTrue(logRepository.find(username).isEmpty());    // log rollback
    }

    /**
     * memberService @Transactional: ON
     * memberRepository @Transactional:ON
     * logRepository @Transactional:ON Exception
     *
     * repository 에서 발생한 예외를 service 에서 잡는다고 해도,
     * 논리 트랜잭션에서 rollbackOnly = true 가 되어 있기 때문에,
     * 물리 트랜잭션 종료 시점에 commit 호출 하지만,
     * rollbackOnly 가 true 이기 때문에
     * rollback 후 UnexpectedRollbackException 반환.
     */
    @Test
    void recoverException_fail() {
        // given
        String username = "로그예외_recoverException_fail";

        // when
        assertThatThrownBy(() -> {
            memberService.joinV2(username);
        }).isInstanceOf(UnexpectedRollbackException.class);

        // then
        assertTrue(memberRepository.find(username).isEmpty()); // member rollback
        assertTrue(logRepository.find(username).isEmpty());    // log rollback
    }

    /**
     * memberService @Transactional: ON
     * memberRepository @Transactional:ON
     * logRepository @Transactional:ON(REQUIRES_NEW) Exception
     */
    @Test
    void recoverException_success() {
        // given
        String username = "로그예외_recoverException_fail";

        // when
        memberService.joinV2(username);

        // then
        assertTrue(memberRepository.find(username).isPresent()); // member commit
        assertTrue(logRepository.find(username).isEmpty());    // log rollback
    }
}