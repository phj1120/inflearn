package hello.jdbc.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class TxOuterService {
    private final TxRepository txRepository;
    private final TxInnerService txInnerService;

    public TxOuterService(TxRepository txRepository, TxInnerService txInnerService) {
        this.txRepository = txRepository;
        this.txInnerService = txInnerService;
    }

    public void dependsOnParentTransactions_parentNxChildTx() {
        log.info("[requiredOuter.start]");
        txRepository.txException(false);

        txInnerService.inner_Required(false);

        txRepository.txException(false);
        log.info("[requiredOuter.end]");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void dependsOnParentTransactions_parentTxChildTx() {
        log.info("[requiredOuter.start]");
        txRepository.txException(false);

        txInnerService.inner_Required(false);

        txRepository.txException(false);
        log.info("[requiredOuter.end]");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void inDependsOnParentTransactions_parentTxChildTx() {
        log.info("[requiredOuter.start]");
        txRepository.txException(false);

        txInnerService.inner_RequiresNew(false);

        txRepository.txException(false);
        log.info("[requiredOuter.end]");
    }
}
