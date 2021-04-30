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

    public List<Note> findNoteListByPage(Integer userId, Integer index, Integer pageSize, String title, String date, String typeId) {
        String sql = "SELECT noteId, title, pubTime FROM notes AS n INNER JOIN" +
                " types AS t ON n.typeId = t.typeId " +
                "WHERE userId=? " +
                "LIMIT ?, ?";
        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(index);
        params.add(pageSize);

        // 调用BaseDao的查询方法
        List<Note> noteList = BaseDao.queryRows(sql, params, Note.class);

        return noteList;
    }

    public long findNoteCount(Integer userId) {
        String sql = "SELECT count(1) FROM notes AS n INNER JOIN" +
                " types AS t ON n.typeId = t.typeId " +
                "WHERE userId=?";
        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 调用BaseDao的查询方法
        long count = (long) BaseDao.findSingleValue(sql, params);

        return count;
    }
}
