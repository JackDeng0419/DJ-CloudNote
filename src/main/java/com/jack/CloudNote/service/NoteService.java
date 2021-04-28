package com.jack.CloudNote.service;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.StrUtil;
import com.jack.CloudNote.dao.NoteDao;
import com.jack.CloudNote.po.Note;
import com.jack.CloudNote.vo.ResultInfo;

public class NoteService {
    NoteDao noteDao = new NoteDao();

    public ResultInfo<Note> addOrUpdate(String typeId, String title, String content) {
        ResultInfo<Note> resultInfo = new ResultInfo<>();
        // 1. the empty check of the parameters
        if(StrUtil.isBlank(typeId)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("请选择云记类型");
        }
        if(StrUtil.isBlank(title)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("云记标题不能为空");
        }
        if(StrUtil.isBlank(content)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("云记内容不能为空");
        }

        // 2. set the resultInfo
        Note note = new Note();
        note.setTypeId(Integer.parseInt(typeId));
        note.setTitle(title);
        note.setContent(content);
        resultInfo.setResult(note);

        // 3. call the dao layer
        int row = noteDao.addOrUpdate(note);

        // 4. check the infected row
        if(row > 0) {
            resultInfo.setCode(1);
            resultInfo.setMsg("");
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("添加错误，请重试");
            resultInfo.setResult(note);
        }

        return resultInfo;


    }
}
