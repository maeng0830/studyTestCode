package sample.cafakiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafakiosk.spring.client.mail.MailSendClient;
import sample.cafakiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafakiosk.spring.domain.history.mail.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

	@InjectMocks
	private MailService mailService; // @ExtendWith(MockitoExtension.class) 필요

//	@Spy // 실제 객체에 기반하여 동작, 일부만 stubbing 해줄 수 있다.
	@Mock
	private MailSendClient mailSendClient; // @ExtendWith(MockitoExtension.class) 필요

	@Mock // 가짜 객체에 기반하여 동작
	private MailSendHistoryRepository mailSendHistoryRepository; // @ExtendWith(MockitoExtension.class) 필요

	@DisplayName("메일 전송 테스트")
	@Test
	void sendMail() {
	    // given
//		MailSendClient mailSendClient = mock(MailSendClient.class);
//		MailSendHistoryRepository mailSendHistoryRepository = mock(MailSendHistoryRepository.class);
//		MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

		// @Mock일 때 stubbing
//		when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
//				.thenReturn(true);

		// @Mock일 때 stubbing, BDDMockito
		given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
				.willReturn(true);

		// @Spy일 때 stubbing, 특정 부분만 stubbing해 줄 수 있다.
//		doReturn(true)
//				.when(mailSendClient)
//				.sendEmail(anyString(), anyString(), anyString(), anyString());

		// when
		boolean result = mailService.sendMail("", "", "", "");

		// then
		assertThat(result).isTrue();
		verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
	}
}