package com.jack.CloudNote.web;


import com.alibaba.fastjson.JSON;
import com.jack.CloudNote.po.NoteType;
import com.jack.CloudNote.po.User;
import com.jack.CloudNote.service.NoteTypeService;
import com.jack.CloudNote.util.JsonUtil;
import com.jack.CloudNote.vo.ResultInfo;
import com.sun.tools.javap.TypeAnnotationWriter;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "TypeServlet", value = "/type")
public class NoteTypeServlet extends HttpServlet {
    private NoteTypeService noteTypeService = new NoteTypeService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("menu_page", "type");
        String actionName = request.getParameter("actionName");
        if ("type".equals(actionName)) {
            typeList(request, response);
        } else if ("delete".equals(actionName)) {
            //System.out.println("delete here");
            deleteType(request, response);
        } else if ("addOrUpdate".equals(actionName)) {
            //System.out.println("delete here");
            addOrUpdate(request, response);
        }
    }

    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        String typeName = request.getParameter("typeName");
        String typeId = request.getParameter("typeId");
        User user = (User) request.getSession().getAttribute("user");
        ResultInfo<Integer> resultInfo = noteTypeService.addOrUpdate(typeName, typeId, user.getUserId());
        JsonUtil.toJson(response, resultInfo);
    }

    private void deleteType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //接收参数，类型id
        String typeId = request.getParameter("typeId");
        //System.out.println(typeId);
        //调用service方法，删除此类型，返回resultInfo
        ResultInfo<NoteType> resultInfo = noteTypeService.deleteType(typeId);
        //System.out.println(resultInfo.getMsg());
        JsonUtil.toJson(response, resultInfo);
    }

    /**
     * 获得类型列表并返回给request作用域
     * @param request
     * @param response
     */
    private void typeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获得user对象
        User user = (User) request.getSession().getAttribute("user");
        // 根据userId获得typeList
        List<NoteType> typeList = noteTypeService.findTypeList(user.getUserId());
        // 将typeList 放到 request请求中并返回给index.jsp
        request.setAttribute("typeList", typeList);
        request.setAttribute("changePage", "type/list.jsp");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
