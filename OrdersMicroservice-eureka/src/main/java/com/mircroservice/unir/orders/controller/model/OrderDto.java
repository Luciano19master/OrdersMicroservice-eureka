package com.mircroservice.unir.orders.controller.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
        "order_number",
        "customer_id",
        "status",
        "created_at",
        "updated_at",
        "shipped_at",
        "delivered_at",
        "billing_name",
        "billing_nif",
        "billing_address",
        "billing_city",
        "billing_zip",
        "billing_country",
        "subtotal",
        "discount",
        "tax_rate",
        "tax_amount",
        "total",
        "notes",
        "lines",
        "status_log"
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements Serializable {

    private static final long serialVersionUID = 3210987654321098765L;

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("order_number")
    public String orderNumber;

    @JsonProperty("customer_id")
    public Integer customerId;

    @JsonProperty("status")
    public String status;

    @JsonProperty("created_at")
    public LocalDateTime createdAt;

    @JsonProperty("updated_at")
    public LocalDateTime updatedAt;

    @JsonProperty("shipped_at")
    public LocalDateTime shippedAt;

    @JsonProperty("delivered_at")
    public LocalDateTime deliveredAt;

    @JsonProperty("billing_name")
    public String billingName;

    @JsonProperty("billing_nif")
    public String billingNif;

    @JsonProperty("billing_address")
    public String billingAddress;

    @JsonProperty("billing_city")
    public String billingCity;

    @JsonProperty("billing_zip")
    public String billingZip;

    @JsonProperty("billing_country")
    public String billingCountry;

    @JsonProperty("subtotal")
    public BigDecimal subtotal;

    @JsonProperty("discount")
    public BigDecimal discount;

    @JsonProperty("tax_rate")
    public BigDecimal taxRate;

    @JsonProperty("tax_amount")
    public BigDecimal taxAmount;

    @JsonProperty("total")
    public BigDecimal total;

    @JsonProperty("notes")
    public String notes;

    @JsonProperty("lines")
    public List<OrderLineDto> lines;

    @JsonProperty("status_log")
    public List<OrderStatusLogDto> statusLog;
}
