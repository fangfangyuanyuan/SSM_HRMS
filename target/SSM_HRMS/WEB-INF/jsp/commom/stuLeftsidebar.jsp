<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>学生主页</title>
</head>
<body>
<div class="panel-group col-sm-2" id="hrms_sidebar_left" role="tablist" aria-multiselectable="true">
    <ul class="nav nav-pills nav-stacked emp_sidebar">
        <li role="presentation" class="active">
            <a href="#" data-toggle="collapse" data-target="#collapse_cou">
                <span class="glyphicon glyphicon-cloud" aria-hidden="true">课程板块</span>
            </a>
            <ul class="nav nav-pills nav-stacked" id="collapse_cou">
                <li role="presentation"><a href="#" class="cou_info">课程信息</a></li>
            </ul>
        </li>
    </ul>
    <ul class="nav nav-pills nav-stacked top_sidebar">
        <li role="presentation" class="active">
            <a href="#"  data-toggle="collapse" data-target="#collapse_top">
                <span class="glyphicon glyphicon-cloud" aria-hidden="true">课题板块</span>
            </a>
            <ul class="nav nav-pills nav-stacked" id="collapse_top">
                <li role="presentation"><a href="#"  class="top_get_btn" data-toggle="modal" data-target=".top-opt-modal">选择课题</a></li>
                <li role="presentation"><a href="#" class="top_result">选题结果</a></li>
            </ul>
        </li>
    </ul>
    <ul class="nav nav-pills nav-stacked score_sidebar">
        <li role="presentation" class="active">
            <a href="#"  data-toggle="collapse" data-target="#collapse_score">
                <span class="glyphicon glyphicon-cloud" aria-hidden="true">成绩板块</span>
            </a>
            <ul class="nav nav-pills nav-stacked" id="collapse_score">
                <li role="presentation"><a href="#" class="score_info">成绩查询</a></li>
            </ul>
        </li>
    </ul>
    <%--个人中心管理--%>
    <ul class="nav nav-pills nav-stacked student_sidebar">
        <li role="presentation" class="active">
            <a href="#"  data-toggle="collapse" data-target="#collapse_student">
                <span class="glyphicon glyphicon-user" aria-hidden="true">个人中心</span>
            </a>
            <ul class="nav nav-pills nav-stacked" id="collapse_teacher">
                <li role="presentation"><a href="#" class="user_get_btn" data-toggle="modal" data-target=".user_get_modal">个人信息</a></li>
                <li role="presentation"><a href="#" class="pass_edit_btn" data-toggle="modal" data-target=".pass-edit-modal">修改密码</a></li>
            </ul>
        </li>
    </ul>

</div><!-- /.panel-group，#hrms_sidebar_left -->
<%--<%@ include file="../stuTopicAdd.jsp"%>--%>
<%@ include file="../stuCurList.jsp"%>
<%@ include file="../userInfo.jsp"%>
<%@ include file="../userPassUpdate.jsp"%>
<script type="text/javascript">
    //跳转到学生课程页面
    $(".cou_info").click(function () {
        $(this).attr("href", "/hrms/cur/getCouListByStu");
    });
    //跳转到成绩页面
    $(".score_info").click(function () {
        $(this).attr("href", "/hrms/group/getScoreByStu");
    });
    //跳转到选题结果页面
    $(".top_result").click(function () {
        $(this).attr("href", "/hrms/group/getOptResule");
    });
    //部门清零这个功能暂未实现
    $(".dept_clearall_btn").click(function () {
        alert("对不起，您暂无权限进行操作！请先获取权限");
    });
</script>
</body>
</html>
