package com.spring.pricegenerator.service;

import com.spring.pricegenerator.DTO.OrderDTO;
import com.spring.pricegenerator.DTO.OrderItemDTO;
import com.spring.pricegenerator.model.Order;
import com.spring.pricegenerator.model.Product;
import com.spring.pricegenerator.model.Quotation;
import com.spring.pricegenerator.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private QuotationService quotationService;

    private UserService userService;

    @Autowired
    public OrderService(UserService userService, OrderRepository orderRepository, QuotationService quotationService) {
        this.orderRepository = orderRepository;
        this.quotationService = quotationService;
        this.userService = userService;
    }

    public Order addOrder(OrderDTO orderDTO) {
        List<Quotation> availableQuotationList = quotationService.getActiveQuotation();
        List<Integer> numberOfProducts = new ArrayList<>();
        Order newOrder = new Order();
        newOrder.setCreatedDate(LocalDateTime.now());
        newOrder.setUser(userService.findLoggedInUser());
        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItemDTOList()) {
            //verific daca cotatiile pt care urmeaza sa se faca comanda mai sunt inca active
            int positionOfQuotationInActiveQuotationList = isAvailableQuotation(orderItemDTO, availableQuotationList);
            if (positionOfQuotationInActiveQuotationList == -1) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the quotation is not more available");
            } else {
                newOrder.getQuotationList().add(availableQuotationList.get(positionOfQuotationInActiveQuotationList));
                //setam numarul de produse (practic in newOrder.getQuotationList la poz x retinem produsul iar in numbersOfProducts
                // tot la poz x vom retine numarul de produse)
                numberOfProducts.add(orderItemDTO.getQuantity());
                //reduc numarul de produse disponibile in baza de date cu nr de produse comandate
                Product currentProduct = availableQuotationList.get(positionOfQuotationInActiveQuotationList).getProduct();
                if (currentProduct.getProductQuantity() - orderItemDTO.getQuantity() <= 0) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there are not enough products in stock");
                }
                currentProduct.setProductQuantity(currentProduct.getProductQuantity() - orderItemDTO.getQuantity());
            }
        }
        //calculam reducerea in functie de numarul de produse
        Double discountByNumberOfProducts = getDiscountByNumberOfProducts(numberOfProducts);
        Double totalDiscount = getTotalDiscount(newOrder.getQuotationList(), numberOfProducts, discountByNumberOfProducts);
        newOrder.setTotalPrice(getTotalPrice(newOrder.getQuotationList(), numberOfProducts, totalDiscount));
        return orderRepository.save(newOrder);
    }


    public int isAvailableQuotation(OrderItemDTO orderItemDTO, List<Quotation> availableQuotationList) {
        for (int i = 0; i < availableQuotationList.size(); i++) {
            if (availableQuotationList.get(i).getId() == orderItemDTO.getQuotationId()) {
                return i;
            }
        }
        return -1;
    }

    public Double getDiscountByNumberOfProducts(List<Integer> numberOfProducts) {
        if ((numberOfProducts.size() >= 3) && (numberOfProducts.size() <= 10)) {
            return numberOfProducts.size() * 0.5;
        }
        return 0.0;
    }

    public Double getTotalDiscount(List<Quotation> quotationList, List<Integer> numberOfProducts, Double discountByNumberOfProducts) {
        Double totalDiscount = 0.0;
        for (int i = 0; i < quotationList.size(); i++) {
            //aplicam reducerea in functie de numarul de produse pentru maxim 10 produse (ne vom referi la 10 tipuri de produse sau 10 produse care pot fi de acelasi fel??)
            if (i < 10) {
                totalDiscount += numberOfProducts.get(i) * (quotationList.get(i).getAgeDiscount() + quotationList.get(i).getCountryDiscount() + quotationList.get(i).getProduct().getProductPrice() * discountByNumberOfProducts);
            } else {
                totalDiscount += numberOfProducts.get(i) * (quotationList.get(i).getAgeDiscount() + quotationList.get(i).getCountryDiscount());
            }
        }
        return totalDiscount;
    }

    public Double getTotalPrice(List<Quotation> quotationList, List<Integer> numberOfProducts, Double totalDiscount) {
        Double totalPrice = 0.0;
        for (int i = 0; i < quotationList.size(); i++) {
            //calculam pretul total fara reducere
            totalPrice += quotationList.get(i).getProduct().getProductPrice() * numberOfProducts.get(i);
        }
        //returnam pretul rezultat dupa aplicarea reducerii
        return totalPrice - totalDiscount;
    }

}
