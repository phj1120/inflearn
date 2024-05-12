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
        log.info("[  inner_RequiresNew.start]");
        txRepository.txException(isInnerExcept);
        log.info("[  inner_RequiresNew.end]");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void inner_Required(boolean isInnerExcept) {
        log.info("[  inner_Required.start]");
        txRepository.txException(isInnerExcept);
        log.info("[  inner_Required.end]");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void update_RequiresNew(String memberId, int money) {
        log.info("[   update_RequiresNew.start");
        txRepository.update(memberId, money);
        log.info("[   update_RequiresNew.end");
    }
}
