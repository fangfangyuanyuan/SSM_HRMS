<%@ page import="com.hrms.bean.Student" %>
<%@ page import="com.hrms.bean.Teacher" %><%--
  Created by IntelliJ IDEA.
  User: me
  Date: 2019/4/29
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>个人信息</title>
</head>
<body>
<div class="modal fade user_get_modal" tabindex="-1" role="dialog" aria-labelledby="user_get_modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">个人信息</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="sel_userId" class="col-sm-2 control-label">账号</label>
                        <div class="col-sm-8">
                            <input type="text" name="userId" class="form-control" id="sel_userId" readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="sel_userName" class="col-sm-2 control-label">名字</label>
                        <div class="col-sm-8">
                            <input type="text" name="userName" class="form-control" id="sel_userName" readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="sel_userMajor" class="col-sm-2 control-label">专业</label>
                        <div class="col-sm-8">
                            <input type="text" name="userMajor" class="form-control" id="sel_userMajor" readonly="readonly" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="sel_userClass" class="col-sm-2 control-label">班级</label>
                        <div class="col-sm-8">
                            <input type="text" name="userClass" class="form-control" id="sel_userClass" readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="sel_userDept" class="col-sm-2 control-label">学院</label>
                        <div class="col-sm-8">
                            <input type="text" name="userDept" class="form-control" id="sel_userDept" readonly="readonly">
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<script type="text/javascript">
    var role = "<%=session.getAttribute("role")%>";

    $(".user_get_btn").click(function () {
        if("T" == role){
            // alert("teacher");
            // val()内容是字符串时候要加引号，否则不显示
            <%--对象取值方式：${sessionScope.teacher.teaId}--%>
            $("#sel_userId").val('${sessionScope.teacher.teaId}');
            $("#sel_userName").val('${sessionScope.teacher.teaName}');
            $("#sel_userMajor").val('${sessionScope.teacher.teaMajor}');
            $("#sel_userDept").val('${sessionScope.teacher.teaDept}');
        }
        if("S"== role){
            // alert("student");
            $("#sel_userId").val('${sessionScope.student.stuId}');
            $("#sel_userName").val('${sessionScope.student.stuName}');
            $("#sel_userMajor").val('${sessionScope.student.stuMajor}');
            $("#sel_userDept").val('${sessionScope.student.stuDept}');
            $("#sel_userClass").val('${sessionScope.student.stuClass}');
        }
    });
</script>
</body>
</html>
