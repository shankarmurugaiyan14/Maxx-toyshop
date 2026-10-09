package com.maxxtoyshop.model;
import jakarta.persistence.*; import lombok.*; import java.math.BigDecimal; import java.time.LocalDateTime;
@Entity @Table(name="products") @Getter @Setter @NoArgsConstructor
public class Product {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String name; private String description;
 @Column(nullable=false,precision=10,scale=2) private BigDecimal price;
 @Column(nullable=false) private Integer stock=100;
 @Column(nullable=false) private String image;
 @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="category_id",nullable=false) private Category category;
 private String tag; private LocalDateTime createdAt;
 @PrePersist void created(){createdAt=LocalDateTime.now();}
}