package com.jack.CloudNote.service;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.StrUtil;
import com.jack.CloudNote.dao.NoteDao;
import com.jack.CloudNote.po.Note;
import com.jack.CloudNote.util.Page;
import com.jack.CloudNote.vo.NoteVo;
import com.jack.CloudNote.vo.ResultInfo;

import java.util.List;

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

    /**
     * 1. 查询此用户有多少条笔记
     * 2. 通过当前第几页，每页显示多少页，总笔记数创建Page对象
     * 3. 调用dao层查询响应页面的笔记列表，传入page对象
     * 4. 将笔记列表set到Page对象里面，并返回page对象
     * @param pageNum
     * @param pageSize
     * @param userId
     * @param title
     * @param date
     * @param typeId
     * @return
     */
    public Page<Note> findNoteListByPage(String pageNumStr, String pageSizeStr, Integer userId, String title, String date, String typeId) {
        // 设置分页参数的默认值
        Integer pageNum = 1; // 默认当前页是第一页
        Integer pageSize = 5; // 默认每页显示5条数据
        // 1. 参数的非空校验 （如果参数不为空，则设置参数）
        if (!StrUtil.isBlank(pageNumStr)) {
            // 设置当前页
            pageNum = Integer.parseInt(pageNumStr);
        }
        if (!StrUtil.isBlank(pageSizeStr)) {
            // 设置每页显示的数量
            pageSize = Integer.parseInt(pageSizeStr);
        }

        // 2. 查询当前登录用户的云记数量，返回总记录数 （long类型）
        long count = noteDao.findNoteCount(userId, title, date, typeId);

        // 3. 判断总记录数是否大于0
        if (count < 1) {
            return null;
        }

        // 4. 如果总记录数大于0，调用Page类的带参构造，得到其他分页参数的值，返回Page对象
        Page<Note> page = new Page<>(pageNum, pageSize, count);

        // 得到数据库中分页查询的开始下标
        Integer index = (pageNum -1) * pageSize;

        // 5. 查询当前登录用户下当前页的数据列表，返回note集合
        List<Note> noteList = noteDao.findNoteListByPage(userId, index, pageSize, title, date, typeId);

        // 6. 将note集合设置到page对象中
        page.setDataList(noteList);

        // 7. 返回Page对象
        return page;

    }

    public List<NoteVo> findNoteCountByDate(Integer userId) {
        return noteDao.findNoteCountByDate(userId);
    }

    public List<NoteVo> findNoteCountByType(Integer userId) {
        return noteDao.findNoteCountByType(userId);
    }

    public Note findNoteById(String noteId) {
        // 1. 参数的非空判断
        if (StrUtil.isBlank(noteId)) {
            return null;
        }
        // 2. 调用Dao层的查询，通过noteId查询note对象
        Note note = noteDao.findNoteById(noteId);
        // 3. 返回note对象
        return note;
    }
}
