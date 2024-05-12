package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.querydsl.entity.Team;

import java.util.Optional;

import static study.querydsl.entity.QTeam.team;

@Repository
@Transactional
@RequiredArgsConstructor
public class TeamJpaRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public void save(Team team) {
        em.persist(team);
    }

    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(team)
                        .where(team.id.eq(id))
                        .fetchOne()
        );
    }
}
