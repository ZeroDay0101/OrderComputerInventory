package org.example.ecommerceorderandinventory.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Qualifier
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotNull
    private String username;

    @NotNull
    private String password;


    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;


    private double balance;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

}