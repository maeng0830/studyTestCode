package sample.cafakiosk.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import sample.cafakiosk.spring.client.mail.MailSendClient;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {
	// 테스트 클래스의 어노테이션, 클래스 내부에서 Mockito 관련 어노테이션의 사용 등이 달라지면 스프링을 재구동 한다(@Transactional은 제외).
	// 테스트 클래스들의 구동 환경 통합을 위한 클래스 => 전체 테스트 시 스프링 재구동 횟수를 최적화

	@MockBean
	protected MailSendClient mailSendClient;
}
