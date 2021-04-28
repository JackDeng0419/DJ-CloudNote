package com.jack.CloudNote.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBUtil {
    //properties 其实就是一系列键值对
    private static Properties properties = new Properties();

    static {
        try {
            // 获取文件流
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            // 加载键值对
            properties.load(in);
            // 注册JDBC驱动，创建加载此驱动的Class对象
            Class.forName(properties.getProperty("jdbcName"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库链接
     * @return
     */
    public static Connection getConnection() {
        Connection connection = null;

        try {
            String dbUrl = properties.getProperty("dbUrl");
            String dbName = properties.getProperty("dbName");
            String dbPwd = properties.getProperty("dbPwd");

            connection = DriverManager.getConnection(dbUrl,dbName,dbPwd);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return connection;
    }

    /**
     * 关闭所有数据库资源
     * @param resultSet
     * @param preparedStatement
     * @param connection
     */
    public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet){
        //判断资源如果不为空，则关闭
        try {
            if(resultSet != null) {
                resultSet.close();
            }
            if(preparedStatement != null) {
                preparedStatement.close();
            }
            if(connection != null) {
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
