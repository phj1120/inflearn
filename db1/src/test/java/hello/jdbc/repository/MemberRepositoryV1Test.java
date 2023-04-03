package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.UUID;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach() {
        // 기본 DriverManager -항상 새로운 커넥션을 획득
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // 커넥션 플링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);

        repository = new MemberRepositoryV1(dataSource);
    }


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

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}