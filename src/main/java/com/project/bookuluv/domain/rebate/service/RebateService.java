package com.project.bookuluv.domain.rebate.service;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.order.domain.OrderItem;
import com.project.bookuluv.domain.order.repository.OrderItemRepository;
import com.project.bookuluv.domain.rebate.domain.RebateOrderItem;
import com.project.bookuluv.domain.rebate.repository.RebateItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RebateService {

    private final OrderItemRepository orderItemRepository;

    private final RebateItemRepository rebateItemRepository;

    public long rebateForMonth() {

        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        List<OrderItem> orderItems = orderItemRepository.findByIsPaidTrueAndPayDateBetween(
                firstDayOfMonth.atStartOfDay(),
                lastDayOfMonth.atTime(LocalTime.MAX)
        );

        long totalPaidAmount = 0;

        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            if (product != null) {
                totalPaidAmount += product.getPriceSales();
            }
        }

        return totalPaidAmount;
    }


    public Map<Product, Integer> getProductSalesForMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        List<OrderItem> orderItems = orderItemRepository.findByIsPaidTrueAndPayDateBetween(
                firstDayOfMonth.atStartOfDay(),
                lastDayOfMonth.atTime(LocalTime.MAX)
        );

        Map<Product, Integer> productSales = new HashMap<>();

        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            if (product != null) {
                productSales.put(product, productSales.getOrDefault(product, 0) + 1);
            }
        }

        return productSales;
    }

    public Map<Member, List<OrderItem>> getMemberPurchaseForMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        List<OrderItem> orderItems = orderItemRepository.findByIsPaidTrueAndPayDateBetween(
                firstDayOfMonth.atStartOfDay(),
                lastDayOfMonth.atTime(LocalTime.MAX)
        );


        Map<Member, List<OrderItem>> memberPurchase = new HashMap<>();

        for (OrderItem orderItem : orderItems) {
            Member buyer = orderItem.getOrder().getBuyer();
            if (buyer != null) {
                memberPurchase.computeIfAbsent(buyer, k -> new ArrayList<>()).add(orderItem);
            }
        }

        return memberPurchase;
    }

}