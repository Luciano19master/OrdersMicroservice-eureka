package com.mircroservice.unir.orders.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_log")
@Getter
@Setter
@NoArgsConstructor
public class OrderStatusLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Order es requerido")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@NotBlank(message = "From status es requerido")
	@Column(nullable = false)
	private String fromStatus;

	@NotBlank(message = "To status es requerido")
	@Column(nullable = false)
	private String toStatus;

	@Column(name = "changed_at", nullable = false)
	private LocalDateTime changedAt;

	@Column(name = "changed_by")
	private String changedBy;
}

