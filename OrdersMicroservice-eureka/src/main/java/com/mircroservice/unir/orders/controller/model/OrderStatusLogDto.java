package com.mircroservice.unir.orders.controller.model;

import java.io.Serializable;
import java.time.LocalDateTime;

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
        "from_status",
        "to_status",
        "changed_at",
        "changed_by"
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusLogDto implements Serializable {

    private static final long serialVersionUID = 1245305219942230398L;

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("order_id")
    public Integer orderId;

    @JsonProperty("from_status")
    public String fromStatus;

    @JsonProperty("to_status")
    public String toStatus;

    @JsonProperty("changed_at")
    public LocalDateTime changedAt;

    @JsonProperty("changed_by")
    public String changedBy;
}
