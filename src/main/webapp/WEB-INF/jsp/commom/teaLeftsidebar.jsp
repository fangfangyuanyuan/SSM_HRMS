<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>教师主页</title>
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
    <ul class="nav nav-pills nav-stacked topic_sidebar">
        <li role="presentation" class="active">
            <a href="#"  data-toggle="collapse" data-target="#collapse_topic">
                <span class="glyphicon glyphicon-cloud" aria-hidden="true">课设板块</span>
            </a>
            <ul class="nav nav-pills nav-stacked" id="collapse_topic">
                <li role="presentation"><a href="#"  class="curId_get_btn" data-toggle="modal" data-target=".curId-get-modal">课设信息</a></li>
                <%--<li role="presentation"><a href="#" class="topic_add_btn" data-toggle="modal" data-target=".topic-add-modal">课题导入</a></li>--%>
            </ul>
        </li>
    </ul>
    <ul class="nav nav-pills nav-stacked teacher_sidebar">
        <li role="presentation" class="active">
            <a href="#"  data-toggle="collapse" data-target="#collapse_teacher">
                <span class="glyphicon glyphicon-user" aria-hidden="true">个人中心</span>
            </a>
            <ul class="nav nav-pills nav-stacked" id="collapse_teacher">
                <li role="presentation"><a href="#"  class="user_get_btn" data-toggle="modal" data-target=".user_get_modal">个人信息</a></li>
                <li role="presentation"><a href="#" class="pass-edit_btn" data-toggle="modal" data-target=".pass-edit-modal">修改密码</a></li>
            </ul>
        </li>
    </ul>

</div><!-- /.panel-group，#hrms_sidebar_left -->
<%--<%@ include file="../teaTopicUpload.jsp"%>--%>
<%@ include file="../teaCurList.jsp"%>
<%@ include file="../userInfo.jsp"%>
<%@ include file="../userPassUpdate.jsp"%>
<script type="text/javascript">
    //跳转到课程页面
    $(".cou_info").click(function () {
        $(this).attr("href", "/hrms/cur/getCouListByTea");
    });
</script>
</body>
</html>
