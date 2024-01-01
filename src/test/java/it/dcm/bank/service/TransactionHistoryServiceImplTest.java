package it.dcm.bank.service;

import it.dcm.bank.entity.TransactionHistoryEntity;
import it.dcm.bank.generated.dto.TransactionDto;
import it.dcm.bank.mapper.TransactionMapper;
import it.dcm.bank.repository.TransactionHistoryRepository;
import it.dcm.bank.service.impl.TransactionHistoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class TransactionHistoryServiceImplTest {

    @Mock
    private TransactionHistoryRepository repository;
    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionHistoryServiceImpl service;


    @Test
    void saveAllTransactions_whenTransactionsAreNull() {
        service.saveAllTransactions(null);
        verify(repository, never()).saveAllAndFlush(anyList());
    }

    @Test
    void saveAllTransactions_whenTransactionsAreEmpty() {
        service.saveAllTransactions(Collections.emptyList());
        verify(repository, never()).saveAllAndFlush(anyList());
    }

    @Test
    void saveAllTransactions_whenTransactionsArePresent() {
        TransactionDto transactionDto = mock(TransactionDto.class);
        TransactionHistoryEntity entity = mock(TransactionHistoryEntity.class);
        when(transactionMapper.toEntity(transactionDto)).thenReturn(entity);

        service.saveAllTransactions(Collections.singletonList(transactionDto));

        verify(transactionMapper).toEntity(transactionDto);
        verify(repository).saveAllAndFlush(Collections.singletonList(entity));
    }

    @Test
    void saveAllTransactions_whenSaveThrowsException() {
        TransactionDto transactionDto = mock(TransactionDto.class);
        when(transactionMapper.toEntity(transactionDto)).thenReturn(new TransactionHistoryEntity());
        doThrow(new RuntimeException()).when(repository).saveAllAndFlush(anyList());

        Assertions.assertDoesNotThrow(() ->
            service.saveAllTransactions(Collections.singletonList(transactionDto))
        );

        verify(transactionMapper).toEntity(transactionDto);
        verify(repository).saveAllAndFlush(anyList());
    }
}
