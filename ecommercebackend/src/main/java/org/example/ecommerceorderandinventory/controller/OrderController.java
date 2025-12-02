package org.example.ecommerceorderandinventory.controller;

import org.example.ecommerceorderandinventory.dto.in.ChangeOrderStatusDTO;
import org.example.ecommerceorderandinventory.dto.out.OrderDTO;
import org.example.ecommerceorderandinventory.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<OrderDTO> getOrder(@RequestParam long orderId) {
        OrderDTO orderDTO = orderService.getOrder(orderId);
        return ResponseEntity.ok().body(
                orderDTO
        );
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestParam long userId) {
        List<OrderDTO> orderDTO = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok().body(
                orderDTO
        );
    }


    /*
        This endpoint should be user by a shipping company for example, indicating the progress of a package.
     */
    @PatchMapping
    public ResponseEntity<Void> updateOrderStatus(@RequestBody ChangeOrderStatusDTO changeOrderStatusDTO) {
        orderService.updateOrderStatus(changeOrderStatusDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
