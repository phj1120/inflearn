package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        String memberId = "memberV" + UUID.randomUUID().toString().substring(0, 3);

        // 추가
        Member member = new Member(memberId, 10000);
        repository.save(member);

        // 조회
        Member findMember = repository.findById(memberId);
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(member);

        // 수정
        repository.update(memberId, 20000);
        Member findUpdateMember = repository.findById(memberId);
        log.info("findUpdateMember={}", findUpdateMember);
        assertThat(findUpdateMember.getMoney()).isEqualTo(20000);

        // 삭제
        repository.deleteById(memberId);

        // 예외 처리
        assertThatThrownBy(() -> {
            repository.findById(memberId);
        }).isInstanceOf(NoSuchElementException.class);
    }

}