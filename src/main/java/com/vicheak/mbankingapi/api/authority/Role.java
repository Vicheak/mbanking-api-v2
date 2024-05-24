package com.vicheak.mbankingapi.api.authority;

import com.vicheak.mbankingapi.api.user.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role_name", unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Authority> authorities;

    @OneToMany(mappedBy = "role")
    private List<UserRole> userRoles;

}
