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
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int addCredential(User user, CredentialForm credentialForm) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encodedKey);
        return credentialMapper.insertCredential(new Credential(null,credentialForm.getUrl(), credentialForm.getUsername(), encodedKey,encryptedPassword, user.getUserId()));

    }

    public int updateCredential(CredentialForm credentialForm) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encodedKey);
        return credentialMapper.updateCredential(new Credential(credentialForm.getCredentialId(),credentialForm.getUrl(), credentialForm.getUsername(), encodedKey, encryptedPassword, null));
    }

    public int deleteCredential(Integer credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }

    public List<Credential> getCredentials(Integer userId) {

        //decryption here and returning list
        List<Credential> credentialList = credentialMapper.getCredential(userId);
        credentialList.stream().forEach(cr -> cr.setEncryptedPassword(encryptionService.decryptValue(cr.getPassword(), cr.getKey())));
        return credentialList;
    }
}
