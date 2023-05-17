package sample.cafakiosk.spring.api;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class) // BindException <- valid 예외
	public ApiResponse<Object> bindException(BindException e) {
		// 첫 번째 바인딩 에러만 추출
		ObjectError data = e.getBindingResult().getAllErrors().get(0);

		// data.getDefaultMessage() <- @NotNull 등에서 정의 가능
		return ApiResponse.of(HttpStatus.BAD_REQUEST, data.getDefaultMessage(),
				null);
	}
}
