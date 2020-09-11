package com.alliex.cvs.domain.user;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.point.PointHistory;
import com.alliex.cvs.domain.transaction.Transaction;
import com.alliex.cvs.domain.type.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String fullName;

    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Transaction> transactionId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<PointHistory> userId;

    @Builder
    public User(String username, String password, String department, String fullName, String email, String phoneNumber, Role role) {
        this.username = username;
        this.password = password;
        this.department = department;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public User update(String username, String password) {
        this.username = username;
        this.password = password;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

}
