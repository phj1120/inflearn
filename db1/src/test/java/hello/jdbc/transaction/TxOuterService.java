package hello.jdbc.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
public class TxOuterService {
    private final TxRepository txRepository;
    private final TxInnerService txInnerService;

    public TxOuterService(TxRepository txRepository, TxInnerService txInnerService) {
        this.txRepository = txRepository;
        this.txInnerService = txInnerService;
    }

    public void dependsOnParentTransactions_OuterNxInnerTx() {
        log.info("[requiredOuter.start]");
        txRepository.txException(false);

        txInnerService.inner_Required(false);

        txRepository.txException(false);
        log.info("[requiredOuter.end]");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void dependsOnParentTransactions_OuterTxInnerTx() {
        log.info("[requiredOuter.start]");
        txRepository.txException(false);

        txInnerService.inner_Required(false);

        txRepository.txException(false);
        log.info("[requiredOuter.end]");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void inDependsOnParentTransactions_OuterTxInnerTx(boolean isOuterExceptBeforeInner, boolean isInnerExcept, boolean isOuterExceptAfterInner) {
        log.info("[requiredOuter.start]");
        txRepository.txException(isOuterExceptBeforeInner);

        try {
            txInnerService.inner_RequiresNew(isInnerExcept);
        } catch (NoSuchElementException e) {
            log.info("inner 예외 처리 로직");
        }

        txRepository.txException(isOuterExceptAfterInner);
        log.info("[requiredOuter.end]");
    }
}
