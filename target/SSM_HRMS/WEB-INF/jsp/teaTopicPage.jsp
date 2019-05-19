<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>课题页面</title>
</head>
<body>
<div class="hrms_container">
    <!-- 导航条 -->
    <%@ include file="./commom/head.jsp"%>

    <!-- 中间部分（包括左边栏和员工/部门表单显示部分） -->
    <div class="hrms_body" style="position:relative; top:-15px;">

        <!-- 左侧栏 -->
        <%@ include file="commom/teaLeftsidebar.jsp"%>

        <!-- 中间课题表格信息展示内容 -->
        <div class="stu_info col-sm-10">

            <div class="panel panel-success">
                <!-- 路径导航 -->
                <div class="panel-heading">
                    <ol class="breadcrumb">
                        <li><a href="#">课设板块</a></li>
                        <li class="active">课题信息</li>
                    </ol>
                    <input type="hidden" name="curId" id="curId" value="${curId}" />
                </div>
                <div class="panel-body">
                    <div class="table_items">
                        该课程选题时间为:${startTime}-------${endTime}.
                    </div>
                </div>
                <table class="table table-bordered table-hover" id="stu_table">
                    <thead>
                    <th>序号</th>
                    <th hidden>课题编号</th>
                    <th>课题名称</th>
                    <th>课题性质</th>
                    <th>课题介绍</th>
                    <th>
                        <a href="#"  style="color:blue;text-decoration:none;" class="top_add_btn" data-toggle="modal" data-target=".top_add_modal">课题添加</a>
                    </th>
                    </thead>
                    <tbody>
                    <c:forEach items="${topics}" var="top">
                        <tr>
                            <td></td>
                            <td hidden>${top.topId}</td>
                            <td>${top.topName}</td>
                            <td>${top.topNature}</td>
                            <td>${top.topIntroduce}</td>
                            <td>
                                <a href="#" role="button" class="btn btn-primary top_edit_btn" data-toggle="modal" data-target=".top-update-modal">编辑</a>
                                <a href="#" role="button" class="btn btn-danger top_delete_dtn">删除</a>
                            </td>
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
<%@ include file="teaTopicAdd.jsp" %>
<%@ include file="teaTopicUpdate.jsp" %>
<script type="text/javascript">
    $(function(){
        //$('table tr:not(:first)').remove();
        var len = $('table tr').length;
        for(var i = 1;i<len;i++){
            $('table tr:eq('+i+') td:first').text(i);
        }
    });

    // <!-- ==========================课题删除操作=================================== -->
    $(".top_delete_dtn").click(function () {
        var delTopId = $(this).parent().parent().find("td:eq(1)").text();
        var delTopName = $(this).parent().parent().find("td:eq(2)").text();
        if (confirm("确认删除【" + delTopName+ "】的信息吗？")){
            $.ajax({
                url:"/hrms/topic/delTop/"+delTopId,
                type:"DELETE",
                success:function (result) {
                    if (result.code == 100){
                        alert("删除成功！");
                        window.location.href="/hrms/topic/getTopList?curId=${curId}";
                    }else {
                        alert(result.extendInfo.top_del_error);
                    }
                }
            });
        }
    });
</script>
</body>
</html>
