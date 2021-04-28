<%--
  Created by IntelliJ IDEA.
  User: jackdeng
  Date: 2021/4/26
  Time: 1:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<div class="col-md-9">


<%--    <script>--%>
<%--        function checkUser(){--%>
<%--            var nickName=$('#nick').val();--%>
<%--            if(nickName.length==0){--%>
<%--                $("#msg").html("昵称不能为空,请核对！");--%>
<%--                return false;--%>
<%--            }--%>
<%--            return true;--%>
<%--        }--%>
<%--    </script>--%>
    <div class="data_list">
        <div class="data_list_title"><span class="glyphicon glyphicon-edit"></span>&nbsp;个人中心 </div>
        <div class="container-fluid">
            <div class="row" style="padding-top: 20px;">
                <div class="col-md-8">
                    <form class="form-horizontal" method="post" action="user" enctype="multipart/form-data">
                        <div class="form-group">
                            <input type="hidden" name="actionName" value="updateUser">
                            <label for="nickName" class="col-sm-2 control-label">昵称:</label>
                            <div class="col-sm-3">
                                <input class="form-control" name="nick" id="nickName" placeholder="昵称" value="${user.nick}">
                            </div>
                            <label for="img" class="col-sm-2 control-label">头像:</label>
                            <div class="col-sm-5">
                                <input type="file" id="img" name="img">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="mood" class="col-sm-2 control-label">心情:</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" name="mood" id="mood" rows="3">${user.mood}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="submit" id="btn" class="btn btn-success" onclick="return updateUser();">修改</button>&nbsp;&nbsp;<span style="color:red ; font-size: 12px;" id="msg"></span>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-4"><img style="width:240px;height:180px" src="user?actionName=userHead&imageName=${user.head}"></div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        $("#nickName").blur(function(){
            //1. 获得输入的值
            var nickName = $("#nickName").val();
            //2. 判断值是否为空
            if(isEmpty(nickName)) {
                //禁用按钮，显示提示信息
                $("#btn").prop("disabled", true)
                $("#msg").html("用户昵称不能为空"); // 整个页面只有一处msg，所以暂时不用管
                return;
            }
            //3. 判断昵称是否修改
            //从session作用域中获取用户昵称
            var nick = '${user.nick}';
            if(nickName == nick) {
                return;
            }
            // 4. 如果昵称做了修改, 发送ajax请求后台，验证昵称是否可用
            $.ajax({
                type:"get",
                url:"user",
                data:{
                    actionName:"checkNick",
                    nick:nickName
                },
                success:function (code) {
                    if(code == 1) {
                        //清空提示信息，按钮可用
                        $("#msg").html("")
                        $("#btn").prop("disabled", false);
                    } else {
                        $("#msg").html("该用户已存在，请重新输入")
                        $("#btn").prop("disabled", false);
                    }
                }
            });

        }).focus(function () {
            // 1. 清空提示信息
            $("#msg").html("")
            // 2. 按钮可用
            $("#btn").prop("disabled", false);
        });

        function updateUser() {

            // 获取昵称文本框的值
            var nickName = $("#nickName").val();
            // 判断值是否为空
            if (isEmpty(nickName)) {
                //  如果为空，提示用户，禁用按钮，并return
                $("#msg").html("用户昵称不能为空！")
                $("#btn").prop("disabled", true);
                return false;
            }

            // 唯一性 TODO

            return true;
        }
    </script>
</div>
