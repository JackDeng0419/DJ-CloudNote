package com.jack.CloudNote.dao;

import com.jack.CloudNote.po.NoteType;
import com.jack.CloudNote.util.DBUtil;
import com.sun.tools.corba.se.idl.constExpr.Not;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NoteTypeDao {

    public List<NoteType> findTypeByUserId(Integer id) {
        List<NoteType> list = null;
        String sql = "SELECT typeId, typeName, userId FROM types WHERE userId = ?";
        List<Object> params = new ArrayList<>();
        params.add(id);
        list = (List<NoteType>) BaseDao.queryRows(sql, params, NoteType.class);

        return list;
    }


    public long findNoteCountByTypeId(String typeId) {
        String sql = "SELECT count(1) FROM notes WHERE typeId=?";
        List<Object> params = new ArrayList<>();
        params.add(typeId);
        return (long) BaseDao.findSingleValue(sql, params);

    }

    public int deleteTypeById(String typeId) {
        String sql = "DELETE FROM types WHERE typeId=?";
        List<Object> params = new ArrayList<>();
        params.add(typeId);
        return BaseDao.executeUpdate(sql, params);
    }

    public Integer checkTypeName(String typeName, Integer userId, String typeId) {
        String sql = "SELECT * FROM types WHERE typeName=? and userId=?";
        List<Object> params = new ArrayList<>();
        params.add(typeName);
        params.add(userId);
        NoteType noteType = (NoteType) BaseDao.queryRow(sql, params, NoteType.class);

        if(noteType == null) {
            return 1;
        } else {
            if (typeId.equals(noteType.getTypeId().toString())) {
                return 1;
            }
        }
        return 0;
    }

    public Integer addType(String typeName, Integer userId) {
        Integer key = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            String sql = "INSERT INTO types (typeName, userId) values (?, ?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, typeName);
            preparedStatement.setInt(2, userId);

            int row = preparedStatement.executeUpdate();

            if(row > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()) {
                    key = resultSet.getInt(1);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return key;
    }

    public Integer updateType(String typeName, String typeId) {
        String sql = "UPDATE types SET typeName=? WHERE typeId=?";
        List<Object> params = new ArrayList<>();
        params.add(typeName);
        params.add(typeId);
        return BaseDao.executeUpdate(sql, params);
    }
}
