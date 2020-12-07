package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.FileData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileDataMapper {
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<FileData> getFiles(Integer userId);

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{file})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(FileData fileData);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFile(Integer fileId);
}
