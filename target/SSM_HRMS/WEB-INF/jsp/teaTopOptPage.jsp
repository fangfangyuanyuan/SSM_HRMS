<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>课程信息</title>
</head>
<body>
<div class="hrms_course_container">
    <!-- 导航栏-->
    <%@ include file="./commom/head.jsp"%>


    <!-- 中间部分（左侧栏+表格内容） -->
    <div class="hrms_course_body">
        <!-- 左侧栏 -->
        <%@ include file="./commom/teaLeftsidebar.jsp"%>

        <!-- 课程设计表格内容 -->
        <div class="course_info col-sm-10">
            <div class="panel panel-success">
                <!-- 路径导航 -->
                <div class="panel-heading">
                    <ol class="breadcrumb">
                        <li><a href="#">课设板块</a></li>
                        <li class="active">选题情况</li>
                    </ol>
                </div>
                <!-- Table -->
                <table class="table table-bordered table-hover" id="course_table">
                    <thead>
                    <th>序号</th>
                    <th hidden>选题编号</th>
                    <th>课题名称</th>
                    <th>学生名字</th>
                    <th>学生专业</th>
                    <th>学生班级</th>
                    <th>操作</th>
                    </thead>
                    <tbody>
                    <c:forEach items="${groups}" var="gro">
                        <tr>
                            <td></td>
                            <td hidden>${gro.groId}</td>
                            <td>${gro.topName}</td>
                            <td>${gro.stuName}</td>
                            <td>${gro.stuMajor}</td>
                            <td>${gro.stuClass}</td>
                            <td>
                                <a href="#" role="button" class="btn btn-danger opt_delete_btn">删除</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <div class="panel-body">
                    <div class="modal-footer">
                        <a href="#" style="color:blue;text-decoration:none;" class="opt_import">导出选题表</a>
                    </div>
                    <%--<div class="table_items">--%>
                       <%----%>
                    <%--</div>--%>
                </div>
            </div><!-- /.panel panel-success -->
        </div><!-- /.course_info -->
    </div><!-- /.hrms_course_body -->

    <!-- 尾部-->
    <%--<%@ include file="./commom/foot.jsp"%>--%>

</div><!-- /.hrms_course_container -->

<script type="text/javascript">
       $(function(){
        //$('table tr:not(:first)').remove();
        var len = $('table tr').length;
        for(var i = 1;i<len;i++){
            $('table tr:eq('+i+') td:first').text(i);
        }
    });

    //导出成绩报表
    $(".opt_import").click(function () {
        $(this).attr("href", "/hrms/group/importOpt?curId=${curId}");
    });

    // <!-- ==========================学生选题删除操作=================================== -->
    $(".opt_delete_btn").click(function () {
              var delgroId = $(this).parent().parent().find("td:eq(1)").text();
        var delStuName = $(this).parent().parent().find("td:eq(3)").text();
        if (confirm("确认删除【"+delStuName+"】的选题信息吗？")){
            $.ajax({
                url:"/hrms/group/delOpt/"+delgroId,
                type:"PUT",
                success:function (result) {
                    if (result.code == 100){
                        alert("删除成功！");
                        window.location.href="/hrms/group/getOptTopByTea?curId=${curId}";
                    }else {
                        alert(result.extendInfo.opt_del_error);
                    }
                }
            });
        }
    });
</script>
</body>
</html>

