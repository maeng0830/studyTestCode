package sample.cafakiosk.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafakiosk.unit.beverage.Americano;
import sample.cafakiosk.unit.beverage.Latte;
import sample.cafakiosk.unit.order.Order;

class CafeKioskTest {

	@Test
	void add_manuel_test() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		cafeKiosk.add(new Americano());

		System.out.println(">>> 담긴 음료 수: " + cafeKiosk.getBeverages().size());
		System.out.println(">>> 담긴 음료:" + cafeKiosk.getBeverages().get(0).getName());
		// 콘솔을 사람이 확인한다. 진정한 자동화 테스트일까?
		// 다른 사람이 보았을 때, 어떤 콘솔의 내용이 정답인지 한번에 파악할 수 있을까?
	}

//	@DisplayName("음료 1개 추가 테스트")
	@DisplayName("음료 1개를 추가하면 주문 목록에 담긴다")
	@Test
	void add() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		cafeKiosk.add(new Americano());

		assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
		assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@Test
	void addSeveralBeverages() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano, 2);

		assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
		assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);
	}

	@Test
	void addZeroBeverages() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
	}

	@Test
	void remove() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano);
		assertThat(cafeKiosk.getBeverages()).hasSize(1);

		cafeKiosk.remove(americano);
		assertThat(cafeKiosk.getBeverages()).hasSize(0);
	}

	@Test
	void clear() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		Latte latte = new Latte();

		cafeKiosk.add(americano);
		cafeKiosk.add(latte);

		assertThat(cafeKiosk.getBeverages()).hasSize(2);

		cafeKiosk.clear();
		assertThat(cafeKiosk.getBeverages()).hasSize(0);
	}

	@DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
	@Test
	void calculateTotalPrice() {
		//given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		Latte latte = new Latte();

		cafeKiosk.add(americano);
		cafeKiosk.add(latte);

		// when
		int totalPrice = cafeKiosk.calculateTotalPrice();

		// then
		assertThat(totalPrice).isEqualTo(8500);
	}

	@Test
	void createOrder() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano);

		Order order = cafeKiosk.createOrder();

		assertThat(order.getBeverages()).hasSize(1);
		assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@DisplayName("키오스크에서 주문을 생성할 수 있다")
	@Test
	void createOrderWithCurrentDateTime() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano);

		Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 5, 11, 10, 0));

		assertThat(order.getBeverages()).hasSize(1);
		assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@DisplayName("영업 시간 외에는 주문을 생성할 수 없다")
	@Test
	void createOrderOutsideOpenTime() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano);

		assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2023, 5, 11, 9, 0)))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");
	}
}