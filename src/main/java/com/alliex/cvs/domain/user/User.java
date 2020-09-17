package com.alliex.cvs.domain.user;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.point.PointHistory;
import com.alliex.cvs.domain.settle.Settle;
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
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Transaction> transactionId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<PointHistory> pointHistoryId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private  Collection<Settle> settleId;

    @Builder
    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
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