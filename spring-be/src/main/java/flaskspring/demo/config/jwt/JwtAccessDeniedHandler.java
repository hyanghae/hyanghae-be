package flaskspring.demo.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import flaskspring.demo.exception.BaseExceptionResponse;
import flaskspring.demo.exception.BaseResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        //필요한 권한이 없이 접근하려 할때 403
        setErrorResponse(response, BaseResponseCode.FORBIDDEN);
    }

    private void setErrorResponse( // 여기서 에러의 경우 반환 데이터를 정의
                                   HttpServletResponse response,
                                   BaseResponseCode baseResponse
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(baseResponse.getCode().getStatusCode());
        response.setContentType("application/json;charset=UTF-8");
        BaseExceptionResponse errorResponse = new BaseExceptionResponse(baseResponse.getCode().getStatusCode(),baseResponse.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}