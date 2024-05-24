package com.vicheak.mbankingapi.api.transaction;

import com.vicheak.mbankingapi.api.account.Account;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "transaction_uuid", unique = true, nullable = false)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "sender_act_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_act_id")
    private Account receiver;

    @Column(name = "transaction_amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_remark")
    private String remark;

    @CreationTimestamp
    @Column(name = "transaction_at", nullable = false, updatable = false)
    private LocalDateTime transactionAt;

    @Column(name = "is_payment")
    private Boolean isPayment;

    @Column(name = "student_card_no")
    private String studentCardNo;

}
