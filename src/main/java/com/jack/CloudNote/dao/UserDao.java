package com.jack.CloudNote.dao;

import com.jack.CloudNote.po.User;
import com.jack.CloudNote.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    /**
     * 通过用户名查询用户
     *  定义sql语句
     *  设置参数集合
     *  调用BaseDao
     * @param username
     * @return
     */
    public User queryUserByName(String username) {
        User user = null;
        String sql = "SELECT * FROM users WHERE uname = ?";
        List<Object> params = new ArrayList<>();
        params.add(username);
        user = (User) BaseDao.queryRow(sql, params, User.class);
        return user;

    }

    /**
     * 1. 获取数据库连接
     * 2. 定义sql语句
     * 3. 预编译
     * 4. 设置参数
     * 5. 执行查询，返回resultSet
     * 6. 判断并分析结果集
     * 7. 关闭资源。
     * @param username
     * @return
     */
    public User queryUserByName02(String username) {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //1. 获取数据库连接
            connection = DBUtil.getConnection();

            //2. 定义sql
            String sql = "SELECT * FROM users WHERE uname = ?";
            //3. 预编译
            preparedStatement = connection.prepareStatement(sql);
            //4. 设置参数
            preparedStatement.setString(1, username);
            //5. 执行查询
            resultSet = preparedStatement.executeQuery();
            //6. 判断并分析结果集
            if(resultSet.next()){
                user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUname(username);
                user.setHead(resultSet.getString("head"));
                user.setMood(resultSet.getString("mood"));
                user.setNick(resultSet.getString("nick"));
                user.setUpwd(resultSet.getString("upwd"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            DBUtil.close(connection, preparedStatement, resultSet);
        }

        return user;
    }

    public User queryUserByNickAndUserId(String nickName, Integer userId) {
        // 设置sql
        String sql = "SELECT * FROM users WHERE nick=? and userId != ?";
        // 设置参数集合
        List<Object> params = new ArrayList<>();
        params.add(nickName);
        params.add(userId);
        // 调用baseDao
        User user = (User) BaseDao.queryRow(sql, params, User.class);
        return user;
    }

    public int updateUser(User user) {
        String sql = "UPDATE users SET nick = ?, mood = ?, head = ? WHERE userId = ?";
        List<Object> params = new ArrayList<>();
        params.add(user.getNick());
        params.add(user.getMood());
        params.add(user.getHead());
        params.add(user.getUserId());

        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }
}
