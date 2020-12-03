package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public int addCredential(User user, CredentialForm credentialForm) {
        String key ="";
        String password = credentialForm.getPassword();

        return credentialMapper.insertCredential(new Credential(null,credentialForm.getUrl(), credentialForm.getUsername(), key,password, user.getUserId()));


    }

    public List<Credential> getCredentials(Integer userId) {
        return credentialMapper.getCredential(userId);
    }
}
