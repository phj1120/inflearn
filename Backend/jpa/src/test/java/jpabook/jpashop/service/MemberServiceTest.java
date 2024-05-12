package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional(readOnly = true)
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 회원가입() {
        // given
        Member member = new Member();
        member.setName("park");

        // when
        Long id = memberService.join(member);

        // then
        assertEquals(id, memberRepository.findOne(id).getId());
    }

    @Test(expected = IllegalStateException.class)
    @Transactional(readOnly = false)
    public void 중복회원예외() {

        // Given
        Member member1 = new Member();
        member1.setName("park");

        Member member2 = new Member();
        member2.setName("park");

        // When
        memberService.join(member1);
        memberService.join(member2);

        // Then
        fail("예외가 발생해야 합니다.");
    }
}