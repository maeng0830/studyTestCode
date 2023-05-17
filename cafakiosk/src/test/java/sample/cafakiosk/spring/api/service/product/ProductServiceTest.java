package sample.cafakiosk.spring.api.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static sample.cafakiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafakiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafakiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafakiosk.spring.api.service.product.response.ProductResponse;
import sample.cafakiosk.spring.domain.product.Product;
import sample.cafakiosk.spring.domain.product.ProductRepository;
import sample.cafakiosk.spring.domain.product.ProductSellingStatus;
import sample.cafakiosk.spring.domain.product.ProductType;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@AfterEach
	void tearDown() {
		productRepository.deleteAllInBatch();
	}

	@DisplayName("신규 상품을 등록한다. 등록될 상품번호는 가장 최근 상품의 상품번호 + 1")
	@Test
	void createProduct() {
		// given
		Product product = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
		productRepository.save(product);

		ProductCreateRequest request = ProductCreateRequest.builder()
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
		ProductCreateRequest request = ProductCreateRequest.builder()
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