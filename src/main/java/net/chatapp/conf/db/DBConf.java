package net.chatapp.conf.db;

import net.chatapp.model.cuser.CUser;
import net.chatapp.service.cuser.CUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConf {

    @Autowired
    public CUserService cUserService;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            CUser cUser = new CUser();
            cUser.setUsername("admin");
            cUser.setPassword("admin");
            cUserService.saveNew(cUser);
        };
    }
}
