package com.vicheak.mbankingapi.api.account;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "act_id")
    private Long id;

    @Column(name = "act_no", unique = true, nullable = false)
    private String number;

    @Column(name = "act_name", unique = true, nullable = false)
    private String name;

    @Column(name = "act_pin", nullable = false)
    private String pin;

    @Column(name = "act_transfer_limit", nullable = false)
    private BigDecimal transferLimit;

    @ManyToOne
    @JoinColumn(name = "act_type_id", nullable = false)
    private AccountType accountType;

}
