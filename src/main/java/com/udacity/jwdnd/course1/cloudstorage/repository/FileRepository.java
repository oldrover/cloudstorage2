package com.udacity.jwdnd.course1.cloudstorage.repository;

import com.udacity.jwdnd.course1.cloudstorage.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileData, Long> {

    FileData findByUserIdAndFileName(Long userId, String fileName);

    FileData findByUserIdAndFileId(Long userId, Long fileId);

    List<FileData> findByUserId(Long userId);
}
