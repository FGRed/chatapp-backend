package net.chatapp.restcontroller.cuser;

import lombok.Getter;
import net.chatapp.model.cuser.CUser;
import net.chatapp.service.cuser.CUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cuser")
public class CUserRestController {

    @Autowired
    private CUserService service;


    @PostMapping("/sign-up")
    ResponseEntity<CUser> singUp(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam("password-repeat") String passwordRepeat) {
        if (password.equals(passwordRepeat)) {
            CUser c = service.signUp(username, password);
            if (c == null) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(c);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/log-in")
    ResponseEntity<CUser> logIn(@RequestParam("username") String username,
                                @RequestParam("password") String password) {
        CUser c = service.logIn(username, password);
        if (c != null) {
            return ResponseEntity.ok(c);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
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


}