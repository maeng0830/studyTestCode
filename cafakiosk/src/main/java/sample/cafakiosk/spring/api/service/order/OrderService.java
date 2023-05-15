package sample.cafakiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafakiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafakiosk.spring.api.service.order.response.OrderResponse;
import sample.cafakiosk.spring.domain.order.Order;
import sample.cafakiosk.spring.domain.order.OrderRepository;
import sample.cafakiosk.spring.domain.product.Product;
import sample.cafakiosk.spring.domain.product.ProductRepository;

@RequiredArgsConstructor
@Service
public class OrderService {

	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
		List<String> productNumbers = request.getProductNumbers();

		// Product
		List<Product> products = findProductsBy(
				productNumbers);

		// Order
		Order order = Order.create(products, registeredDateTime);
		Order savedOrder = orderRepository.save(order);

		return OrderResponse.of(savedOrder);
	}

	private List<Product> findProductsBy(List<String> productNumbers) {
		List<Product> products = productRepository.findAllByProductNumberIn(
				productNumbers);

		Map<String, Product> productMap = products.stream()
				.collect(Collectors.toMap(Product::getProductNumber, p -> p));

		List<Product> duplicateProducts = productNumbers.stream()
				.map(productMap::get)
				.collect(Collectors.toList());
		return duplicateProducts;
	}
}
