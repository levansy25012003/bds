package com.example.bds.service;

import com.example.bds.dto.rep.DataMailDTO;
import jakarta.mail.MessagingException;

public interface IMailService {
    void sendHtmlMail(DataMailDTO dataMail, String templateName) throws MessagingException;
}
