package org.example.marketplacebackend.service;

import org.example.marketplacebackend.DTO.incoming.OrderItemDTO;
import org.example.marketplacebackend.model.OrderItem;
import org.example.marketplacebackend.model.Product;
import org.example.marketplacebackend.model.ProductOrder;
import org.example.marketplacebackend.repository.OrderHistoryRepository;
import org.example.marketplacebackend.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductOrderService {

  private final OrderHistoryRepository orderHistoryRepo;
  private final OrderRepository orderItemRepo;
  private final ProductService productService;

  public ProductOrderService(OrderHistoryRepository orderHistoryRepo,
      OrderRepository orderItemRepo, ProductService productService) {
    this.orderItemRepo = orderItemRepo;
    this.orderHistoryRepo = orderHistoryRepo;
    this.productService = productService;
  }

  public ProductOrder saveOrder(ProductOrder productOrder) {
    return orderHistoryRepo.save(productOrder);
  }

  public List<OrderItem> saveOrderItems(ProductOrder order, List<OrderItemDTO> orderItems) {
    List<OrderItem> orderItemsDb = new ArrayList<>();

    for (OrderItemDTO orderItemDTO : orderItems) {
      OrderItem orderItem = new OrderItem();
      Product product = productService.getProductOrNull(orderItemDTO.productId());
      orderItem.setProduct(product);
      orderItem.setOrder(order);
      orderItemsDb.add(orderItemRepo.save(orderItem));
    }

    return orderItemsDb;
  }

  public OrderItem getOrderItemOrNull(UUID orderId) {
    return orderItemRepo.findById(orderId).orElse(null);
  }

  public ProductOrder getOrderOrNull(UUID orderId) {
    return orderHistoryRepo.findById(orderId).orElse(null);
  }
}