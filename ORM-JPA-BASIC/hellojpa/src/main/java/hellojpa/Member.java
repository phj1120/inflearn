package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "name")
    private String username;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

//    @OneToMany(mappedBy = "friend")
//    private List<Member> friends = new ArrayList<>();
//
//    @ManyToOne
//    @JoinColumn(name = "friend_id")
//    private Member friend;

    @ManyToMany
    @JoinTable(name = "MEMBER_MEMBER")
    private List<Member> requesterMember = new ArrayList<>();

    @ManyToMany(mappedBy = "requesterMember")
    private List<Member> targetMember = new ArrayList<>();

    public void addFriend(Member member) {
        this.requesterMember.add(member);
        member.targetMember.add(this);
    }


}
