package kz.s1lk.pay.account.api.respository;

import kz.s1lk.pay.account.api.respository.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByNumber(String number);
}
