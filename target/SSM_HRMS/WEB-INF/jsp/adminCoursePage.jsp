<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>课程管理页面</title>
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
                        <li><a href="#">课程板块</a></li>
                        <li class="active">课程信息</li>
                    </ol>
                </div>
                <!-- Table -->
                <table class="table table-bordered table-hover" id="course_table">
                    <thead>
                    <th>序号</th>
                    <th>课程编号</th>
                    <th>课程名称</th>
                    <th>课程性质</th>
                    <th>课程学分</th>
                    <th>开设院系</th>
                    <th>
                        <a href="#" style="color:blue;text-decoration:none;" class="cou_add_btn" data-toggle="modal" data-target=".course-add-modal">课程添加</a>
                    </th>
                    </thead>
                    <tbody>
                    <c:forEach items="${courses}" var="cou">
                        <tr>
                            <td></td>
                            <td>${cou.couId}</td>
                            <td>${cou.couName}</td>
                            <td>${cou.couNature}</td>
                            <td>${cou.couCredit}</td>
                            <td>${cou.couDept}</td>
                            <td>
                                <a href="#" role="button" class="btn btn-primary cou_edit_btn" data-toggle="modal" data-target=".cou-update-modal">编辑</a>
                                <a href="#" role="button" class="btn btn-danger cou_delete_btn">删除</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <div class="panel-body">
                    <div class="table_items">
                        当前第<span class="badge">${curPageNo}</span>页，共有<span class="badge">${totalPages}</span>页，总记录数<span class="badge">${totalItems}</span>条。
                    </div>
                    <nav aria-label="Page navigation" class="pull-right">
                        <ul class="pagination">
                            <li><a href="/hrms/course/getCouList?pageNo=1">首页</a></li>
                            <c:if test="${curPageNo==1}">
                                <li class="disabled">
                                    <a href="#" aria-label="Previous" class="prePage">
                                        <span aria-hidden="true">&laquo;</span>
                                    </a>
                                </li>
                            </c:if>
                            <c:if test="${curPageNo!=1}">
                                <li>
                                    <a href="#" aria-label="Previous" class="prePage">
                                        <span aria-hidden="true">&laquo;</span>
                                    </a>
                                </li>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages<5?totalPages:5}" step="1" var="itemPage">
                                <c:if test="${curPageNo == itemPage}">
                                    <li class="active"><a href="/hrms/course/getCouList?pageNo=${itemPage}">${itemPage}</a></li>
                                </c:if>
                                <c:if test="${curPageNo != itemPage}">
                                    <li><a href="/hrms/course/getCouList?pageNo=${itemPage}">${itemPage}</a></li>
                                </c:if>
                            </c:forEach>

                            <c:if test="${curPageNo==totalPages}">
                                <li class="disabled" class="nextPage">
                                    <a href="#" aria-label="Next">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </c:if>
                            <c:if test="${curPageNo!=totalPages}">
                                <li>
                                    <a href="#" aria-label="Next" class="nextPage">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </c:if>
                            <li><a href="/hrms/course/getCouList?pageNo=${totalPages}">尾页</a></li>
                        </ul>
                    </nav>
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
    var curPageNo = ${curPageNo};
    var totalPages = ${totalPages};
    $(function(){
        //$('table tr:not(:first)').remove();
        var len = $('table tr').length;
        for(var i = 1;i<len;i++){
            $('table tr:eq('+i+') td:first').text(i);
        }
    });
    //上一页
    $(".prePage").click(function () {
        if (curPageNo > 1){
            var pageNo = curPageNo - 1;
            $(this).attr("href", "/hrms/course/getCouList?pageNo="+pageNo);
        }
    });
    //下一页
    $(".nextPage").click(function () {
        if (curPageNo < totalPages){
            var pageNo = curPageNo + 1;
            $(this).attr("href", "/hrms/course/getCouList?pageNo="+pageNo);
        }
    });
    //下载评分标准
    $(".standard_down").click(function () {
        var couId = $(this).parent().parent().find("td:eq(1)").text();
        alert(couId);
        $(this).attr("href", "/hrms/file/getStadardMouldByAdmin?couId="+couId);
    });

    <!-- ==========================课程删除操作=================================== -->
    $(".cou_delete_btn").click(function () {
        var delCouId = $(this).parent().parent().find("td:eq(1)").text();
        var curPageNo = ${curPageNo};
        if (confirm("确认删除信息吗？")){
            $.ajax({
                url:"/hrms/course/delCou/"+delCouId,
                type:"DELETE",
                success:function (result) {
                    if (result.code == 100){
                        alert("删除成功！");
                        window.location.href = "/hrms/course/getCouList?pageNo="+curPageNo;
                    }else {
                        alert(result.extendInfo.cou_del_error);
                    }
                }
            });
        }
    });
</script>
</body>
</html>
