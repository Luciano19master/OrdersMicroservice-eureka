package com.mircroservice.unir.orders.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "order_lines")
@Getter
@Setter
@NoArgsConstructor
public class OrderLine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Order es requerido")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@NotNull(message = "Book ID es requerido")
	@Column(nullable = false)
	private Long bookId;

	@NotNull(message = "Quantity es requerido")
	@Min(value = 1, message = "La cantidad debe ser mayor a 0")
	@Column(nullable = false)
	private Integer quantity;

	@NotNull(message = "Unit price es requerido")
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal unitPrice;

	@Column(precision = 5, scale = 2)
	private BigDecimal discountPct = BigDecimal.ZERO;

	@Column(precision = 10, scale = 2)
	private BigDecimal lineTotal = BigDecimal.ZERO;

	public void calculateLineTotal() {
		if (unitPrice != null && quantity != null) {
			BigDecimal total = unitPrice.multiply(new BigDecimal(quantity));
			if (discountPct != null && discountPct.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal discount = total.multiply(discountPct).divide(new BigDecimal(100));
				this.lineTotal = total.subtract(discount);
			} else {
				this.lineTotal = total;
			}
		}
	}
}
