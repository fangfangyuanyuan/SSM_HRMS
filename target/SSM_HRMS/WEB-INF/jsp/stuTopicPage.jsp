<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%--<meta http-equiv ="refresh" content="5; url=/hrms/topic/getTopList?curId=${curId}">--%>
    <title>课题页面</title>
</head>
<body>
<div class="hrms_container">
    <!-- 导航条 -->
    <%@ include file="./commom/head.jsp"%>

    <!-- 中间部分（包括左边栏和员工/部门表单显示部分） -->
    <div class="hrms_body" style="position:relative; top:-15px;">

        <!-- 左侧栏 -->
        <%@ include file="commom/stuLeftsidebar.jsp"%>

        <!-- 中间课题表格信息展示内容 -->
        <div class="stu_info col-sm-10">

            <div class="panel panel-success">
                <!-- 路径导航 -->
                <div class="panel-heading">
                        <ol class="breadcrumb">
                            <li><a href="#">课题板块</a></li>
                            <li class="active">选择课题</li>
                            <%--<li  style="float:right;"><a href="#" class="top_draft_btn" data-toggle="modal" data-target=".draft-add-modal">自拟课题</a><li>--%>
                        </ol>
                </div>
                <!-- Table -->
                <input id="curId" value="${curId}" hidden/>
                <div class="panel-body">
                    <div class="table_items">
                        该课程选题时间为:${startTime}-------${endTime}.
                    </div>
                    <div class="table_items">
                        ${msg}
                    </div>
                </div>
                <div id="top_list_father">
                <table class="table table-bordered table-hover" id="stu_table">
                    <thead>
                    <th>序号</th>
                    <th hidden>课题编号</th>
                    <th>课题名称</th>
                    <th>课题性质</th>
                    <th>课题介绍</th>
                    <th hidden>选题状态</th>
                    <th>操作</th>
                    </thead>
                    <tbody id="top_list">
                    <c:forEach items="${topics}" var="top" varStatus="t">
                        <tr>
                            <td>${t.index+1}</td>
                            <td hidden>${top.topId}</td>
                            <td>${top.topName}</td>
                            <td>${top.topNature}</td>
                            <td>${top.topIntroduce}</td>
                            <td hidden>${top.topStatus}</td>
                            <td>
                                <button type="button"  class="btn btn-danger stu_choose_btn">确定</button>
                                <%--<a href="#" role="button" class="btn btn-danger stu_choose_btn">确定</a>--%>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                </div>
            </div><!-- /.panel panel-success -->
        </div><!-- /.emp_info -->
        <!-- 尾部 -->
    </div><!-- /.hrms_body -->
</div><!-- /.container -->
<%--<%@ include file="stuTopicAdd.jsp"%>--%>
<script type="text/javascript">
    $(function(){
        $("#top_list").find("tr").each(function(){
            var tdArr = $(this).children();
            var topStatus = tdArr.eq(5).text();
            // alert(topStatus);
            if(topStatus == 1)
                tdArr.eq(6).find('button').attr({"disabled":true});
            else
                tdArr.eq(6).find('button').attr({"disabled":false});
        });

        // setInterval(function () {
        //     $("#top_list_father").load(location.href + " #stu_table");
        // }, 2000);//8秒自动刷新
    })
    $(function () {
        setInterval(function () {
            $("#top_list_father").load(location.href + " #stu_table",null,function jsLoad(){
                $("#top_list").find("tr").each(function(){
                    var tdArr = $(this).children();
                    var topStatus = tdArr.eq(5).text();
                    // alert(topStatus);
                    if(topStatus == 1)
                        tdArr.eq(6).find('button').attr({"disabled":true});
                    else
                        tdArr.eq(6).find('button').attr({"disabled":false});
                });
            });
        }, 8000);//8秒自动刷新
    })

    // <!-- ==========================学生选题操作=================================== -->
    // $(".stu_choose_btn").click(function () {
    $(document).on("click",".stu_choose_btn",function () {
        <%--var curPageNo = ${curPageNo};--%>
        var selTopId = $(this).parent().parent().find("td:eq(1)").text();
        var selTopName = $(this).parent().parent().find("td:eq(2)").text();
        if (confirm("确认选择【" + selTopName+ "】吗？")){
            $.ajax({
                url:"/hrms/group/AddOptTopic?topId="+selTopId+"&curId=${curId}",
                type:"PUT",
                success:function (result) {
                    if (result.code == 100){
                        alert("选题成功！");
                        window.location.href="/hrms/topic/getTopList?curId=${curId}";
                    }else {
                        alert(result.extendInfo.top_opt_error);
                    }
                }
            });
        }
    });
</script>
</body>
</html>
