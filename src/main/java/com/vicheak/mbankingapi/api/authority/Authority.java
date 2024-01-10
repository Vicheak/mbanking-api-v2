package com.vicheak.mbankingapi.api.authority;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Integer id;

    @Column(name = "auth_name", unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;

}