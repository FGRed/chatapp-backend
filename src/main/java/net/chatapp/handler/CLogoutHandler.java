package net.chatapp.handler;

import net.chatapp.model.cuser.CUser;
import net.chatapp.service.cuser.CUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CLogoutHandler implements LogoutHandler {

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private CUserService cUserService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        CUser cUser = (CUser) authentication.getPrincipal();
        cUser.setOnline(false);
        cUserService.update(cUser);

        simpMessagingTemplate.convertAndSend("/topic/participant-status", cUser);

        sessionRegistry.getAllSessions(authentication.getPrincipal(), false)
                .forEach(SessionInformation::expireNow);
    }
}
