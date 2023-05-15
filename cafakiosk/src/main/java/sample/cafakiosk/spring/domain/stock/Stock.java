package sample.cafakiosk.spring.domain.stock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafakiosk.spring.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productNumber;

	private int quantity;

	@Builder
	private Stock(String productNumber, int quantity) {
		this.productNumber = productNumber;
		this.quantity = quantity;
	}

	public static Stock create(String productNumber, int quantity) {
		return Stock.builder()
				.productNumber(productNumber)
				.quantity(quantity)
				.build();
	}
}
