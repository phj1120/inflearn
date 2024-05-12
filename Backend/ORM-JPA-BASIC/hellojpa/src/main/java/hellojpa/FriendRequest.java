//// TODO FriendShip 하나로 처리할 지 FriendRequest 로 처리할 지 고민 해보기
//// 친구 요청, 수락 매커니즘을 domain 에 담아야할지 repository 에 담아야 할지를 아직 모르겠음
//// 친구 요청을 무지성으로 보내는 사용자가 있으면 어떡할지? -> 테이블을 나누려는 이유
//
//package hellojpa;
//
//import javax.persistence.*;
//
//@Entity
//public class FriendRequest {
//
//    @Id
//    @GeneratedValue
//    @Column(name = "FRIEND_REQUEST_ID")
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "REQUESTER_ID", referencedColumnName="MEMBER_ID")
//    private Member requester;
//
//    @ManyToOne
//    @JoinColumn(name = "TARGET_ID", referencedColumnName="MEMBER_ID")
//    private Member target;
//
//    @Enumerated
//    private FriendshipState friendshipState;
//}
