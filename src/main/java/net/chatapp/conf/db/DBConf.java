package net.chatapp.conf.db;

import net.chatapp.model.chat.Chat;
import net.chatapp.model.contact.Contact;
import net.chatapp.model.cuser.CUser;
import net.chatapp.repository.chatmessage.ChatMessageRepository;
import net.chatapp.repository.contact.ContactRepository;
import net.chatapp.service.chat.ChatService;
import net.chatapp.service.cuser.CUserService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class DBConf {

    @Autowired
    public CUserService cUserService;

    @Autowired
    ApplicationArguments appArgs;

    @Autowired
    ChatService chatService;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    ContactRepository contactRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {

            CUser cUser = new CUser();
            cUser.setUsername("admin");
            cUser.setPassword("admin");
            cUser.setAvatar(readBase64FromFile(System.getProperty("user.dir")+"/src/main/java/net/chatapp/conf/db/avatarbase-64.txt"));
            cUser.setRole("ADMIN");
            cUser = cUserService.saveNew(cUser);

            CUser cUser2 = new CUser();
            cUser2.setUsername("user");
            cUser2.setPassword("user");
            cUser2.setRole("USER");
            cUser2.setAvatar(readBase64FromFile(System.getProperty("user.dir")+"/src/main/java/net/chatapp/conf/db/avatarbase-64.txt"));
            cUser2 = cUserService.saveNew(cUser2);

            Contact contact = new Contact();
            contact.setContactUser(cUser2);
            contact.setRelatesToUser(cUser.getId());
            contactRepository.save(contact);

            Chat chat = new Chat();
            chat.setChatCreatorId(cUser.getId());
            chat.addToParticipants(cUser);
            chat.addToParticipants(cUser2);
            chat = chatService.saveNew(chat);

        };
    }

    public byte[] readBase64FromFile(String filePath) throws IOException {
        return Base64.decodeBase64(Files.readAllBytes(new File(filePath).toPath()));
    }
}
