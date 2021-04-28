package com.jack.CloudNote.dao;

import com.jack.CloudNote.util.DBUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础的JDBC操作类
 *      更新操作
 *      查询操作
 *          1. 查询一个字段（只返回一个值）
 *          2. 查询集合
 *          3. 查询某个对象
 */
public class BaseDao {


    /**
     * 更新操作
     * 1. 得到数据库连接
     * 2. 定义sql语句
     * 3. 预编译
     * 4. 如果有参数，则设置参数，下标从1开始
     * 5. 执行更新，返回受影响行数
     * 6. 关闭资源
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, List<Object> params) {
        int row = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            if(params != null && params.size() > 0){
                for(int i = 0; i < params.size(); i++) {
                    preparedStatement.setObject(i+1, params.get(i));
                }
            }
            row = preparedStatement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            DBUtil.close(connection,preparedStatement,null);
        }
        return row;
    }

    /**
     * 查询一个字段
     * @param sql
     * @param params
     * @return
     */
    public static Object findSingleValue(String sql, List<Object> params) {
        Object object = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            if(params != null && params.size() > 0){
                for(int i = 0; i < params.size(); i++) {
                    preparedStatement.setObject(i+1, params.get(i));
                }
            }
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                object = resultSet.getObject(1);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }

        return object;
    }

    /**
     * 查询集合（数据库的字段名要与bean的相同）
     * @param sql
     * @param params
     * @param cls
     * @return
     */
    public static List queryRows(String sql, List<Object> params, Class cls) {
        List list = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            if(params != null && params.size() > 0){
                for(int i = 0; i < params.size(); i++) {
                    preparedStatement.setObject(i+1, params.get(i));
                }
            }
            // 执行查询
            resultSet = preparedStatement.executeQuery();

            // 获得结果集的元数据对象
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 获得结果集的字段数
            int fieldNum = resultSetMetaData.getColumnCount();

            while(resultSet.next()) {
                // 反射获得实例化对象
                Object object = cls.newInstance();

                for(int i = 1; i <= fieldNum ; i++) {
                    //获得字段名
                    String columnName = resultSetMetaData.getColumnLabel(i);
                    //通过反射，使用类名得到响应的field对象
                    Field field = cls.getDeclaredField(columnName);

                    // 我们使用非暴力的方法来设置bean的字段
                    //      1. 拼接setXxx方法名
                    String setMethod = "set" + columnName.substring(0,1).toUpperCase() + columnName.substring(1);
                    //      2. 通过反射，将setMethod反射成真正的set方法对象
                    Method method = cls.getDeclaredMethod(setMethod, field.getType());
                    method.invoke(object, resultSet.getObject(columnName));
                }
                list.add(object);
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }

        return list;
    }

    public static Object queryRow(String sql, List<Object> params, Class cls) {
        List list = queryRows(sql, params, cls);

        Object object = null;

        if(list != null && list.size()>0) {
            object = list.get(0);
        }

        return object;
    }

}
