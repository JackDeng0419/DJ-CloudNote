package com.jack.CloudNote.dao;

import cn.hutool.core.util.StrUtil;
import com.jack.CloudNote.po.Note;
import com.jack.CloudNote.vo.NoteVo;
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
        // 定义SQL语句
        String sql = "SELECT noteId,title,pubTime FROM notes n INNER JOIN " +
                "types t on n.typeId = t.typeId WHERE userId = ? ";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);
        //System.out.println(title);
        // 判断条件查询的参数是否为空 （如果查询的参数不为空，则拼接sql语句，并设置所需要的参数）
        if (!StrUtil.isBlank(title)) {
            // 拼接条件查询的sql语句

            sql += " and title like concat('%',?,'%') ";
            // 设置sql语句所需要的参数
            params.add(title);
        }  else if (!StrUtil.isBlank(date)) { // 日期查询
            // 拼接条件查询的sql语句
            sql += " and date_format(pubTime,'%Y年%m月') = ? ";
            // 设置sql语句所需要的参数
            params.add(date);
        } else if (!StrUtil.isBlank(typeId)) { // 类型查询
            // 拼接条件查询的sql语句
            sql += " and n.typeId = ? ";
            // 设置sql语句所需要的参数
            params.add(typeId);
        }

        // 拼接分页的sql语句 （limit语句需要写在sql语句最后）
        sql += " order by pubTime desc limit ?,?";
        params.add(index);
        params.add(pageSize);

        // 调用BaseDao的查询方法
        List<Note> noteList = BaseDao.queryRows(sql, params, Note.class);

        return noteList;
    }

    public long findNoteCount(Integer userId, String title, String date, String typeId) {
        // 定义SQL语句
        String sql = "SELECT count(1) FROM notes n INNER JOIN " +
                " types t on n.typeId = t.typeId " +
                " WHERE userId = ? ";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 判断条件查询的参数是否为空 （如果查询的参数不为空，则拼接sql语句，并设置所需要的参数）
        if (!StrUtil.isBlank(title)) { // 标题查询
            // 拼接条件查询的sql语句
            sql += " and title like concat('%',?,'%') ";
            // 设置sql语句所需要的参数
            params.add(title);

        } else if (!StrUtil.isBlank(date)) { // 日期查询
            // 拼接条件查询的sql语句
            sql += " and date_format(pubTime,'%Y年%m月') = ? ";
            // 设置sql语句所需要的参数
            params.add(date);

        } else if (!StrUtil.isBlank(typeId)) { // 类型查询
            // 拼接条件查询的sql语句
            sql += " and n.typeId = ? ";
            // 设置sql语句所需要的参数
            params.add(typeId);
        }

        // 调用BaseDao的查询方法
        long count = (long) BaseDao.findSingleValue(sql, params);

        return count;
    }

    public List<NoteVo> findNoteCountByType(Integer userId) {
        // 定义SQL语句
        String sql = "SELECT count(noteId) noteCount, t.typeId, typeName groupName FROM notes n " +
                " RIGHT JOIN types t ON n.typeId = t.typeId WHERE userId = ? " +
                " GROUP BY t.typeId ORDER BY COUNT(noteId) DESC ";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 调用BaseDao的查询方法
        List<NoteVo> list = BaseDao.queryRows(sql, params, NoteVo.class);

        return list;
    }


    public List<NoteVo> findNoteCountByDate(Integer userId) {
        // 定义SQL语句
        String sql = "SELECT count(1) noteCount,DATE_FORMAT(pubTime,'%Y年%m月') groupName FROM notes n " +
                " INNER JOIN types t ON n.typeId = t.typeId WHERE userId = ? " +
                " GROUP BY DATE_FORMAT(pubTime,'%Y年%m月')" +
                " ORDER BY DATE_FORMAT(pubTime,'%Y年%m月') DESC ";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 调用BaseDao的查询方法
        List<NoteVo> list = BaseDao.queryRows(sql, params, NoteVo.class);

        return list;
    }

    public Note findNoteById(String noteId) {
        // 定义SQL
        String sql = "select noteId,title,content,pubTime,typeName,n.typeId from notes n " +
                " inner join types t on n.typeId=t.typeId where noteId = ?";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(noteId);
        //System.out.println(noteId);

        // 调用BaseDao的查询方法
        Note note = (Note) BaseDao.queryRow(sql, params, Note.class);
        return note;
    }
}
