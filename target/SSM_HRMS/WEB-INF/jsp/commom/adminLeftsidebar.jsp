<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理员主页</title>
</head>
<body>
<div class="panel-group col-sm-2" id="hrms_sidebar_left" role="tablist" aria-multiselectable="true">
    <ul class="nav nav-pills nav-stacked file_sidebar">
        <li role="presentation" class="active">
            <%--将.file-upload-modal这个Dom元素的内容以模态框的形式展示。--%>
            <a href="#"  data-toggle="collapse" data-target="#collapse_course">
                <span class="glyphicon glyphicon-cloud" aria-hidden="true">导入板块</span>
            </a>
            <ul class="nav nav-pills nav-stacked" id="collapse_course">
                <li role="presentation"><a href="#" class="admin-upload-btn" data-toggle="modal" data-target=".file-upload-modal">文件上传</a></li>
                <%--<li role="presentation"><a href="#" class="desi_clearall_btn">课程设计清除</a></li>--%>
            </ul>
        </li>
    </ul>

    <ul class="nav nav-pills nav-stacked cou_sidebar">
        <li role="presentation" class="active">
            <a href="#" data-toggle="collapse" data-target="#collapse_cou">
                <span class="glyphicon glyphicon-cloud" aria-hidden="true">课程板块</span>
            </a>
            <ul class="nav nav-pills nav-stacked" id="collapse_cou">
                <li role="presentation"><a href="#" class="cou_info">课程信息</a></li>
                <%--<li role="presentation"><a href="#" role="button" class="cou_add_btn" data-toggle="modal" data-target=".course-add-modal">课程添加</a></li>--%>
                <%--<li role="presentation"><a href="#" class="emp_clearall_btn">课程清除</a></li>--%>
            </ul>
        </li>
    </ul>
    <ul class="nav nav-pills nav-stacked tea_sidebar">
        <li role="presentation" class="active">
            <a href="#"  data-toggle="collapse" data-target="#collapse_tea">
                <span class="glyphicon glyphicon-user" aria-hidden="true">教师板块</span>
            </a>
            <ul class="nav nav-pills nav-stacked" id="collapse_tea">
                <li role="presentation"><a href="#" class="tea_info">教师信息</a></li>
                <%--<li role="presentation"><a href="#" class="tea_add_btn" data-toggle="modal" data-target=".tea-add-modal">教师新增</a></li>--%>
                <%--<li role="presentation"><a href="#" class="dept_clearall_btn">部门清除</a></li>--%>
            </ul>
        </li>
    </ul>
    <ul class="nav nav-pills nav-stacked stu_sidebar">
        <li role="presentation" class="active">
            <%--将#collapse_design这个Dom元素的内容以模态框的形式展示。--%>
            <a href="#"  data-toggle="collapse" data-target="#collapse_stu">
                <span class="glyphicon glyphicon-user" aria-hidden="true">学生板块</span>
            </a>
            <ul class="nav nav-pills nav-stacked" id="collapse_stu">
                <li role="presentation"><a href="#" class="stu_info">学生信息</a></li>
                <%--<li role="presentation"><a href="#" class="stu_add_btn" data-toggle="modal" data-target=".stu-add-modal">学生新增</a></li>--%>
                <%--<li role="presentation"><a href="#" class="desi_clearall_btn">课程设计清除</a></li>--%>
            </ul>
        </li>
    </ul>
    <ul class="nav nav-pills nav-stacked tea_sidebar">
        <li role="presentation" class="active">
            <a href="#"  data-toggle="collapse" data-target="#collapse_group">
                <span class="glyphicon glyphicon-user" aria-hidden="true">分组板块</span>
            </a>
            <ul class="nav nav-pills nav-stacked" id="collapse_group">
                <li role="presentation"><a href="#" class="cou_tea_btn" data-toggle="modal" data-target=".cou-tea-modal">课程教师分组</a></li>
                <li role="presentation"><a href="#" class="tea_stu_btn" data-toggle="modal" data-target=".tea-stu-modal">教师学生分组</a></li>
                <%--<li role="presentation"><a href="#" class="dept_clearall_btn">部门清除</a></li>--%>
            </ul>
        </li>
    </ul>
    <ul class="nav nav-pills nav-stacked score_sidebar">
        <li role="presentation" class="active">
            <%--将#collapse_design这个Dom元素的内容以模态框的形式展示。--%>
            <a href="#"  data-toggle="collapse" data-target="#collapse_score">
                <span class="glyphicon glyphicon-cloud" aria-hidden="true">成绩板块</span>
            </a>
            <ul class="nav nav-pills nav-stacked" id="collapse_score">
                <li role="presentation"><a href="#" class="sco_get_btn" data-toggle="modal" data-target=".sco-get-modal">成绩排名</a></li>
                <li role="presentation"><a href="#" class="graph_analysis_btn" data-toggle="modal" data-target=".graph-analysis-modal">图表分析</a></li>
                <%--<li role="presentation"><a href="#" class="desi_clearall_btn">课程设计清除</a></li>--%>
            </ul>
        </li>
    </ul>

</div><!-- /.panel-group，#hrms_sidebar_left -->
<%@ include file="../adminUpload.jsp"%>
<%--<%@ include file="../adminCourseAdd.jsp"%>--%>
<%--<%@ include file="../adminStudentAdd.jsp"%>--%>
<%--<%@ include file="../adminTeacherAdd.jsp"%>--%>
<%@ include file="../adminCurListOfTea.jsp"%>
<%@ include file="../adminCurListOfStu.jsp"%>
<%@ include file="../adminCurListOfSco.jsp"%>
<%@ include file="../adminCurListOfGraph.jsp"%>
<script type="text/javascript">
    //跳转到课程页面
    $(".cou_info").click(function () {
        $(this).attr("href", "/hrms/course/getCouList");
    });
    //跳转到教师页面
    $(".tea_info").click(function () {
        $(this).attr("href", "/hrms/tea/getTeaList");
    });
    //跳转到学生页面
    $(".stu_info").click(function () {
        $(this).attr("href", "/hrms/student/getStuList");
    });

    // $(".sco_get_btn").click(function () {
    //     $(this).attr("href", "../adminScorePage.jsp");
    // });
</script>
</body>
</html>
