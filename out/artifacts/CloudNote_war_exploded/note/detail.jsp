<%--
  Created by IntelliJ IDEA.
  User: jackdeng
  Date: 2021/4/28
  Time: 11:38 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-md-9">
    <div class="data_list">
        <div class="data_list_title">
            <span class="glyphicon glyphicon-eye-open"></span>&nbsp;查看云记
        </div>
        <div>


            <div class="note_title"><h2>123</h2></div>
            <div class="note_info">
                发布时间：『 2016-08-04 22:46:45』&nbsp;&nbsp;云记类别：语录
            </div>
            <div class="note_content">
                <p>adfasdffeaf</p>
            </div>
            <div class="note_btn">
                <button class="btn btn-primary" type="button" onclick="update(28)">修改</button>
                <button class="btn btn-danger" type="button" onclick="del(28)">删除</button>
            </div>



        </div>


    </div>

    <script>
        function update(data){
            window.location="note?noteId="+data;
        }

        function del(data){
            //使用sweet-alert
            swal({title: "删除提示",   //弹出框的title
                text: "确定删除吗？",  //弹出框里面的提示文本
                type: "warning",    //弹出框类型
                showCancelButton: true, //是否显示取消按钮
                confirmButtonColor: "#DD6B55",//确定按钮颜色
                cancelButtonText: "取消",//取消按钮文本
                confirmButtonText: "是的，确定删除！"//确定按钮上面的文档
            }).then(function(isConfirm) {
                if (isConfirm === true) {
                    window.location="note?act=del&noteId="+data;
                }
            });
        }
    </script>

</div>
