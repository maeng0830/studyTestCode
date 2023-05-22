package sample.cafakiosk.spring.domain.stock;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StockTest {

	/**
	 * 테스트 간 독립성을 보장하자
	 * 두 개 이상의 테스트가 공통된 자원을 사용하지 않도록 한다.
	 * 테스트 간에 영향을 미쳐서 올바른 검증이 불가능할 수 있다.
	 */
//	private static final Stock stock = Stock.create("001", 1);

	@DisplayName("재고 수량이 제공된 수량보다 작은지 확인한다.")
	@Test
	void isQuantityLessThan() {
	    // given
		Stock stock = Stock.create("001", 1);
		int quantity = 2;

	    // when
		boolean result = stock.isQuantityLessThan(quantity);

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("재고 수량을 제공된 수량만큼 차감할 수 있다.")
	@Test
	void deductQuantity() {
	    // given
		Stock stock = Stock.create("001", 1);
		int quantity = 1;

	    // when
		stock.deductQuantity(quantity);

	    // then
		assertThat(stock.getQuantity()).isEqualTo(0);
	}

	@DisplayName("재고 수량 보다 많은 수량을 차감하려는 경우, 예외가 발생한다.")
	@Test
	void deductQuantity2() {
		// given
		Stock stock = Stock.create("001", 1);
		int quantity = 2;

		// when

		// then
		assertThatThrownBy(() -> stock.deductQuantity(quantity))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("재고 수량이 부족합니다.");
	}
}