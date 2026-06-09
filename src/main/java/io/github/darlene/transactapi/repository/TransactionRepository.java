package io.github.darlene.transactapi.repository;

import io.github.darlene.transactapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    Optional<Transaction> findByTransferReference(String transferReference);

    //block duplicate submissions
    boolean existsByTransferReference(String transferReference);
}