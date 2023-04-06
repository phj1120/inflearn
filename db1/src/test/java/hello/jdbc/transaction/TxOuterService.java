package hello.jdbc.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TxOuterService {
    private final TxRepository txRepository;
    private final TxInnerService txInnerService;

    @Transactional(propagation = Propagation.REQUIRED)
    public void requiredOuter(boolean isOuterExcept, boolean isInnerExcept) throws SQLException {
        try {
            log.info("[requiredOuter.start]");
//            txRepository.txException(isOuterExcept);
            txInnerService.requiredInner(isInnerExcept);
            txRepository.txException(isOuterExcept);
            log.info("[requiredOuter.end]");
        } catch (NoSuchElementException e) {
            log.error("----------------{}------------------", e.toString());
            throw new NoSuchElementException();
        }
    }

    // 이미 Transaction 이 걸린 상태에서 내부에서 호출 할 경우 @Transactional 설정 안 먹음
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiredInner(boolean isInnerExcept) throws SQLException {
        log.info("[  requiredInner.start]");
        txRepository.txException(isInnerExcept);
        log.info("[  requiredInner.end]");
    }

}
