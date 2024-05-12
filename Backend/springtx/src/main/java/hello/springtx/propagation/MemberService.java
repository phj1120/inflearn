package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    @Transactional
    public void joinV1(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("memberRepository 시작");
        memberRepository.save(member);
        log.info("memberRepository 종료");

        log.info("logRepository 시작");
        logRepository.save(logMessage);
        log.info("logRepository 종료");
    }

    @Transactional
    public void joinV2(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("memberRepository 시작");
        memberRepository.save(member);
        log.info("memberRepository 종료");

        log.info("logRepository 시작");
        try {
            logRepository.save(logMessage);
        } catch (RuntimeException e) {
            log.info("log 저장 실패. logMessage={}", logMessage.getMessage());
            log.info("정상흐름 반환");
        }
    }
}
