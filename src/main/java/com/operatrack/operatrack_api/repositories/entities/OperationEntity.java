package com.operatrack.operatrack_api.repositories.entities;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "operations")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(name = "share_quantity", nullable = false)
    private Integer shareQuantity;

    @Column(name = "purchase_price", nullable = false)
    private Double purchasePrice;

    @Column(name = "total_value", nullable = false)
    private Double totalValue;

    @Column(name = "capital_gain")
    private Double capitalGain;

    @Column(name = "purchase_tax")
    private Double purchaseTax;

    @Column(name = "sale_tax")
    private Double saleTax;

    @Column(name = "total_tax")
    private Double totalTax;

    @Column(name = "net_earnings")
    private Double netEarnings;

    @Column(name = "purchase_date", nullable = false)
    private Instant purchaseDate;

    @Column(name = "sale_date")
    private Instant saleDate;

    @Column(name = "stock_id", nullable = false)
    private String stockId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
