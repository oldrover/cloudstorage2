package com.udacity.jwdnd.course1.cloudstorage.service;


import com.udacity.jwdnd.course1.cloudstorage.model.FileData;
import com.udacity.jwdnd.course1.cloudstorage.repository.FileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;

    public boolean isFileNameAvailable(Long userId, String fileName){
        return fileRepository.findByUserIdAndFileName(userId, fileName) == null;
    }

    public FileData addFile(FileData fileData) {
        return fileRepository.save(fileData);
    }

    public FileData viewFile(Long userId, Long fileId){
        return fileRepository.findByUserIdAndFileId(userId, fileId);
    }

    public void deleteFile(Long fileId){
        fileRepository.deleteById(fileId);
    }

    public List<FileData> getFiles(Long userId) {
        return fileRepository.findByUserId(userId);
    }
}
