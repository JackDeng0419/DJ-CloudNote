package com.jack.CloudNote.dao;

import com.jack.CloudNote.po.User;
import junit.framework.TestCase;
import org.junit.Test;

public class UserDaoTest extends TestCase {

    @Test
    public void testQueryUserByName() {
        UserDao userDao = new UserDao();
        String userName = "admin";
        User user = userDao.queryUserByName(userName);
        System.out.println(user.getUname());
    }
}