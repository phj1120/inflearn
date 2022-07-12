package hellojpa;

import javax.persistence.*;


@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "name")
    private String username;

    @Column(name = "TEAM_ID")
    private String teamId;
}
