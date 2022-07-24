package hellojpa;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship {

    @Id
    @GeneratedValue
    @Column(name = "FRIENDSHIP_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "REQUESTER_ID", referencedColumnName = "MEMBER_ID")
    private Member requester;

    @ManyToOne
    @JoinColumn(name = "TARGET_ID", referencedColumnName = "MEMBER_ID")
    private Member target;

    public Friendship(Member requester, Member target) {
        this.requester = requester;
        this.target = target;
    }


}
