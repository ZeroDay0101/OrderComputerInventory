package org.example.ecommerceorderandinventory.unit.controller;


import org.example.ecommerceorderandinventory.controller.OrderController;
import org.example.ecommerceorderandinventory.dto.in.ChangeOrderStatusDTO;
import org.example.ecommerceorderandinventory.dto.out.OrderDTO;
import org.example.ecommerceorderandinventory.entity.order.OrderStatus;
import org.example.ecommerceorderandinventory.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {OrderController.class})
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    private MockMvc mockMvc;
    @MockitoBean
    private OrderService orderService;

    @BeforeEach
    void setup() {
        orderService = mock(OrderService.class);
        OrderController controller = new OrderController(orderService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getOrder_success() throws Exception {
        OrderDTO expected = new OrderDTO(1L, 10L, 1, 1, OrderStatus.ONGOING, "Poznańska", "1", "Poznań", "32-111", "POLAND");

        when(orderService.getOrder(1L)).thenReturn(expected);

        mockMvc.perform(get("/api/order").param("orderId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.status").value("ONGOING"));

        verify(orderService).getOrder(1L);
    }

    @Test
    void getUserOrders_success() throws Exception {
        List<OrderDTO> orders = List.of(
                new OrderDTO(1L, 10L, 1, 1, OrderStatus.ONGOING, "Poznańska", "1", "Poznań", "32-111", "POLAND"),
                new OrderDTO(2L, 10L, 1, 1, OrderStatus.ONGOING, "Gdańska", "3", "Poznań", "32-111", "POLAND")
        );
        when(orderService.getOrdersByUserId(1L)).thenReturn(orders);

        mockMvc.perform(get("/api/order/user").param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].transactionId").value(1))
                .andExpect(jsonPath("$[1].transactionId").value(2));

        verify(orderService).getOrdersByUserId(1L);
    }

    @Test
    void updateOrderStatus_success() throws Exception {
        String json = """
                {
                  "orderId": 5,
                  "orderStatus": "COMPLETED"
                }
                """;

        mockMvc.perform(
                        patch("/api/order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk());

        ArgumentCaptor<ChangeOrderStatusDTO> captor =
                ArgumentCaptor.forClass(ChangeOrderStatusDTO.class);
        verify(orderService).updateOrderStatus(captor.capture());

        ChangeOrderStatusDTO dto = captor.getValue();
        org.junit.jupiter.api.Assertions.assertEquals(5L, dto.getOrderId());
        org.junit.jupiter.api.Assertions.assertEquals(OrderStatus.COMPLETED, dto.getOrderStatus());
    }
}
