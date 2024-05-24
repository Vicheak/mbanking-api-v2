package com.vicheak.mbankingapi.api.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_uuid", unique = true, nullable = false)
    private String uuid;

    @Column(name = "user_username", unique = true, nullable = false)
    private String username;

    @Column(name = "user_email", unique = true, nullable = false)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_gender", nullable = false)
    private String gender;

    @Column(name = "user_phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "user_one_signal_id", unique = true)
    private String oneSignalId;

    @Column(name = "user_is_student")
    private Boolean isStudent;

    @Column(name = "user_student_card_no", unique = true)
    private String studentCardNo;

    @Column(name = "user_is_verified", nullable = false)
    private Boolean isVerified;

    @Column(name = "user_verified_code")
    private String verifiedCode;

    @Column(name = "user_is_deleted", nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<UserRole> userRoles;

}
