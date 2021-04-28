package com.jack.CloudNote.service;

import cn.hutool.core.util.StrUtil;
import com.jack.CloudNote.dao.NoteTypeDao;
import com.jack.CloudNote.po.NoteType;
import com.jack.CloudNote.vo.ResultInfo;
import com.sun.tools.corba.se.idl.constExpr.Not;

import java.util.List;

public class NoteTypeService {
    private NoteTypeDao typeDao = new NoteTypeDao();

    public List<NoteType> findTypeList(Integer id) {
        List<NoteType> list = typeDao.findTypeByUserId(id);
        return list;
    }

    public ResultInfo<NoteType> deleteType(String typeId) {
        ResultInfo<NoteType> resultInfo = new ResultInfo<>();

        // 判断id是否为空
        if(StrUtil.isBlank(typeId)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("系统异常，请重试");
            return resultInfo;
        }

        // 调用DAO层，查询此类别id的笔记数量
        long noteCount = typeDao.findNoteCountByTypeId(typeId);

        // 如果笔记数量大于0，则不能删除
        if(noteCount > 0) {
            resultInfo.setCode(0);
            resultInfo.setMsg("该类型存在子记录，不可删除");
            return resultInfo;
        }

        // 如果笔记数量为0，调用dao层方法，通过id删除对应的类别，返回受影响的函数
        int rows = typeDao.deleteTypeById(typeId);
        if(rows > 0) {
            resultInfo.setCode(1);
            resultInfo.setMsg("");
            return resultInfo;
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("删除失败");
            return resultInfo;
        }

        //return resultInfo;
    }

    public ResultInfo<Integer> addOrUpdate(String typeName, String typeId, Integer userId) {
        System.out.println(typeId);
        ResultInfo<Integer> resultInfo = new ResultInfo<>();
        // 1. 判断参数是否为空 （类型名称）
        if (StrUtil.isBlank(typeName)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称不能为空！");
            return resultInfo;
        }
        // 2. 验证此类型名是否在用户内冲突
        // 2.1 从types表中查询此用户id且名字为typeName的项的数量
        Integer code = typeDao.checkTypeName(typeName, userId, typeId);
        if (code == 0) {
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称已存在，请重新输入");
            return resultInfo;
        }

        // 3. 判断类型id是否为空
        Integer key = null;

        if (StrUtil.isBlank(typeId)) {
            key = typeDao.addType(typeName, userId);
        } else {
            key = typeDao.updateType(typeName, typeId);
        }

        // 4. 如果key（受影响函数）> 0
        if(key > 0) {
            resultInfo.setCode(1);
            resultInfo.setResult(key);
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败");
        }
        return resultInfo;
    }
}
