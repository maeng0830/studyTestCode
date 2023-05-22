package sample.cafakiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafakiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static sample.cafakiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafakiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;
import static sample.cafakiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafakiosk.spring.IntegrationTestSupport;


@Transactional
//@SpringBootTest // 테스트 시 스프링 구동. 모든 빈을 사용할 수 있음
// @DataJpaTest // 테스트 시 스프링 구동. JPA 관련 빈을 사용할 수 있음, @Transactionl 적용
class ProductRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private ProductRepository productRepository;

	@DisplayName("원하는 판매상태를 가진 상품을 조회한다")
	@Test
	void findAllBySellingStatusIn() {
		// given
		Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
		Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
		Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);

		productRepository.saveAll(List.of(product1, product2, product3));

		// when
		List<Product> products = productRepository.findAllBySellingStatusIn(
				List.of(SELLING, HOLD));

		// then
		assertThat(products).hasSize(2)
				.extracting("productNumber", "name", "sellingStatus")
				.containsExactlyInAnyOrder(
						tuple("001", "아메리카노", SELLING),
						tuple("002", "카페라떼", HOLD));
	}

	@DisplayName("상품번호 리스트로 상품들을 조회한다")
	@Test
	void findAllByProductNumberIn() {
		// given
		Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
		Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
		Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);

		productRepository.saveAll(List.of(product1, product2, product3));

		// when
		List<Product> products = productRepository.findAllByProductNumberIn(
				List.of("001", "002"));

		// then
		assertThat(products).hasSize(2)
				.extracting("productNumber", "name", "sellingStatus")
				.containsExactlyInAnyOrder(
						tuple("001", "아메리카노", SELLING),
						tuple("002", "카페라떼", HOLD));
	}

	@DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
	@Test
	void findLatestProductNumber() {
		// given
		String targetProductNumber = "003";

		Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
		Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
		Product product3 = createProduct(targetProductNumber, HANDMADE, STOP_SELLING, "팥빙수", 7000);

		productRepository.saveAll(List.of(product1, product2, product3));

		// when
		String result = productRepository.findLatestProductNumber();

		// then
		assertThat(result).isEqualTo(targetProductNumber);
	}

	@DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때 상품이 하나도 없으면 null을 반환한다.")
	@Test
	void findLatestProductNumberWhenProductIsEmpty() {
		// given

		// when
		String result = productRepository.findLatestProductNumber();

		// then
		assertThat(result).isNull();
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