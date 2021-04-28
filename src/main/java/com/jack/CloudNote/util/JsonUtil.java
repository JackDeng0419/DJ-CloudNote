package com.jack.CloudNote.util;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonUtil {
    public static void toJson(HttpServletResponse response, Object result) {
        try {
            // 设置response为json格式
            response.setContentType("application/json;charset=UTF-8");
            // 得到字符串输出流
            PrintWriter printWriter = response.getWriter();
            // 转成json string
            String json = JSON.toJSONString(result);
            printWriter.write(json);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
