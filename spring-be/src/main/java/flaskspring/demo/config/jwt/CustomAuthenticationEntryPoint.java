package flaskspring.demo.config.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static flaskspring.demo.config.auth.AuthConstant.TOKEN_EXPIRED_MESSAGE;
import static flaskspring.demo.config.auth.AuthConstant.TOKEN_NOT_VALID_MESSAGE;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /*
    이 곳은 인증이 필요한 api에 접근 시 인증에 실패할 경우 이곳으로 진입하여 Response객체를 정의한다.
     토큰이 없거나, 만료되었거나, 유효하지 않거나 셋 중에 하나이다.
     @ControllerAdvice에서 에러를 처리하는 건 서블릿 대상이다.
     필터의 에러는 이 곳에서 처리할 수 있다. 필터에서 에러가 발생한 경우
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute(JwtProperties.HEADER_STRING);
        String errorCode;

        if (exception == null) { //토큰이 없고, 인증 못해서 에러 발생한 경우
            setErrorResponse(response, BaseResponseCode.NO_TOKEN_FOUND);
            return;
        }

        if(exception.equals(TOKEN_EXPIRED_MESSAGE)) {
            setErrorResponse(response, BaseResponseCode.TOKEN_EXPIRED);
            return;
        }

        if (exception.equals(TOKEN_NOT_VALID_MESSAGE)) { //토큰이 있지만, 로그인을(인증) 하지 못한 경우
            setErrorResponse(response, BaseResponseCode.NOT_VALID_TOKEN);
            return;
        }
    }

    private void setErrorResponse( // 여기서 에러의 경우 반환 데이터를 정의
                                   HttpServletResponse response,
                                   BaseResponseCode errorCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getCode().value());
        response.setContentType("application/json;charset=UTF-8");
        BaseExceptionResponse baseExceptionResponse = new BaseExceptionResponse(errorCode.getCode().value(), errorCode.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(baseExceptionResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
