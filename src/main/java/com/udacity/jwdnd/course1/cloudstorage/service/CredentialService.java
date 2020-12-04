package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private HashService hashService;

    public CredentialService(CredentialMapper credentialMapper, HashService hashService) {
        this.credentialMapper = credentialMapper;
        this.hashService = hashService;
    }

    public int addCredential(User user, CredentialForm credentialForm) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedkey = Base64.getEncoder().encodeToString(key);
        String hashedPassword = hashService.getHashedValue(credentialForm.getPassword(), encodedkey);
        return credentialMapper.insertCredential(new Credential(null,credentialForm.getUrl(), credentialForm.getUsername(), encodedkey,hashedPassword, user.getUserId()));


    }

    public List<Credential> getCredentials(Integer userId) {
        return credentialMapper.getCredential(userId);
    }
}
