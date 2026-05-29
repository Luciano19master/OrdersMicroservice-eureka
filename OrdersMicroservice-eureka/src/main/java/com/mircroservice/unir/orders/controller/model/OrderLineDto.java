package com.mircroservice.unir.orders.controller.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "order_id",
        "book_id",
        "quantity",
        "unit_price",
        "discount_pct",
        "line_total"
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineDto implements Serializable {

    private static final long serialVersionUID = 1234567890123456789L;

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("order_id")
    public Integer orderId;

    @JsonProperty("book_id")
    public Integer bookId;

    @JsonProperty("quantity")
    public Integer quantity;

    @JsonProperty("unit_price")
    public BigDecimal unitPrice;

    @JsonProperty("discount_pct")
    public BigDecimal discountPct;

    @JsonProperty("line_total")
    public BigDecimal lineTotal;
}
