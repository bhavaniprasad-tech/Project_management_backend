package com.bhavani.service;

import com.bhavani.model.Invitation;
import jakarta.mail.MessagingException;

public interface InvitationService {

    public void sendInvitation(String email, Long projectId) throws MessagingException;

    public Invitation acceptInvitation(String email, Long userId) throws Exception;

    public String getTokenByUserMail(String email);

    void deleteToken(String email);

}
