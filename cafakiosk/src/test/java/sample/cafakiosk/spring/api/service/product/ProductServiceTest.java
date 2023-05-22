package sample.cafakiosk.spring.api.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static sample.cafakiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafakiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafakiosk.spring.IntegrationTestSupport;
import sample.cafakiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafakiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafakiosk.spring.api.service.product.response.ProductResponse;
import sample.cafakiosk.spring.domain.product.Product;
import sample.cafakiosk.spring.domain.product.ProductRepository;
import sample.cafakiosk.spring.domain.product.ProductSellingStatus;
import sample.cafakiosk.spring.domain.product.ProductType;


class ProductServiceTest extends IntegrationTestSupport {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	// TestFixture를 위해 사용하는 것은 지양하자.
	@BeforeAll
	static void beforeAll() {
		// before class
	}

	// TestFixture를 위해 사용하는 것은 지양하자.
	@BeforeEach
	void setUp() {
		// before method

		/*
		 * 언제 사용해야 하는가?
		 * 각 테스트 입장에서 봤을 때: setUp() 내부의 내용을 아예 몰라도, 테스트 내용을 이해하는데 문제가 없는가?
		 * 수정해도 모든 테스트의 결과에 영향을 주지 않는가?
		 */
	}

	@AfterEach
	void tearDown() {
		productRepository.deleteAllInBatch();
	}

	// 테스트는 문서라는 관점에서, TestFixture는 given절에서 관리하는 것이 좋다.
	@DisplayName("신규 상품을 등록한다. 등록될 상품번호는 가장 최근 상품의 상품번호 + 1")
	@Test
	void createProduct() {
		// given
		Product product = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
		productRepository.save(product);

		ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
				.type(HANDMADE)
				.sellingStatus(SELLING)
				.name("카푸치노")
				.price(5000)
				.build();

		// when
		ProductResponse productResponse = productService.createProduct(request);

		// then
		assertThat(productResponse)
				.extracting("productNumber", "type", "sellingStatus", "name", "price")
				.contains("002", HANDMADE, SELLING, "카푸치노", 5000);

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(2)
				.extracting("productNumber", "type", "sellingStatus", "name", "price")
				.containsExactlyInAnyOrder(
						tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
						tuple("002", HANDMADE, SELLING, "카푸치노", 5000)
				);
	}

	@DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
	@Test
	void createProductWhenProductIsEmpty() {
		// given
		ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
				.type(HANDMADE)
				.sellingStatus(SELLING)
				.name("카푸치노")
				.price(5000)
				.build();

		// when
		ProductResponse productResponse = productService.createProduct(request);

		// then
		assertThat(productResponse)
				.extracting("productNumber", "type", "sellingStatus", "name", "price")
				.contains("001", HANDMADE, SELLING, "카푸치노", 5000);

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(1)
				.extracting("productNumber", "type", "sellingStatus", "name", "price")
				.containsExactlyInAnyOrder(tuple("001", HANDMADE, SELLING, "카푸치노", 5000));
	}

	// 테스트용 FactoryMethod에는 꼭 필요한 파라미터만 사용하자.
	// 여러 테스트 클래스에서 사용하기 위한 공통의 FactoryMethod 사용은 지양하자.
	private Product createProduct(String productNumber, ProductType type,
								  ProductSellingStatus sellingStatus, String name, int price) {
		return Product.builder()
				.productNumber(productNumber)
				.type(type)
				.sellingStatus(sellingStatus)
				.name(name)
				.price(price)
				.build();
	}
}