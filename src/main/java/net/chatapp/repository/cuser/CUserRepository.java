package net.chatapp.repository.cuser;
import org.springframework.data.jpa.repository.JpaRepository;
import net.chatapp.model.cuser.CUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CUserRepository extends JpaRepository<CUser, Long>, CustomCUserRepository {

    @Query("select c from CUser c where c.username = :username")
    Optional<CUser> findByUsername(@Param("username") final String username);

    @Query("select c from CUser c where c.email = :email")
    Optional<CUser> findByEmail(@Param("email") final String email);
}