<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>学生管理页面</title>
</head>
<body>
<div class="hrms_container">
    <!-- 导航条 -->
    <%@ include file="./commom/head.jsp"%>

    <!-- 中间部分（包括左边栏和员工/部门表单显示部分） -->
    <div class="hrms_body" style="position:relative; top:-15px;">

        <!-- 左侧栏 -->
        <%@ include file="commom/teaLeftsidebar.jsp"%>

        <!-- 中间员工表格信息展示内容 -->
        <div class="stu_info col-sm-10">

            <div class="panel panel-success">
                <!-- 路径导航 -->
                <div class="panel-heading">
                    <ol class="breadcrumb">
                        <li><a href="#">课设板块</a></li>
                        <li class="active">学生信息</li>
                    </ol>
                </div>
                <!-- Table -->
                <table class="table table-bordered table-hover" id="stu_table">
                    <thead>
                    <th>序号</th>
                    <th>学号</th>
                    <th>姓名</th>
                    <th>班级</th>
                    <th>专业</th>
                    <th>院系</th>
                    </thead>
                    <tbody>
                    <c:forEach items="${students}" var="stu">
                        <tr>
                            <td></td>
                            <td>${stu.stuId}</td>
                            <td>${stu.stuName}</td>
                            <td>${stu.stuClass}</td>
                            <td>${stu.stuMajor}</td>
                            <td>${stu.stuDept}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div><!-- /.panel panel-success -->
        </div><!-- /.emp_info -->
        <!-- 尾部 -->
        <%--<%@ include file="./commom/foot.jsp"%>--%>
    </div><!-- /.hrms_body -->
</div><!-- /.container -->

<script>
    $(function(){
        //$('table tr:not(:first)').remove();
        var len = $('table tr').length;
        for(var i = 1;i<len;i++){
            $('table tr:eq('+i+') td:first').text(i);
        }
    });
</script>
</body>

</html>
