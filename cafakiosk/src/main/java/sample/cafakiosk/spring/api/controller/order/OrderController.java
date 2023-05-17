package sample.cafakiosk.spring.api.controller.order;

import java.time.LocalDateTime;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafakiosk.spring.api.ApiResponse;
import sample.cafakiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafakiosk.spring.api.service.order.OrderService;
import sample.cafakiosk.spring.api.service.order.response.OrderResponse;

@RequiredArgsConstructor
@RestController
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/api/v1/orders/new")
	public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
		LocalDateTime registeredDateTime = LocalDateTime.now();
		return ApiResponse.ok(orderService.createOrder(request.toServiceRequest(), registeredDateTime));
	}
}
