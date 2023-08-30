package net.chatapp.service.server;

import net.chatapp.model.server.ServerInfo;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

@Service
public class ServerService {


    public ServerInfo getServerInfo() throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        String version = model.getVersion();
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setApplicationVersion(version);
        return serverInfo;
    }

}
