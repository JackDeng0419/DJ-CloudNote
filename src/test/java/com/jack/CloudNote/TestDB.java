package com.jack.CloudNote;

import org.junit.Test;

import com.jack.CloudNote.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class TestDB {
    private Logger logger = LoggerFactory.getLogger(TestDB.class);

    /**
     * 单元测试方法
     * 1. 方法的返回值建议为void
     * 2. 参数列表，建议空参
     * 3. 方法需要搭上@Test注解
     * 4. 每个方法需要能独立运行
     */

    @Test
    public void testDB(){
        Connection connection = DBUtil.getConnection();
        System.out.println(connection);
        logger.info("获得数据库：" + connection);
    }
}
