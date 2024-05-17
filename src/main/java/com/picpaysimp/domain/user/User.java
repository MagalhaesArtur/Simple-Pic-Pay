package com.picpaysimp.domain.user;


import com.picpaysimp.dtos.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String document;

    @Column(unique = true)
    private String email;

    private String password;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(UserDTO user){
        setFirstName(user.firstName());
        setLastName(user.lastName());
        setBalance(user.balance());
        setUserType(user.type());
        setPassword(user.password());
        setDocument(user.document());
        setEmail(user.email());
    }
}
