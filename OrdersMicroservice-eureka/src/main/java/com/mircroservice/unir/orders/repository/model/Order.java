package com.mircroservice.unir.orders.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String orderNumber;

	@NotNull(message = "Customer ID es requerido")
	@Column(nullable = false)
	private Long customerId;

	@NotBlank(message = "Status es requerido")
	@Column(nullable = false)
	private String status; // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "shipped_at")
	private LocalDateTime shippedAt;

	@Column(name = "delivered_at")
	private LocalDateTime deliveredAt;

	@Column(nullable = false)
	private String billingName;

	@Column
	private String billingNif;

	@Column
	private String billingAddress;

	@Column
	private String billingCity;

	@Column
	private String billingZip;

	@Column
	private String billingCountry;

	@Column(precision = 10, scale = 2)
	private BigDecimal subtotal = BigDecimal.ZERO;

	@Column(precision = 10, scale = 2)
	private BigDecimal discount = BigDecimal.ZERO;

	@Column(precision = 5, scale = 2)
	private BigDecimal taxRate = BigDecimal.ZERO;

	@Column(precision = 10, scale = 2)
	private BigDecimal taxAmount = BigDecimal.ZERO;

	@Column(precision = 10, scale = 2)
	private BigDecimal total = BigDecimal.ZERO;

	@Column(columnDefinition = "TEXT")
	private String notes;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderLine> lines = new ArrayList<>();

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderStatusLog> statusLog = new ArrayList<>();

	public void addOrderLine(OrderLine line) {
		lines.add(line);
		line.setOrder(this);
	}

	public void removeOrderLine(OrderLine line) {
		lines.remove(line);
		line.setOrder(null);
	}

	public void addStatusLog(OrderStatusLog log) {
		statusLog.add(log);
		log.setOrder(this);
	}
}
