package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    NoteRepository noteRepository;

    public Note addNote(User user, NoteForm noteForm) {
      return noteRepository.save(new Note(null, noteForm.getNoteTitle(), noteForm.getNoteDescription(), user.getUserId()));
    }

    public void deleteNote(Long noteId) {
       noteRepository.deleteById(noteId);
    }

    public Note updateNote(User user, NoteForm noteForm) {
        return noteRepository.save(new Note(noteForm.getNoteId(), noteForm.getNoteTitle(),noteForm.getNoteDescription(), user.getUserId() ));

    }

    public List<Note> getNotes(Long userId) {
        return noteRepository.getNotesByUserId(userId);
    }

}
