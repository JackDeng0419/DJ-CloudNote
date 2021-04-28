package com.jack.CloudNote;

import com.jack.CloudNote.dao.UserDao;
import com.jack.CloudNote.po.User;
import org.junit.Test;


public class TestUser {
    @Test
    public void testQueryUserByName() {
        UserDao userDao = new UserDao();
        User user = userDao.queryUserByName("admin");
        System.out.println(user.getUpwd());
    }
}
