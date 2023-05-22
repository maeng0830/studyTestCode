package sample.cafakiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafakiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafakiosk.spring.api.service.product.response.ProductResponse;
import sample.cafakiosk.spring.domain.product.Product;
import sample.cafakiosk.spring.domain.product.ProductRepository;
import sample.cafakiosk.spring.domain.product.ProductSellingStatus;

/**
 * readOnly = true : 읽기 전용
 * CRUD에서 CUD 동작 X / only read
 * JPA: CUD 스냅샷 저장, 변경감지 X <- 성능 향상
 *
 * CQRS - Command(CUD)와 Query(R)를 분리 <- 가장 기본적인 방법 @Transactional(readOnly = true)
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductNumberFactory productNumberFactory;

	@Transactional
	public ProductResponse createProduct(ProductCreateServiceRequest request) {
		// create nextProductNumber
		String nextProductNumber = productNumberFactory.createNextProductNumber();

		Product product = request.toEntity(nextProductNumber);
		Product savedProduct = productRepository.save(product);

		return ProductResponse.of(savedProduct);
	}

	public List<ProductResponse> getSellingProducts() {
		List<Product> products = productRepository.findAllBySellingStatusIn(
				ProductSellingStatus.forDisplay());

		return products.stream()
				.map(ProductResponse::of)
				.collect(Collectors.toList());
	}
}
