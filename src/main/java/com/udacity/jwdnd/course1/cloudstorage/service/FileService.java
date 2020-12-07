package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileDataMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.FileData;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileDataMapper fileDataMapper;

    public FileService(FileDataMapper fileDataMapper) {
        this.fileDataMapper = fileDataMapper;
    }

    public int addFile(FileData fileData) {
        return fileDataMapper.insertFile(fileData);
    }

    public FileData viewFile(Integer fileId){
        return fileDataMapper.viewFile(fileId);
    }

    public int deleteFile(Integer fileId){
        return fileDataMapper.deleteFile(fileId);
    }

    public List<FileData> getFiles(Integer userId) {
        return fileDataMapper.getFiles(userId);
    }
}
