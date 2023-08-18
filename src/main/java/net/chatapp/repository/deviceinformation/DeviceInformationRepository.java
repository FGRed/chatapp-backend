package net.chatapp.repository.deviceinformation;
import org.springframework.data.jpa.repository.JpaRepository;
import net.chatapp.model.deviceinformation.DeviceInformation;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceInformationRepository extends JpaRepository<DeviceInformation, Long> {
}