package sample.cafakiosk.spring.api.controller.product.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafakiosk.spring.domain.product.ProductSellingStatus;
import sample.cafakiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {


	@NotNull(message = "상품 타입은 필수 입니다.")
	private ProductType type;
	@NotNull(message = "상품 판매상태는 필수입니다.")
	private ProductSellingStatus sellingStatus;

	@NotBlank(message = "상품 이름은 필수입니다.") // "", " " => 통과 못함
//	@NotNull "", " " => 통과
//	@NotEmpty " " => 통과
	private String name;
	@Positive(message = "상품 가격은 양수여야 합니다.")
	private int price;

	@Builder
	private ProductCreateRequest(ProductType type,
								 ProductSellingStatus sellingStatus, String name, int price) {
		this.type = type;
		this.sellingStatus = sellingStatus;
		this.name = name;
		this.price = price;
	}

	public sample.cafakiosk.spring.api.service.product.request.ProductCreateServiceRequest toServiceRequest() {
		return sample.cafakiosk.spring.api.service.product.request.ProductCreateServiceRequest.builder()
				.type(type)
				.sellingStatus(sellingStatus)
				.name(name)
				.price(price)
				.build();
	}
}
