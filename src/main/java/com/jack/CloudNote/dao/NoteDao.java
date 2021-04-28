package com.jack.CloudNote.dao;

import com.jack.CloudNote.po.Note;
import com.sun.xml.internal.rngom.parse.host.Base;

import java.util.ArrayList;
import java.util.List;

public class NoteDao {

    public int addOrUpdate(Note note) {
        String sql = "";
        List<Object> params = new ArrayList<>();
        params.add(note.getTypeId());
        params.add(note.getTitle());
        params.add(note.getContent());

        if(note.getNoteId() == null) { // adding
            sql = "INSERT INTO notes (typeId, title, content, pubTime) VALUES (?, ?, ?, now())";
        }

        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }
}
