package be.uantwerpen.labplanner.Controller;

import be.uantwerpen.labplanner.Service.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    private Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "Errors/error-404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                logger.error("500 Error");
                return "Errors/error-500";
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                logger.error("403 Error");
                return "Errors/error-403";
            }
        }
        return "Errors/error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
