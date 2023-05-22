package sample.cafakiosk.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafakiosk.spring.api.controller.order.OrderController;
import sample.cafakiosk.spring.api.controller.product.ProductController;
import sample.cafakiosk.spring.api.service.order.OrderService;
import sample.cafakiosk.spring.api.service.product.ProductService;

@WebMvcTest(controllers = {
		OrderController.class,
		ProductController.class}) // controller 관련 빈들을 사용하기 위한 애노테이션
public abstract class ControllerTestSupport {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper; // json <-> Object (직렬화, 역직렬화)

	@MockBean
	protected OrderService orderService;

	@MockBean //
	protected ProductService productService;
}
