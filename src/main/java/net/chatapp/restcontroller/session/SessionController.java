package net.chatapp.restcontroller.session;

import net.chatapp.model.cuser.CUser;
import net.chatapp.service.cuser.CUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/session")
@RestController
public class SessionController {

    @Autowired
    private CUserService service;

    @DeleteMapping("/current")
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
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/")
    ResponseEntity<CUser> logIn(@RequestParam("username") String username,
                                @RequestParam("password") String password) {
        CUser c = service.logIn(username, password);
        if (c != null) {
            return ResponseEntity.ok(c);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
