package pri.wkz.mockitodemo.demos.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author kuiZhi Wang
 */
@Slf4j
@Service
public class MessengerService {
    @Value("${messenger.endpoint:''}")
    private String messengerEndpoint;
    private RestTemplate restTemplate = new RestTemplate();

    public boolean sendSms(String text, String phone) {
        JSONObject payload = new JSONObject();
        payload.put("body", text);
        payload.put("receipt", phone);
        Boolean result = restTemplate.postForObject(messengerEndpoint + "/api/sms/send",
                payload, Boolean.class);
        return Boolean.TRUE.equals(result);
    }
}
