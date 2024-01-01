package it.dcm.bank.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "TRANSACTION_HISTORY")
public class TransactionHistoryEntity {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "OPERATION_ID", nullable = false)
    private String operationId;
    @Column(name = "ACCOUNTING_DATE", nullable = false)
    private LocalDate accountingDate;
    @Column(name = "VALUE_DATE", nullable = false)
    private LocalDate valueDate;
    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;
    @Column(name = "CURRENCY", nullable = false)
    private String currency;
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @Column(name = "TYPE_TRANSACTION")
    private String typeTransaction;


    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionHistoryEntity that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getOperationId(), that.getOperationId());
    }
}
