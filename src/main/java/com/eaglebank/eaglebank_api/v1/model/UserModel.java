package com.eaglebank.eaglebank_api.v1.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    @Embedded
    private Address address;
    private String phoneNumber;
    @CreationTimestamp
    private LocalDateTime createdTimestamp;
    @UpdateTimestamp
    private LocalDateTime updatedTimestamp;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AccountModel> accounts;
}
