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

    @PutMapping("/email/{email}")
    ResponseEntity<CUser> setEmail(@PathVariable("email") String email){
        CUser c = service.changeUserEmail(email);
        if(c != null){
            return ResponseEntity.ok(c);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PutMapping("/username/{username}")
    ResponseEntity<CUser> setUsername(@PathVariable("username") String username){
        CUser c = service.changeUsername(username);
        if(c != null){
            return ResponseEntity.ok(c);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/")
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


}