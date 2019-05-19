<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>成绩分析页面</title>
</head>
<body>
<div class="hrms_course_container">
    <!-- 导航栏-->
    <%@ include file="./commom/head.jsp"%>


    <!-- 中间部分（左侧栏+表格内容） -->
    <div class="hrms_course_body">
        <!-- 左侧栏 -->
        <%@ include file="./commom/adminLeftsidebar.jsp"%>

        <!-- 部门表格内容 -->
        <div class="course_info col-sm-10">
            <div class="panel panel-success">
                <!-- 路径导航 -->
                <div class="panel-heading">
                    <ol class="breadcrumb">
                        <li><a href="#">成绩板块</a></li>
                        <li class="active">成绩排名</li>
                    </ol>
                </div>
                <!-- Table -->
                <table class="table table-bordered table-hover" id="course_table">
                    <thead>
                    <th>排名</th>
                    <th>学号</th>
                    <th>姓名</th>
                    <th>专业班级</th>
                    <th>目标一</th>
                    <th>平均</th>
                    <th>目标二</th>
                    <th>平均</th>
                    <th>目标三</th>
                    <th>平均</th>
                    <th>成绩</th>
                    <th>平均</th>
                    </thead>
                    <tbody>
                    <c:forEach items="${groups}" var="gro">
                        <tr>
                            <td></td>
                            <td>${gro.stuId}</td>
                            <td>${gro.stuName}</td>
                            <td>${gro.stuMajor}${gro.stuClass}</td>
                            <td>${gro.score1}</td>
                            <td>${gro.avg1}</td>
                            <td>${gro.score2}</td>
                            <td>${gro.avg2}</td>
                            <td>${gro.score3}</td>
                            <td>${gro.avg3}</td>
                            <td>${gro.total}</td>
                            <td>${gro.avgTotal}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <div class="panel-body">
                    <div class="modal-footer">
                        <a href="#" style="color:blue;text-decoration:none;" class="sco_import">导出报表</a>
                    </div>
                </div>
            </div><!-- /.panel panel-success -->
        </div><!-- /.course_info -->
    </div><!-- /.hrms_course_body -->

    <%--<%@ include file="adminCourseAdd.jsp"%>--%>
    <%@ include file="adminCourseUpdate.jsp"%>

    <!-- 尾部-->
    <%--<%@ include file="./commom/foot.jsp"%>--%>

</div><!-- /.hrms_course_container -->
<%@ include file="adminCourseAdd.jsp"%>
<script type="text/javascript">
    $(function(){
        //$('table tr:not(:first)').remove();
        var len = $('table tr').length;
        for(var i = 1;i<len;i++){
            $('table tr:eq('+i+') td:first').text(i);
        }
    });

    //导出成绩报表
    $(".sco_import").click(function () {
            $(this).attr("href", "/hrms/group/importSco?curName=${curName}");
    });


</script>
</body>
</html>
