package com.spring.pricegenerator.DTO;

import java.util.List;

public class OrderDTO {
    private List<OrderItemDTO> orderItemDTOList;

    public OrderDTO(){}

    public OrderDTO(List<OrderItemDTO> orderItemDTOList) {
        this.orderItemDTOList = orderItemDTOList;
    }

    public List<OrderItemDTO> getOrderItemDTOList() {
        return orderItemDTOList;
    }

    public void setOrderItemDTOList(List<OrderItemDTO> orderItemDTOList) {
        this.orderItemDTOList = orderItemDTOList;
    }
}
