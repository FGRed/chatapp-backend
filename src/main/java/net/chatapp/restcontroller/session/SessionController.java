package net.chatapp.restcontroller.session;

import net.chatapp.model.cuser.CUser;
import net.chatapp.model.response.ResponseData;
import net.chatapp.service.cuser.CUserService;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/session")
@RestController
public class SessionController {

    @Autowired
    private CUserService service;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

   //@Secured({"USER", "ADMIN"})
    @DeleteMapping("/log-out")
    ResponseEntity<CUser> logout(HttpServletRequest request, HttpServletResponse response){
        CUser c = service.logout(request, response);
        if(c != null){
            return ResponseEntity.ok(c);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/current")
    ResponseEntity<CUser> getCurrentSessionUser() {
        CUser c = service.getCurrentSessionUser();
        if (c != null) {
            return ResponseEntity.ok(c);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/log-in")
    ResponseEntity<ResponseData> logIn(@RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       @RequestParam(value = "logout-active", required = false)
                                       boolean logoutActive,
                                       HttpServletRequest request, HttpServletResponse response) {
        Pair<Object, String> cPair = service.logIn(username, password, request, logoutActive, response);
        if (cPair.getValue0() != null) {
            return ResponseEntity
                    .ok(new ResponseData(cPair.getValue0(), cPair.getValue1()));
        } else {
            return ResponseEntity.badRequest()
                    .body(new ResponseData(cPair.getValue0(), cPair.getValue1()));
        }
    }

    @PostMapping("/in-registry")
    ResponseEntity<String> getSessionStatus(@RequestParam("username") String username,
                                            @RequestParam("password") String password) {

        CUser user = (CUser) service.getSessionUser(username, password);
        if(user == null) {
            return ResponseEntity.badRequest().body("No user found with given credentials!");
        }

        boolean cuserAlreadyLoggedIn = service.userAlreadyLoggedIn(username);

        if(cuserAlreadyLoggedIn){
            return ResponseEntity.badRequest().body("Unauthorized access detected. You are currently logged in on another device. More info regarding the issue sent to your email.");
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/session-expired")
    ResponseEntity<Void> getSessionExpired(){
       // simpMessagingTemplate.convertAndSend("/topic/session-timeout", true);
        return ResponseEntity.noContent().build();
    }

}
