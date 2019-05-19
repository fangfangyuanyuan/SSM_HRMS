<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>自拟课题审核页面</title>
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
                        <li class="active">课题审核</li>
                    </ol>
                    <input type="hidden" name="curId" id="curId" value="${curId}" />
                </div>
                <!-- Table -->
                <table class="table table-bordered table-hover" id="stu_table">
                    <thead>
                    <th>序号</th>
                    <th hidden>课题编号</th>
                    <th>课题名称</th>
                    <th>课题性质</th>
                    <th>课题介绍</th>
                    <th>自拟人</th>
                    </thead>
                    <tbody>
                    <c:forEach items="${topics}" var="top">
                        <tr>
                            <td></td>
                            <td hidden>${top.topId}</td>
                            <td>${top.topName}</td>
                            <td>${top.topNature}</td>
                            <td>${top.topIntroduce}</td>
                            <td>${top.stuName}</td>
                            <td>
                                <a href="#" role="button" class="btn btn-primary review_suc_btn">同意</a>
                                <a href="#" role="button" class="btn btn-danger review_fail_dtn">拒绝</a>
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
<script type="text/javascript">
    var curId = ${curId};

    $(function(){
        //$('table tr:not(:first)').remove();
        var len = $('table tr').length;
        for(var i = 1;i<len;i++){
            $('table tr:eq('+i+') td:first').text(i);
        }
    });
    // <!-- ==========================课题审核通过操作=================================== -->
    $(".review_suc_btn").click(function () {
        var checkTopId = $(this).parent().parent().find("td:eq(1)").text();
       // alert(checkTopId);
        var topStatus = 1;

            $.ajax({
                url:"/hrms/topic/topReview?topId="+checkTopId+"&topStatus="+topStatus+"&curId="+curId,
                type:"PUT",
                success:function (result) {
                    if (result.code == 100){
                        alert("审核成功！");
                        window.location.href="/hrms/topic/getTopCheckList?curId="+curId;
                    }else {
                        alert(result.extendInfo.review_fail_error);
                    }
                }
            });
    });
    // <!-- ==========================课题审核不通过操作=================================== -->
    $ (".review_fail_dtn").click(function () {
        var checkTopId = $(this).parent().parent().find("td:eq(1)").text();
        var topStatus = -1;
        alert(checkTopId);
        $.ajax({
            url:"/hrms/topic/topReview?topId="+checkTopId+"&topStatus="+topStatus+"&curId="+curId,
            type:"PUT",
            success:function (result) {
                if (result.code == 100){
                    alert("审核成功！");
                    window.location.href="/hrms/topic/getTopCheckList?curId="+curId;
                }else {
                    alert(result.extendInfo.review_fail_error);
                }
            }
        });
    });
</script>
</body>
</html>
