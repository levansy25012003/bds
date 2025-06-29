package com.example.bds.service.impl;

import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioService {

    @Value("${twilio.serviceSid}")
    private String serviceSid;

    public boolean sendOTP(String phoneNumber) {
        try {
            Verification.creator(serviceSid, phoneNumber, "sms").create();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
