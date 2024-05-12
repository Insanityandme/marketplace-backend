package org.example.marketplacebackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode @ToString
@Entity
@Table(name = "product")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_category")
  @JsonBackReference
  private ProductCategory productCategory;

  private Integer price;

  private Integer condition;

  private Boolean isPurchased;

  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller")
  @JsonBackReference
  private Account seller;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer")
  @JsonBackReference
  private Account buyer;

  private Integer color;

  private Integer productionYear;

  @Column(insertable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<ProductImage> productImages;
}
