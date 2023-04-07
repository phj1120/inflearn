package hello.jdbc.repository;

import hello.jdbc.domain.Member;

import java.sql.SQLException;

public interface MemberRepository {

    Member save(Member member);
    void update(String memberId, int money);
    void deleteById(String memberId);
    Member findById(String memberId);
}
