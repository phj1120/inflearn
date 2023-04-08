package hello.jdbc.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class TxInnerService {
    private final TxRepository txRepository;

    public TxInnerService(TxRepository txRepository) {
        this.txRepository = txRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void inner_RequiresNew(boolean isInnerExcept) {
        log.info("[  requiredInner.start]");
        txRepository.txException(isInnerExcept);
        log.info("[  requiredInner.end]");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void inner_Required(boolean isInnerExcept) {
        log.info("[  requiredInner.start]");
        txRepository.txException(isInnerExcept);
        log.info("[  requiredInner.end]");
    }

}
