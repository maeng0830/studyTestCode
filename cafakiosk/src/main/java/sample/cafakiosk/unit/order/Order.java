package sample.cafakiosk.unit.order;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sample.cafakiosk.unit.beverage.Beverage;

@RequiredArgsConstructor
@Getter
public class Order {

	private final LocalDateTime orderDateTime;
	private final List<Beverage> beverages;
}
