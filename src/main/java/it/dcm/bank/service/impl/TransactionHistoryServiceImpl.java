package it.dcm.bank.service.impl;


import it.dcm.bank.entity.TransactionHistoryEntity;
import it.dcm.bank.generated.dto.TransactionDto;
import it.dcm.bank.mapper.TransactionMapper;
import it.dcm.bank.repository.TransactionHistoryRepository;
import it.dcm.bank.service.TransactionHistoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionHistoryServiceImpl implements TransactionHistoryService {
    private final TransactionHistoryRepository repository;
    private final TransactionMapper transactionMapper;

    @Override
    public void saveAllTransactions(List<TransactionDto> transactions) {
        if (transactions == null) {
            log.info("Transactions are null, nothing to save on DB");
            return;
        }

        if (transactions.isEmpty()) {
            log.info("Transactions are empty, nothing to save on DB");
            return;
        }

        log.info("Saving on db {} transactions", transactions.size());
        List<TransactionHistoryEntity> entities = transactions.parallelStream()
                .map(transactionMapper::toEntity)
                .toList();
        try {
            this.repository.saveAllAndFlush(entities);
            log.info("All transactions saved");
        } catch (Exception ex) {
            log.error("Error occurred during the saving of transactions. ", ex);
        }
    }

}
