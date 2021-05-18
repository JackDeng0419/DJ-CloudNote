package com.jack.CloudNote.web;

import cn.hutool.core.util.StrUtil;
import com.jack.CloudNote.po.Note;
import com.jack.CloudNote.po.NoteType;
import com.jack.CloudNote.po.User;
import com.jack.CloudNote.service.NoteService;
import com.jack.CloudNote.service.NoteTypeService;
import com.jack.CloudNote.vo.ResultInfo;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "NoteServlet", value = "/note")
public class NoteServlet extends HttpServlet {

    NoteService noteService = new NoteService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("menu_page", "note");
        String actionName = request.getParameter("actionName");
        if ("view".equals(actionName)) {
            //进入发布云记页面
            view(request, response);
        } else if ("addOrUpdate".equals(actionName)) {
            // 添加或修改云记
            addOrUpdate(request, response);
        } else if ("detail".equals(actionName)) {
            // 添加或修改云记
            detail(request, response);
        }
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // 1. 接收参数 （noteId）
        String noteId = request.getParameter("noteId");
        // 2. 调用Service层的查询方法，返回Note对象
        Note note = noteService.findNoteById(noteId);
        // 3. 将Note对象设置到request请求域中
        request.setAttribute("note", note);
        // 4. 设置首页动态包含的页面值
        request.setAttribute("changePage","note/detail.jsp");
        // 5. 请求转发跳转到index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 1. 接收参数 （类型ID、标题、内容）
        String typeId = request.getParameter("typeId");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // 获取经纬度
        String lon = request.getParameter("lon");
        String lat = request.getParameter("lat");

        // 如果是修改操作，需要接收noteId
        String noteId = request.getParameter("noteId");

        // 2. 调用Service层方法，返回resultInfo对
        ResultInfo<Note> resultInfo = noteService.addOrUpdate(typeId, title, content);

        if(resultInfo.getCode() == 1) { // success add or update
            // redirect to index
            response.sendRedirect("index");
        } else {
            // dispatch to view page
            request.setAttribute("resultInfo", resultInfo);
            String url = "note?actionName=view";
            // 如果是修改操作，需要传递noteId
            if (!StrUtil.isBlank(noteId)) {
                url += "&noteId="+noteId;
            }
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    private void view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* 修改操作 */
        // 得到要修改的云记ID
        String noteId = request.getParameter("noteId");
        // 通过noteId查询云记对象
        Note note = noteService.findNoteById(noteId);
        // 将note对象设置到请求域中
        request.setAttribute("noteInfo", note);
        /* 修改操作 */


        // 1. 从Session对象中获取用户对象
        User user = (User) request.getSession().getAttribute("user");
        // 2. 通过用户ID查询对应的类型列表
        List<NoteType> typeList = new NoteTypeService().findTypeList(user.getUserId());
        // 3. 将类型列表设置到request请求域中
        request.setAttribute("typeList", typeList);


        request.setAttribute("changePage", "note/view.jsp");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
