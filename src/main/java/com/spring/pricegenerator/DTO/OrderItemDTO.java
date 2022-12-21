package com.spring.pricegenerator.DTO;

public class OrderItemDTO {
    private Long quotationId;
    private Integer quantity;

    public OrderItemDTO(Long quotationId, Integer quantity) {
        this.quotationId = quotationId;
        this.quantity = quantity;
    }

    public Long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
