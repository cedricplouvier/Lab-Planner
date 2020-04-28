package be.uantwerpen.labplanner.Exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

   private Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure  (
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException, UsernameNotFoundException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> data = new HashMap<>();
        data.put(
                "TimeZone ",
                Calendar.getInstance().getTimeZone());
        data.put(
                "exception ",
                exception.getMessage());

        logger.error(HttpStatus.UNAUTHORIZED.toString()+"  => "+objectMapper.writeValueAsString(data));
        response.getOutputStream()
                .println(objectMapper.writeValueAsString(data));
        response.sendRedirect("/se/g3/login");
    }

}