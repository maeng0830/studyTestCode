package sample.cafakiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafakiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafakiosk.spring.domain.product.ProductType.BAKERY;
import static sample.cafakiosk.spring.domain.product.ProductType.BOTTLE;
import static sample.cafakiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafakiosk.spring.IntegrationTestSupport;
import sample.cafakiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafakiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafakiosk.spring.api.service.order.response.OrderResponse;
import sample.cafakiosk.spring.domain.order.OrderRepository;
import sample.cafakiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafakiosk.spring.domain.product.Product;
import sample.cafakiosk.spring.domain.product.ProductRepository;
import sample.cafakiosk.spring.domain.product.ProductType;
import sample.cafakiosk.spring.domain.stock.Stock;
import sample.cafakiosk.spring.domain.stock.StockRepository;

class OrderServiceTest extends IntegrationTestSupport {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private OrderService orderService;

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		stockRepository.deleteAllInBatch();
	}

	@DisplayName("주문번호 리스트를 받아 주문을 생성한다")
	@Test
	void createOrder() {
		// given
		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);

		productRepository.saveAll(List.of(product1, product2, product3));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of("001", "002"))
				.build();

		// when
		LocalDateTime registeredDateTime = LocalDateTime.now();
		OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
				.extracting("registeredDateTime", "totalPrice")
				.contains(registeredDateTime, 4000);
		assertThat(orderResponse.getProducts()).hasSize(2)
				.extracting("productNumber", "price")
				.containsExactlyInAnyOrder(
						tuple("001", 1000),
						tuple("002", 3000)
				);
	}

	@DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다")
	@Test
	void createOrderWithStock() {
		// given
		Product product1 = createProduct(BOTTLE, "001", 1000);
		Product product2 = createProduct(BAKERY, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));

		Stock stock1 = Stock.create("001", 2);
		Stock stock2 = Stock.create("002", 2);
		stockRepository.saveAll(List.of(stock1, stock2));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of("001", "001", "002", "003"))
				.build();

		LocalDateTime registeredDateTime = LocalDateTime.now();

		// when
		OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
				.extracting("registeredDateTime", "totalPrice")
				.contains(registeredDateTime, 10000);
		assertThat(orderResponse.getProducts()).hasSize(4)
				.extracting("productNumber", "price")
				.containsExactlyInAnyOrder(
						tuple("001", 1000),
						tuple("001", 1000),
						tuple("002", 3000),
						tuple("003", 5000)
				);

		List<Stock> stocks = stockRepository.findAll();
		assertThat(stocks).hasSize(2)
				.extracting("productNumber", "quantity")
				.containsExactlyInAnyOrder(
						tuple("001", 0),
						tuple("002", 1)
				);
	}

	/**
	 * 테스트 환경(given 절)의 독립성을 보장하자.
	 * 해당 테스트에서 실제 검증하고자 하는 것은 orderService.createOrder()
	 * 그러나 테스트 코드에는 다른 기능인 stock1.deductQuantity(1)이 사용되고 있다.
	 * 이런 경우 테스트 코드를 이해하기 위해 다른 기능에 대한 이해가 필요하며,
	 * 다른 기능에 문제가 발생할 경우 해당 테스트에도 문제가 발생한다.
	 * 즉 when, then에 대한 검증이 아닌 given에 대한 검증이 되는 것이다.
	 */
	@DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
	@Test
	void createOrderWithNoStock() {
		// given
		Product product1 = createProduct(BOTTLE, "001", 1000);
		Product product2 = createProduct(BAKERY, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));

		Stock stock1 = Stock.create("001", 2);
		Stock stock2 = Stock.create("002", 2);
		stock1.deductQuantity(1); // todo
		stockRepository.saveAll(List.of(stock1, stock2));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of("001", "001", "002", "003"))
				.build();

		LocalDateTime registeredDateTime = LocalDateTime.now();

		// when

		// then
		assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("재고가 부족한 상품이 있습니다.");
	}

	@DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
	@Test
	void createOrderWithDuplicateProductNumbers() {
		// given
		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);

		productRepository.saveAll(List.of(product1, product2, product3));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
				.productNumbers(List.of("001", "001"))
				.build();

		// when
		LocalDateTime registeredDateTime = LocalDateTime.now();
		OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
				.extracting("registeredDateTime", "totalPrice")
				.contains(registeredDateTime, 2000);
		assertThat(orderResponse.getProducts()).hasSize(2)
				.extracting("productNumber", "price")
				.containsExactlyInAnyOrder(
						tuple("001", 1000),
						tuple("001", 1000)
				);
	}

	private Product createProduct(ProductType type, String productNumber, int price) {
		return Product.builder()
				.type(type)
				.productNumber(productNumber)
				.price(price)
				.sellingStatus(SELLING)
				.name("메뉴 이름")
				.build();
	}
}