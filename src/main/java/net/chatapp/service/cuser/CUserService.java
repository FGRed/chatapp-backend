package net.chatapp.service.cuser;

import net.chatapp.error.CUserNotFoundException;
import net.chatapp.error.CUserSaveExcpetion;
import net.chatapp.model.contact.Contact;
import net.chatapp.model.cuser.CUser;
import net.chatapp.repository.contact.ContactRepository;
import net.chatapp.repository.cuser.CUserRepository;
import net.chatapp.service.BasicService;
import net.chatapp.service.deviceinformation.DeviceInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.io.IOException;
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

    public CUser logIn(String username, String password, HttpServletRequest request) {

        var userOpt = cUserRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            userOpt = cUserRepository.findByEmail(username);
        }

        if (userOpt.isPresent()) {
            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(userOpt.get().getUsername(), password);
            Authentication authenticatedUser = authenticationManager.authenticate(loginToken);

            if (!authenticatedUser.isAuthenticated()) {
                return null;
            }

            CUser user = ((CUser) authenticatedUser.getPrincipal());

            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

            String sessionId = request.getSession().getId();
            sessionRegistry.registerNewSession(sessionId, authenticatedUser.getPrincipal());
            user.setSessionId(sessionId);
            user.setDeviceInformation(null);
            user = deviceInformationService.fillDeviceInformation(request, user);
            user.setOnline(true);

            user = cUserRepository.save(user);


            return user;
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

    public CUser signUp(String username, String password, String email) {
        UserDetails userDetails = loadUserByUsername(username);
        if (userDetails == null) {
            CUser c = new CUser();
            c.setUsername(username);
            c.setPassword(password);
            c.setEmail(email);
            c.setRole("USER");
            c = saveNew(c);
            return c;
        }
        return null;
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

        sessionRegistry.removeSessionInformation(getCurrentSessionUser().getSessionId());
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        cookieClearingLogoutHandler.logout(request, response, null);
        securityContextLogoutHandler.logout(request, response, null);

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


}