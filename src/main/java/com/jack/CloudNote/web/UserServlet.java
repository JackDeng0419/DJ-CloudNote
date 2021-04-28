package com.jack.CloudNote.web;

import com.jack.CloudNote.po.User;
import com.jack.CloudNote.service.UserService;
import com.jack.CloudNote.vo.ResultInfo;
import org.apache.commons.io.FileUtils;
import sun.misc.Request;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.IOException;

@WebServlet(name = "UserServlet", value = "/user")
@MultipartConfig
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 标记高亮
        request.setAttribute("menu_page", "user");

        // 接收用户行为
        String actionName = request.getParameter("actionName");
        // 判断用户行为，调用对应方法
        if("login".equals(actionName)) {
            userLogin(request, response);
        } else if ("logout".equals(actionName)) {
            // 用户退出
            userLogOut(request, response);
        } else if ("userCenter".equals(actionName)) {
            // 个人中心
            userCenter(request, response);
        } else if ("userHead".equals(actionName)) {
            // 个人中心
            userHead(request, response);
        } else if ("checkNick".equals(actionName)) {
            // 验证昵称的唯一性
            checkNick(request, response);
        } else if ("updateUser".equals(actionName)) {
            // 验证昵称的唯一性
            updateUser(request, response);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // 1. 调用service层的方法，传递request对象，返回resultInfo对象
        ResultInfo<User> resultInfo = userService.updateUser(request);
        request.setAttribute("resultInfo", resultInfo);
        request.getRequestDispatcher("user?actionName=userCenter").forward(request, response);
    }

    private void checkNick(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // 1. 获取参数
        String nickName = request.getParameter("nick");
        // 2. 检测昵称是否可用（是否有重复的昵称，用昵称找id，判断id是否为同一个）
        // 2.1 获取当前用户id
        User user = (User) request.getSession().getAttribute("user");
        // 2.2 调用UserService的方法，判断昵称是否重复
        Integer code = userService.checkNick(nickName, user.getUserId());
        response.getWriter().write(code + "");
        response.getWriter().close();
    }

    private void userHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("UserHead");
        // 1. 获取参数 （图片名）
        String head = request.getParameter("imageName");
        // 2. 得到图片的存放路径
        String realPath = request.getServletContext().getRealPath("/WEB-INF/upload");
        // 3. 得到图片的完整路径，得到file对象
        File file = new File(realPath + "/" + head);
        //System.out.println(realPath + "/" + head);
        // 4. 通过截取，得到图片的后缀
        String pic = head.substring(head.lastIndexOf(".")+1);
        // 5. 通过不同的图片后缀，设置不同的响应类型
        if ("PNG".equalsIgnoreCase(pic)) {
            response.setContentType("image/png");
        } else if ("JPG".equalsIgnoreCase(pic) || "JPEG".equalsIgnoreCase(pic)) {
            response.setContentType("image/jpeg");
        } else if ("GIF".equalsIgnoreCase(pic)) {
            response.setContentType("image/gif");
        }
        FileUtils.copyFile(file, response.getOutputStream());
    }

    private void userCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("changePage", "user/info.jsp");
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }


    /**
     * 1. 获取参数
     * 2. 调用Service层的方法，返回ResultInfo对象
     * 3. 判断是否登陆成功
     *     1. 失败
     *         1. 将resultInfo放到request作用域中
     *         2. 请求转发到登陆页面
     *     2. 成功
     *         1. 判断用户是否记住密码
     *             1. 判断用户是否记住密码（rem的值为1）
     *                 1. 是：将用户名和密码存到cookie中，设置失效时间，响应给客户端
     *                 2. 否：清空原有的对象
     *         2. 重定向到index
     */
    protected void userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取参数
        String userName = request.getParameter("userName");
        String userPwd = request.getParameter("userPwd");

        // 2. 调用service的方法，获得ResultInfo对象
        ResultInfo<User> resultInfo = userService.userLogin(userName, userPwd);

        // 3. 判断是否成功登陆
        if (resultInfo.getCode() == 1) {
            // 将用户信息设置到session作用域
            request.getSession().setAttribute("user", resultInfo.getResult());
            // 判断用户是否记住密码
            String rem = request.getParameter("rem");
            // 如果是，将用户姓名与密码存到cookie中
            if("1".equals(rem)) {
                Cookie cookie = new Cookie("user", userName + "-" + userPwd);
                cookie.setMaxAge(3*24*60*60);
                response.addCookie(cookie);
            } else {
                Cookie cookie = new Cookie("user", null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }

            //重定向跳转到index
            response.sendRedirect("index");

        } else {
            // 将resultInfo放到request作用域中
            request.setAttribute("resultInfo", resultInfo);
            // 请求转发跳转到登陆页面
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void userLogOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.getSession().invalidate();

        Cookie cookie = new Cookie("user", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.sendRedirect("login.jsp");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
