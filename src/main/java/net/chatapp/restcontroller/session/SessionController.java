package net.chatapp.restcontroller.session;

import net.chatapp.model.cuser.CUser;
import net.chatapp.service.cuser.CUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/session")
@RestController
public class SessionController {

    @Autowired
    private CUserService service;

    @Secured({"USER", "ADMIN"})
    @DeleteMapping("/logout")
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

    @PostMapping("/")
    ResponseEntity<CUser> logIn(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpServletRequest request) {
        CUser c = service.logIn(username, password, request);
        if (c != null) {
            return ResponseEntity.ok(c);
        } else {
            return ResponseEntity.badRequest().body(null);
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

}
