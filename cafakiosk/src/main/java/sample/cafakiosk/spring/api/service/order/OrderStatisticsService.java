package sample.cafakiosk.spring.api.service.order;

import static sample.cafakiosk.spring.domain.order.OrderStatus.PAYMENT_COMPLETED;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafakiosk.spring.api.service.mail.MailService;
import sample.cafakiosk.spring.domain.order.Order;
import sample.cafakiosk.spring.domain.order.OrderRepository;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

	private final OrderRepository orderRepository;
	private final MailService mailService;

	public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
		// 해당 일자에 결제완료된 주문들을 가져온다.
		List<Order> orders = orderRepository.findOrdersBy(
				orderDate.atStartOfDay(), orderDate.plusDays(1).atStartOfDay(), PAYMENT_COMPLETED);

		// 총 매출 합계를 계산
		int totalAmount = orders.stream().mapToInt(Order::getTotalPrice)
				.sum();

		// 메일 전송
		boolean result = mailService.sendMail(
				"no-reply@cafaKiosk.com",
				email,
				String.format("[매출 통계] %s", orderDate),
				String.format("총 매출 합계는 %d원 입니다.", totalAmount));

		if (!result) {
			throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
		}

		return true;
	}
}
