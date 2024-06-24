package com.gymmanagementsystembackend.tool;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MSGServiceImpl {
    @Autowired
    private ObjectMapper objectMapper;

    public  boolean send(Map map, String phone) {
        String id=System.getProperty("SEND_MSG_ID");
        String secret=System.getProperty("SEND_MSG_SECRET");

        if(StringUtils.isEmpty(phone)) return false;

        Config config = new Config()
                .setAccessKeyId(id)
                .setAccessKeySecret(secret);

        config.endpoint = "dysmsapi.aliyuncs.com";
        Client client = null;
        try {
            client = new Client(config);
            SendSmsRequest request = new SendSmsRequest();

            request.setSignName("阿里云短信测试");
            request.setTemplateCode("SMS_154950909");
            request.setPhoneNumbers("17374451797");

            request.setTemplateParam(objectMapper.writeValueAsString(map));
            SendSmsResponse response = client.sendSms(request);
            System.out.println("发送成功："+new Gson().toJson(response));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
