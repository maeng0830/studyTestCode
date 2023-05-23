package sample.cafakiosk.spring.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {


	protected MockMvc mockMvc;

	protected ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp(//WebApplicationContext webApplicationContext,
			   RestDocumentationContextProvider provider) {

//		// webAppContextSetup <- 스프링 구동 필요
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//				.apply(documentationConfiguration(provider))
//				.build();

		// standaloneSetup <- 스프링 구동 불필요
		this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
				.apply(documentationConfiguration(provider))
				.build();
	}

	protected abstract Object initController();
}
