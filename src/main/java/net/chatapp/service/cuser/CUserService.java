package net.chatapp.service.cuser;

import net.chatapp.error.CUserNotFoundException;
import net.chatapp.error.CUserSaveExcpetion;
import net.chatapp.model.cuser.CUser;
import net.chatapp.repository.cuser.CUserRepository;
import net.chatapp.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
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


    @Override
    public UserDetails loadUserByUsername(final String username) {
        Optional<CUser> cUserOptional = cUserRepository.findByUsername(username);
        if (cUserOptional.isPresent()) {
            return cUserOptional.get();
        }
        logger.info("No user found with username " + username);
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

    public CUser logIn(String username, String password) {

        var userOpt = cUserRepository.findByUsername(username);
        if(userOpt.isPresent()) {

            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticatedUser = authenticationManager.authenticate(loginToken);

            if (!authenticatedUser.isAuthenticated()) {
                return null;
            }

            CUser user = ((CUser) authenticatedUser.getPrincipal());

            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            return user;
        }

        return null;

    }

    public CUser signUp(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if(userDetails == null) {
            CUser c = new CUser();
            c.setUsername(username);
            c.setPassword(password);
            c = saveNew(c);
            return c;
        }
        return null;
    }


}