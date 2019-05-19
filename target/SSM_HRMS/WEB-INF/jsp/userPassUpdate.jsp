<%--
  Created by IntelliJ IDEA.
  User: me
  Date: 2019/4/29
  Time: 15:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>密码修改</title>
</head>
<body>
<div class="modal fade pass-edit-modal" tabindex="-1" role="dialog" aria-labelledby="pass-edit-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">密码修改</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal update_tea_form">
                    <div class="form-group">
                        <label for="update_pass1" class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-8">
                            <input type="password" name="pass" class="form-control" id="update_pass1">
                            <span id="pass1"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_pass2" class="col-sm-2 control-label">确认密码</label>
                        <div class="col-sm-8">
                            <input type="password" name="pass1" class="form-control" id="update_pass2">
                            <span id="pass2"></span>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary pass_update_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->



</body>
<script type="text/javascript">
    var role = "<%=session.getAttribute("role")%>";
    // alert(role);

    function  password(){
        if(pass1.length < 6|| pass1.length > 16 ){
            alert("密码由6-16位字母数字组成！");
        }
    }

    function password2() {

    }
    $(".pass_update_btn").click(function () {
        var pass1  = $("#update_pass1").val();
        var pass2  = $("#update_pass2").val();
        var reg = /^[\w]{6,12}$/
        if(!pass1.match(reg)){
            alert("密码由6-16位字母数字组成！");
                return;
        }
        if(pass1 != pass2){
            alert("两次密码不一致！");
            return;
        }
        if("T" == role) {
              var teaData = "<%=(Teacher)session.getAttribute("teacher")%>";
              $.ajax({
                        url: "/hrms/tea/updateTeaPass/" + pass1,
                        type: "PUT",
                        success: function (result) {
                            if (result.code == 100) {
                                alert("更新成功！");
                                $('.pass-edit-modal').modal("hide");
                            } else {
                                alert(result.extendInfo.pass_edit_error);
                            }
                        }

                    });
                }
                if("S"== role){
                    var stu = "<%=(Student)session.getAttribute("student")%>";
                    $.ajax({
                        url: "/hrms/student/updateStuPass/" + pass1,
                        type: "PUT",
                        success: function (result) {
                            if (result.code == 100) {
                                alert("更新成功！");
                                $('.pass-edit-modal').modal("hide");
                            } else {
                                alert(result.extendInfo.pass_edit_error);
                            }
                        }

                    });
                }
    });
</script>
</html>
