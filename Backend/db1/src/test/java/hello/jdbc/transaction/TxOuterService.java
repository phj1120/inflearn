package hello.jdbc.transaction;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.springframework.transaction.annotation.Isolation;
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

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void nonRepeatableRead_Isolation_DEFAULT(String memberId) {
        Member memberBeforeUpdate = txRepository.read(memberId);
        log.info("memberBeforeUpdate: {}", memberBeforeUpdate);

        txInnerService.update_RequiresNew(memberId, memberBeforeUpdate.getMoney() + 1000);

        Member memberAfterUpdate = txRepository.read(memberId);
        log.info("memberAfterUpdate: {}", memberAfterUpdate);

        log.info("[Result]: memberBeforeUpdate isNotEqualTo memberAfterUpdate");
        Assertions.assertThat(memberBeforeUpdate).isNotEqualTo(memberAfterUpdate);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void nonRepeatableRead_Isolation_READ_UNCOMMITTED(String memberId) {
        Member memberBeforeUpdate = txRepository.read(memberId);
        log.info("memberBeforeUpdate: {}", memberBeforeUpdate);

        txInnerService.update_RequiresNew(memberId, memberBeforeUpdate.getMoney() + 1000);

        Member memberAfterUpdate = txRepository.read(memberId);
        log.info("memberAfterUpdate: {}", memberAfterUpdate);

        log.info("[Result]: memberBeforeUpdate isNotEqualTo memberAfterUpdate");
        Assertions.assertThat(memberBeforeUpdate).isNotEqualTo(memberAfterUpdate);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void nonRepeatableRead_Isolation_READ_COMMITTED(String memberId) {
        Member memberBeforeUpdate = txRepository.read(memberId);
        log.info("memberBeforeUpdate: {}", memberBeforeUpdate);

        txInnerService.update_RequiresNew(memberId, memberBeforeUpdate.getMoney() + 1000);

        Member memberAfterUpdate = txRepository.read(memberId);
        log.info("memberAfterUpdate: {}", memberAfterUpdate);

        log.info("[Result]: memberBeforeUpdate isNotEqualTo memberAfterUpdate");
        Assertions.assertThat(memberBeforeUpdate).isNotEqualTo(memberAfterUpdate);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void nonRepeatableRead_Isolation_REPEATABLE_READ(String memberId) {
        Member memberBeforeUpdate = txRepository.read(memberId);
        log.info("memberBeforeUpdate: {}", memberBeforeUpdate);

        txInnerService.update_RequiresNew(memberId, memberBeforeUpdate.getMoney() + 1000);

        Member memberAfterUpdate = txRepository.read(memberId);
        log.info("memberAfterUpdate: {}", memberAfterUpdate);

        log.info("[Result]: memberBeforeUpdate isEqualTo memberAfterUpdate");
        Assertions.assertThat(memberBeforeUpdate).isEqualTo(memberAfterUpdate);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void nonRepeatableRead_Isolation_SERIALIZABLE(String memberId) {
        Member memberBeforeUpdate = txRepository.read(memberId);
        log.info("memberBeforeUpdate: {}", memberBeforeUpdate);

        txInnerService.update_RequiresNew(memberId, memberBeforeUpdate.getMoney() + 1000);

        Member memberAfterUpdate = txRepository.read(memberId);
        log.info("memberAfterUpdate: {}", memberAfterUpdate);

        log.info("[Result]: memberBeforeUpdate isEqualTo memberAfterUpdate");
        Assertions.assertThat(memberBeforeUpdate).isEqualTo(memberAfterUpdate);
    }
}
