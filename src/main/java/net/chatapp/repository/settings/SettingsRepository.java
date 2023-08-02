package net.chatapp.repository.settings;
import org.springframework.data.jpa.repository.JpaRepository;
import net.chatapp.model.settings.Settings;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
}