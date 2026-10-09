package com.maxxtoyshop.model;
import jakarta.persistence.*; import lombok.*; import java.time.LocalDateTime;
@Entity @Table(name="users") @Getter @Setter @NoArgsConstructor
public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String firstName;
 @Column(nullable=false) private String lastName;
 @Column(nullable=false,unique=true) private String email;
 @Column(nullable=false) private String password;
 private String phone;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role=Role.CUSTOMER;
 private LocalDateTime createdAt;
 @PrePersist void created(){createdAt=LocalDateTime.now();}
}