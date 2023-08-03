package net.chatapp.conf.db;

import net.chatapp.model.cuser.CUser;
import net.chatapp.service.cuser.CUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConf {

    @Autowired
    public CUserService cUserService;

    @Autowired
    ApplicationArguments appArgs;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {


            CUser cUser = new CUser();
            cUser.setAvatar("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/a6ce62a7-bf87-485f-a844-cc45c137d88b/dakwf2r-ffc886d4-f25a-4a8f-84a0-c2d34aaf91d3.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcL2E2Y2U2MmE3LWJmODctNDg1Zi1hODQ0LWNjNDVjMTM3ZDg4YlwvZGFrd2Yyci1mZmM4ODZkNC1mMjVhLTRhOGYtODRhMC1jMmQzNGFhZjkxZDMucG5nIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.soh3wRnoOJJin1l6bNUKxnYTn3ZMcpsV9CNZPrUf7GU");
            cUser.setUsername("admin");
            cUser.setPassword("admin");
            cUserService.saveNew(cUser);
        };
    }
}
