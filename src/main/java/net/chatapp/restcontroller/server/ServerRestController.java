package net.chatapp.restcontroller.server;

import net.chatapp.model.server.ServerInfo;
import net.chatapp.service.server.ServerService;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/server")
public class ServerRestController {

    @Autowired
    private ServerService service;

    @GetMapping("/")
    ResponseEntity<ServerInfo> getServerInfo() throws XmlPullParserException, IOException {
        return ResponseEntity.ok().body(service.getServerInfo());
    }

}
