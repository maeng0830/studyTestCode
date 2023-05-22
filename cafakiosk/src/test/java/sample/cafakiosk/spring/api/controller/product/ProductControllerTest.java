package sample.cafakiosk.spring.api.controller.product;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafakiosk.spring.ControllerTestSupport;
import sample.cafakiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafakiosk.spring.api.service.product.response.ProductResponse;
import sample.cafakiosk.spring.domain.product.ProductSellingStatus;
import sample.cafakiosk.spring.domain.product.ProductType;

class ProductControllerTest extends ControllerTestSupport {

	@DisplayName("신규 상품을 등록한다.")
	@Test
	void createProduct() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
				.type(ProductType.HANDMADE)
				.sellingStatus(ProductSellingStatus.SELLING)
				.name("아메리카노")
				.price(4000)
				.build();
		// when

		// then
		mockMvc.perform(post("/api/v1/products/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk());
	}

	@DisplayName("신규 상품을 등록할 때 상품타입은 필수다.")
	@Test
	void createProductWithoutType() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
				.sellingStatus(ProductSellingStatus.SELLING)
				.name("아메리카노")
				.price(4000)
				.build();
		// when

		// then
		mockMvc.perform(post("/api/v1/products/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("400"))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("상품 타입은 필수 입니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("신규 상품을 등록할 때 상품상태는 필수다.")
	@Test
	void createProductWithoutStatus() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
				.type(ProductType.HANDMADE)
				.name("아메리카노")
				.price(4000)
				.build();
		// when

		// then
		mockMvc.perform(post("/api/v1/products/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("400"))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("신규 상품을 등록할 때 상품 이름은 필수다.")
	@Test
	void createProductWithoutName() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
				.type(ProductType.HANDMADE)
				.sellingStatus(ProductSellingStatus.SELLING)
				.price(4000)
				.build();
		// when

		// then
		mockMvc.perform(post("/api/v1/products/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("400"))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("신규 상품을 등록할 때 상품 가격은 양수여야한다.")
	@Test
	void createProductPriceIsNotPositive() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
				.type(ProductType.HANDMADE)
				.sellingStatus(ProductSellingStatus.SELLING)
				.name("아메리카노")
				.price(0)
				.build();
		// when

		// then
		mockMvc.perform(post("/api/v1/products/new")
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("400"))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("판매 상품을 조회한다.")
	@Test
	void getSellingProducts() throws Exception {
		// given
		List<ProductResponse> result = List.of();
		when(productService.getSellingProducts()).thenReturn(result);
		// when

		// then
		mockMvc.perform(get("/api/v1/products/selling"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(jsonPath("$.status").value("OK"))
				.andExpect(jsonPath("$.message").value("OK"))
				.andExpect(jsonPath("$.data").isArray());
	}
}