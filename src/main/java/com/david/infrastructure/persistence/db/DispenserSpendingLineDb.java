package com.david.infrastructure.persistence.db;

import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by david on 07-03-2023
 */

@Entity
@Table(name = "dispenser_spending_line")
@Setter
@Getter
@ToString
public class DispenserSpendingLineDb {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  private String status;

  @Column(columnDefinition = "timestamp with time zone")
  private ZonedDateTime openedAt;

  @Column(columnDefinition = "timestamp with time zone")
  private ZonedDateTime closedAt;

  @Column(columnDefinition = "timestamp with time zone")
  private ZonedDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "dispenser_id")
  private DispenserDb dispenser;

}
