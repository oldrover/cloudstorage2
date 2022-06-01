package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    @Autowired
    CredentialRepository credentialRepository;

    @Autowired
    EncryptionService encryptionService;

    public Credential addCredential(User user, CredentialForm credentialForm) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encodedKey);
        return credentialRepository.save(new Credential(null,credentialForm.getUrl(), credentialForm.getUsername(), encodedKey,encryptedPassword, user.getUserId()));

    }

    public Credential updateCredential(User user, CredentialForm credentialForm) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encodedKey);
        return credentialRepository.save(new Credential(credentialForm.getCredentialId(),credentialForm.getUrl(), credentialForm.getUsername(), encodedKey, encryptedPassword, user.getUserId()));
    }

    public void deleteCredential(Long credentialId) {
        credentialRepository.deleteById(credentialId);
    }

    public List<Credential> getCredentials(Long userId) {

        List<Credential> credentialList = credentialRepository.findByUserId(userId);
        credentialList.forEach(cr -> cr.setDecryptedPassword(encryptionService.decryptValue(cr.getPassword(), cr.getKey())));
        return credentialList;
    }
}
