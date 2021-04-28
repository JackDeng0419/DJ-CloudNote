package com.jack.CloudNote.filter;

import com.jack.CloudNote.po.User;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class LoginAccessFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI(); //格式：项目路径/资源路径

        // 1. 指定页面放行
        if(path.contains("/login.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. 静态资源放行
        if(path.contains("/statics")) {
            chain.doFilter(request, response);
            return;
        }

        // 3. 指定行为放行
        if(path.contains("/user")) {
            String actionName = request.getParameter("actionName");
            if("login".equals(actionName)) {
                chain.doFilter(request,response);
                return;
            }
        }

        //  4. 登陆状态放行
        User user = (User) request.getSession().getAttribute("user");
        if(user != null) {
            chain.doFilter(request,response);
            return;
        }

        /**
         * 如果上面的都不满足，还有一种可能，就是用户可以免登陆
         */
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0) {
            //遍历cookie
            for (Cookie cookie: cookies) {
                //如果cookie的名字为user
                if("user".equals(cookie.getName())) {
                    //获得cookie - user的值
                    String value = cookie.getValue();
                    //将user的值分成用户名和密码
                    String[] val = value.split("-");
                    //获得用户名和密码
                    String userName = val[0];
                    String userPwd = val[1];
                    //将此请求转发到登陆操作，并带上用户名和密码作为参数
                    String url = "user?actionName=login&rem=1&userName=" + userName + "&userPwd="+userPwd;
                    request.getRequestDispatcher(url).forward(request, response);
                    return;
                }
            }
        }


        response.sendRedirect("login.jsp");
    }
}
