package com.jack.CloudNote.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.jack.CloudNote.dao.UserDao;
import com.jack.CloudNote.po.User;
import com.jack.CloudNote.vo.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class UserService {
    private UserDao userDao = new UserDao();

    /**
     * 1. 参数的非空校验（准备一个封装类ResultInfo，用来封装响应结果：状态吗，提示信息，返回的对象）
     *     1. 如果为空
     *         1. 设置ResultInfo对象的状态码和提示信息
     *         2. 将ResultInfo对象设置request作用域中
     *         3. 请求转发跳转到登陆界面
     *         4. return
     *     2. 如果不为空，通过用户名查询用户对象
     *         1. 如果用户不存在
     *             1. 设置ResultInfo对象的状态码和提示信息
     *             2. 将ResultInfo对象设置request作用域中
     *         2. 如果用户存在
     *             1. 验证密码（加密后比较）
     *                 1. 密码不正确：
     *                     1. 设置ResultInfo对象的状态码和提示信息
     *                     2. 将ResultInfo对象设置request作用域中
     *                 2. 密码正确：
     *                     1. 设置ResultInfo对象的状态码和提示信息
     * @return
     */
    public ResultInfo<User> userLogin(String userName, String userPwd) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        System.out.println(userName);
        User user = new User();
        user.setUname(userName);
        user.setUpwd(userPwd);
        resultInfo.setResult(user);

        // 1. 参数的非空校验
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(userName)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户姓名或密码不能为空");
            return resultInfo;
        }

        // 2. 参数不为空，通过用户名查询用户对象
        user = userDao.queryUserByName(userName);

        // 3. 判断用户对象是否为空
        if(user == null) {
            // 如果为空，设置ResultInfo对象的状态码和提示信息
            resultInfo.setCode(0);
            resultInfo.setMsg("该用户不存在");
            return resultInfo;
        }

        // 4. 如果用户对象不为空，则比较密码
        //先加密
        userPwd = DigestUtil.md5Hex(userPwd);
        //System.out.println(userPwd);
        //判断密码是否相同
        if(!userPwd.equals(user.getUpwd())) {
            // 密码不正确
            resultInfo.setCode(0);
            resultInfo.setMsg("密码错误");
            return resultInfo;
        }

        resultInfo.setCode(1);
        resultInfo.setResult(user);
        return resultInfo;
    }

    public Integer checkNick(String nickName, Integer userId) {
        // 1. 判断昵称是否为空
        if (StrUtil.isBlank(nickName)) {
            return 0;
        }
        // 1. 根据nickName 寻找user
        User user = userDao.queryUserByNickAndUserId(nickName, userId);

        if(user != null){
            return 0;
        }
        return 1;
    }

    public ResultInfo<User> updateUser(HttpServletRequest request) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        // 1. 获取参数：昵称，心情
        String nick = request.getParameter("nick");
        String mood = request.getParameter("mood");

        // 昵称不能为空
        if(StrUtil.isBlank(nick)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户昵称不能为空");
            return resultInfo;
        }

        // 从session作用域中取得当前user对象，先修改昵称和心情
        User user = (User) request.getSession().getAttribute("user");
        user.setNick(nick);
        user.setMood(mood);

        // 4. 实现上传文件
        try{
            // 获取part对象，也就是request的表单中的某一个name的值
            Part part = request.getPart("img");
            // 从请求头中获取上传文件的文件名
            String header = part.getHeader("Content-Disposition");
            // 获取具体请求头的对应值
            String str = header.substring(header.lastIndexOf("=") + 2);
            // 获取上传的文件名
            String fileName = str.substring(0, str.length() - 1);
            // 判断文件名是否为空
            if (!StrUtil.isBlank(fileName)) {
                // 如果用户上传了头像，则更新用户对象中的头像
                user.setHead(fileName);
                // 4. 获取文件存放的路径  WEB-INF/upload/目录中
                String filePath = request.getServletContext().getRealPath("/WEB-INF/upload");
                //System.out.println(filePath + "/" + fileName);
                // 5. 上传文件到指定目录
                part.write(filePath + "/" + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }

        // 6. 调用Dao层的更新方法,更新数据库中的数据，返回受影响的行数
        int row = userDao.updateUser(user);
        // 7. 判断受影响的行数
        if (row > 0) {
            resultInfo.setCode(1);
            // 更新session中用户对象
            request.getSession().setAttribute("user", user);
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }

        return resultInfo;
    }
}
