package com.vicheak.mbankingapi.api.account;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account_types")
public class AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "act_type_id")
    private Integer id;

    @Column(name = "act_type_name", unique = true, nullable = false)
    private String name;

}
