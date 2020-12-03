package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;


    public NoteService() {
    }

    public void addNote(User user) {
        Note newNote = new Note();
        newNote.setNoteTitle("Test Title");
        newNote.setNoteDescription("Test Description");
        newNote.setUserId(user.getUserId());


    }

    public List<Note> getNotes(Integer userId) {
        return noteMapper.getNote(userId);

    }




}
