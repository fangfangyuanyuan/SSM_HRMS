<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>选题结果</title>
</head>
<body>
<div class="hrms_course_container">
    <!-- 导航栏-->
    <%@ include file="./commom/head.jsp"%>


    <!-- 中间部分（左侧栏+表格内容） -->
    <div class="hrms_course_body">
        <!-- 左侧栏 -->
        <%@ include file="./commom/stuLeftsidebar.jsp"%>

        <!-- 课程设计表格内容 -->
        <div class="course_info col-sm-10">
            <div class="panel panel-success">
                <!-- 路径导航 -->
                <div class="panel-heading">
                    <ol class="breadcrumb">
                        <li><a href="#">课题板块</a></li>
                        <li class="active">选题结果</li>
                    </ol>
                </div>
                <!-- Table -->
                <table class="table table-bordered table-hover" id="course_table">
                    <thead>
                    <th>序号</th>
                    <th>课程名称</th>
                    <th>学年</th>
                    <th>指导教师</th>
                    <th>课题名称</th>
                    <th>审核结果</th>
                    </thead>
                    <tbody>
                    <c:forEach items="${groups}" var="gro">
                        <tr>
                            <td></td>
                            <td>${gro.couName}</td>
                            <td>${gro.curYear}</td>
                            <td>${gro.teaName}</td>
                            <td>${gro.topName}</td>
                            <td>${gro.optResult}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <%--<div class="panel-body">--%>
                    <%--<div class="table_items">--%>
                    <%--总记录数<span class="badge">${totalItems}</span>条。--%>
                    <%--</div>--%>
                <%--</div>--%>
            </div><!-- /.panel panel-success -->
        </div><!-- /.course_info -->
    </div><!-- /.hrms_course_body -->

    <!-- 尾部-->
    <%--<%@ include file="./commom/foot.jsp"%>--%>

</div><!-- /.hrms_course_container -->

<script type="text/javascript">
    $(function(){
        var len = $('table tr').length;
        for(var i = 1;i<len;i++){
            $('table tr:eq('+i+') td:first').text(i);
        }
    });
</script>
</body>
</html>

