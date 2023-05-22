package sample.cafakiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static sample.cafakiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafakiosk.spring.domain.product.ProductType.BAKERY;
import static sample.cafakiosk.spring.domain.product.ProductType.BOTTLE;
import static sample.cafakiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import sample.cafakiosk.spring.IntegrationTestSupport;
import sample.cafakiosk.spring.client.mail.MailSendClient;
import sample.cafakiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafakiosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafakiosk.spring.domain.order.Order;
import sample.cafakiosk.spring.domain.order.OrderRepository;
import sample.cafakiosk.spring.domain.order.OrderStatus;
import sample.cafakiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafakiosk.spring.domain.product.Product;
import sample.cafakiosk.spring.domain.product.ProductRepository;
import sample.cafakiosk.spring.domain.product.ProductType;

class OrderStatisticsServiceTest extends IntegrationTestSupport {

	@Autowired
	private OrderStatisticsService orderStatisticsService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MailSendHistoryRepository mailSendHistoryRepository;

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		mailSendHistoryRepository.deleteAllInBatch();
	}

	@DisplayName("특정 날짜의 결제 완료 주문 총 금액을 메일로 발송한다.")
	@Test
	void sendOrderStatisticsMail() {
	    // given
		LocalDateTime now = LocalDateTime.of(2023, 5, 20, 0, 0);

		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 2000);
		Product product3 = createProduct(HANDMADE, "003", 3000);
		List<Product> products = List.of(product1, product2, product3);
		productRepository.saveAll(products);

		Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 5, 19, 23, 59, 59), products);
		Order order2 = createPaymentCompletedOrder(now, products);
		Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 5, 20, 23, 59, 59), products);
		Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 5, 21, 0, 0), products);

		Mockito.when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(
				String.class)))
				.thenReturn(true);

		// when
		boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 5, 20),
				"test@test.com");

		// then
		assertThat(result).isTrue();

		List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
		assertThat(histories).hasSize(1)
				.extracting("content")
				.contains("총 매출 합계는 12000원 입니다.");

	}

	private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
		Order order = Order.builder()
				.products(products)
				.orderStatus(OrderStatus.PAYMENT_COMPLETED)
				.registeredDateTime(now)
				.build();

		return orderRepository.save(order);
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