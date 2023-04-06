package com.david.infrastructure.persistence.db;

import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

/**
 * Created by david on 07-03-2023
 */

@Entity
@Table(name = "dispenser")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class DispenserDb {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @NonNull
  private double flowVolume;

  @OneToMany(mappedBy = "dispenser")
  @ToString.Exclude
  private List<DispenserSpendingLineDb>  dispenserSpendingLineDbs;

}
