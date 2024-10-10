package net.chatapp.service.cuser;

import net.chatapp.error.CUserNotFoundException;
import net.chatapp.error.CUserSaveExcpetion;
import net.chatapp.model.cuser.CUser;
import net.chatapp.repository.contact.ContactRepository;
import net.chatapp.repository.cuser.CUserRepository;
import net.chatapp.service.BasicService;
import net.chatapp.service.deviceinformation.DeviceInformationService;
import org.apache.commons.codec.binary.Base64;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CUserService implements UserDetailsService, BasicService<CUser, Long> {

    @Autowired
    private CUserRepository cUserRepository;
    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    private Logger logger = Logger.getLogger(CUserService.class.getName());

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private DeviceInformationService deviceInformationService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        Optional<CUser> cUserOptional = cUserRepository.findByUsername(username);
        if (cUserOptional.isPresent()) {
            return cUserOptional.get();
        }
        logger.info("No user found with username " + username);
        return null;
    }

    public CUser loadUserByEmail(final String email) {
        Optional<CUser> cUserOptional = cUserRepository.findByEmail(email);
        if (cUserOptional.isPresent()) {
            return cUserOptional.get();
        }
        logger.info("No user found with email " + email);
        return null;
    }

    @Override
    public CUser saveNew(CUser entity) {
        if (entity.getId() == null) {
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            return cUserRepository.save(entity);
        }
        throw new CUserSaveExcpetion("This method is meant for creating new entities only!");
    }

    @Override
    public CUser update(CUser entity) {
        return cUserRepository.save(entity);
    }

    @Override
    public CUser findById(Long aLong) {
        Optional<CUser> cUserOptional = cUserRepository.findById(aLong);
        if (cUserOptional.isEmpty()) {
            throw new CUserNotFoundException("No user found with " + aLong);
        }
        return cUserOptional.get();
    }

    @Override
    public void deleteById(Long aLong) {
        Optional<CUser> cUserOptional = cUserRepository.findById(aLong);
        if (cUserOptional.isEmpty()) {
            throw new CUserNotFoundException("Cannot delete. No user found with id " + aLong);
        }
        cUserRepository.deleteById(aLong);
    }

    @Override
    public void delete(CUser entity) {
        Optional<CUser> cUserOptional = cUserRepository.findById(entity.getId());
        if (cUserOptional.isEmpty()) {
            throw new CUserNotFoundException("Cannot delete. No user found with id " + entity.getId());
        }
        cUserRepository.delete(entity);
    }

    @Override
    public void deleteAll(Collection<CUser> entities) {
        cUserRepository.deleteAll(entities);
    }

    @Override
    public Collection<CUser> saveAll(Collection<CUser> entities) {
        return cUserRepository.saveAll(entities);
    }

    public Pair<Object, String> logIn(String username, String password, HttpServletRequest request, boolean logoutActive, HttpServletResponse response) {

        var userOpt = cUserRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            userOpt = cUserRepository.findByEmail(username);
        }

        if (userOpt.isPresent()) {
            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(userOpt.get().getUsername(), password);
            Authentication authenticatedUser = null;
            try {
                authenticatedUser= authenticationManager.authenticate(loginToken);
                if (!authenticatedUser.isAuthenticated()) {
                    return Pair.with(true, "Invalid username or password");
                }
            } catch (Exception e){
                return Pair.with(null, e.getMessage());
            }

            CUser user = ((CUser) authenticatedUser.getPrincipal());

            if(userAlreadyLoggedIn(username)){
                if(logoutActive){
                    //Logging out user and let the code go their merry way
                    logout(request, response);
                }else{
                    logger.warning("User already logged in!");
                    return Pair.with(null, "User already logged in on " + user.getDeviceInformation().getDeviceName() + " at " +user.getDeviceInformation().getIp() + ". ");
                }

            }

            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

            String sessionId = request.getSession().getId();
            sessionRegistry.registerNewSession(sessionId, authenticatedUser.getPrincipal());
            user.setSessionId(sessionId);
            user.setDeviceInformation(null);
            user = deviceInformationService.fillDeviceInformation(request, user);
            user.setOnline(true);

            user = cUserRepository.save(user);
            simpMessagingTemplate.convertAndSend("/topic/participant-status", user);

            return Pair.with(user, "Success!");
        }

        return null;

    }

    public CUser getSessionUser(String username, String password) {

        CUser userComp = (CUser) loadUserByUsername(username);
        if (userComp == null) {
            userComp = loadUserByEmail(username);
            if (userComp == null) {
                return null;
            }
        }

        UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(userComp.getUsername(), password);
        Authentication authenticatedUser = authenticationManager.authenticate(loginToken);
        if (authenticatedUser != null && authenticatedUser.isAuthenticated()) {
            return ((CUser) authenticatedUser.getPrincipal());
        }
        return null;
    }


    public boolean userAlreadyLoggedIn(String username) {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object o : principals) {
            if (o instanceof CUser && ((CUser) o).getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public CUser signUp(String username, String password, String email) throws IOException {
        UserDetails userDetails = loadUserByUsername(username);
        if (userDetails == null) {
            CUser c = new CUser();
            c.setUsername(username);
            c.setPassword(password);
            c.setEmail(email);
            c.setRole("USER");
            c.setAvatar(readBase64FromFile(System.getProperty("user.dir")+"/src/main/java/net/chatapp/conf/db/avatarbase-64.txt"));
            c = saveNew(c);
            return c;
        }
        return null;
    }

    private byte[] readBase64FromFile(String filePath) throws IOException {
        return Base64.decodeBase64(Files.readAllBytes(new File(filePath).toPath()));
    }

    public CUser getCurrentSessionUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CUser) {
                return ((CUser) principal);
            }
        }

        return null;
    }

    public CUser changeUserEmail(String email) {
        Optional<CUser> userOptional = cUserRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            CUser cUser = getCurrentSessionUser();
            cUser.setEmail(email);
            cUser = cUserRepository.save(cUser);
            return cUser;
        }
        return null;
    }


    public CUser changeUsername(String username) {
        Optional<CUser> userOptional = cUserRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            CUser cUser = getCurrentSessionUser();

            cUser.setUsername(username);
            cUser = cUserRepository.save(cUser);
            return cUser;
        }
        return null;
    }

    public CUser logout(HttpServletRequest request, HttpServletResponse response) {

        CUser cUser = getCurrentSessionUser();

        cUser.setOnline(false);
        update(cUser);

        sessionRegistry.removeSessionInformation(cUser.getSessionId());
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        cookieClearingLogoutHandler.logout(request, response, null);
        securityContextLogoutHandler.logout(request, response, null);

        simpMessagingTemplate.convertAndSend("/topic/participant-status", cUser);

        sessionRegistry.getAllSessions(cUser, false)
                .forEach(SessionInformation::expireNow);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
            }
        }
        return new CUser();
    }

    public CUser changeAvatar(MultipartFile avatarMultiPart) {
        try {
            byte[] base64Avatar = avatarMultiPart.getBytes();
            CUser cUser = getCurrentSessionUser();
            if (cUser != null) {
                cUser.setAvatar(base64Avatar);
                cUserRepository.save(cUser);
                return cUser;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<CUser> getUsers() {
        List<CUser> retVal = cUserRepository.findAll();
        CUser currentUser = getCurrentSessionUser();
        retVal.removeIf(cUser -> cUser.getId().equals(currentUser.getId()));
        return retVal;
    }

    public List<CUser> findByIds(Long... ids) {
        return cUserRepository.findAllById(List.of(ids));
    }

    public List<CUser> find(String keyword){
        return cUserRepository.find(keyword);
    }


}