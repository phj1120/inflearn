package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
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

    @OneToMany(mappedBy = "requester")
    private List<Friendship> friendships = new ArrayList<>();

    /*
     * 친구 관계를 편하게 조회하려면 requester, target / target, requester
     * 이렇게 두 번 저장해야 조회하기 편하다고 함.
     * 그래서 한번에 두개 저장할 수 있게 하는 것을
     * Member 에서 하고 싶었으나
     * Member 가 연관관계 주인이 아니라 수정 불가능...
     * -> Repository 에서 관리하면 되는 건가?
     */
//    public void addFriend(Member member) {
//        Friend friend1 = new Friend(member, this);
//        Friend friend2 = new Friend(this, member);
//    }

    public List<Member> selectFriends() {
        List<Member> memberList = new ArrayList<>();
        for (Friendship friendship : friendships) {

            memberList.add(friendship.getRequester());
        }
        return memberList;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Team getTeam() {
        return team;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
