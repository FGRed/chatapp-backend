package net.chatapp.service.deviceinformation;
import net.chatapp.model.cuser.CUser;
import net.chatapp.model.deviceinformation.DeviceInformation;
import net.chatapp.repository.cuser.CUserRepository;
import net.chatapp.repository.deviceinformation.DeviceInformationRepository;
import net.chatapp.util.DeviceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class DeviceInformationService {

    @Autowired
    public DeviceInformationRepository deviceInformationRepository;

    @Autowired
    public CUserRepository cUserRepository;


    public CUser fillDeviceInformation(HttpServletRequest httpServletRequest, final CUser cUser){
        CUser retval = clearPrevious(cUser);

        DeviceInformation deviceInformation = new DeviceInformation();
        String browserType = httpServletRequest.getHeader("User-Agent");
        String browser = DeviceUtil.getBrowserInfo(browserType);
        deviceInformation.setDeviceName(browser);
        deviceInformation.setIp(httpServletRequest.getRemoteAddr());
        deviceInformation = deviceInformationRepository.save(deviceInformation);

        retval.setDeviceInformation(deviceInformation);
        return cUserRepository.save(retval);
    }

    public CUser clearPrevious(CUser cUser){
        if(cUser.getDeviceInformation() != null){
            DeviceInformation deviceInformation = cUser.getDeviceInformation();
            cUser.setDeviceInformation(null);
            cUser = cUserRepository.save(cUser);
            deviceInformationRepository.delete(deviceInformation);
        }
        return cUser;
    }



}