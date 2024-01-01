package it.dcm.bank.repository;


import it.dcm.bank.entity.TransactionHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistoryEntity, String> {
}
