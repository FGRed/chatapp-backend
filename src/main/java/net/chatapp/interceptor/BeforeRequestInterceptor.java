package net.chatapp.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

public class BeforeRequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        System.out.println(requestURI);

        if(requestURI.equals("/session/session") || requestURI.equals("/session/in-registry/") || requestURI.equals("/session/current") || requestURI.equals("/cuser/")){
            return true;
        }

        Long lastAccessedTime = (request.getSession().getLastAccessedTime()/1000)/60;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastAccessedTime);
        calendar.add(Calendar.MINUTE, request.getSession().getMaxInactiveInterval());
        Long whenTimeOutShouldHappen = calendar.getTimeInMillis();
        calendar = Calendar.getInstance();
        if(calendar.getTimeInMillis() >= whenTimeOutShouldHappen && request.getSession().isNew()){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }
}
