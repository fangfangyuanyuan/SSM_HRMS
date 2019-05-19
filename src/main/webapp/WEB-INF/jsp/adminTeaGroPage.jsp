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
                        <li><a href="#">分组板块</a></li>
                        <li class="active">课程教师分配</li>
                    </ol>
                </div>
                <!-- Table -->
                <table class="table table-bordered table-hover" id="teaGro_table">
                    <thead>
                    <th>序号</th>
                    <th hidden>任课编号</th>
                    <th>课程名称</th>
                    <th>教师名称</th>
                    <th>
                        <a href="#" style="color:blue;text-decoration:none;"  class="scoTime_install_btn" data-toggle="modal" data-target=".time_install-modal">设置登记成绩时间</a>
                    </th>
                    <th>比例一</th>
                    <th>比例二</th>
                    <th>比例三</th>
                    <th>
                        <a href="#" style="color:blue;text-decoration:none;" class="cur_add_btn" data-toggle="modal" data-target=".cur-add-modal">增加</a>
                    </th>
                    </thead>
                    <tbody id="admin_scoTime">
                    <c:forEach items="${curriculums}" var="cur">
                        <tr>
                            <td></td>
                            <td hidden>${cur.curId}</td>
                            <td hidden>${cur.scoStart}</td>
                            <td hidden>${cur.scoEnd}</td>
                            <td>${cur.curYear}学年${cur.couName}</td>
                            <td>${cur.teaName}</td>
                            <td>${cur.scoStart}~${cur.scoEnd}</td>
                            <td>${cur.sco1Count}</td>
                            <td>${cur.sco2Count}</td>
                            <td>${cur.sco3Count}</td>
                            <td>
                                <a href="#" role="button" class="btn btn-primary count_edit_btn" data-toggle="modal" data-target=".count-update-modal">修改</a>
                                <a href="#" role="button" class="btn btn-danger cur_delete_btn">删除</a>
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
                            <li><a href="/hrms/cur/getTeaGroByAdmin?pageNo=1&couId=${couId}&curYear=${curYear}">首页</a></li>
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
                                    <li class="active"><a href="/hrms/cur/getTeaGroByAdmin?pageNo=${itemPage}&couId=${couId}&curYear=${curYear}">${itemPage}</a></li>
                                </c:if>
                                <c:if test="${curPageNo != itemPage}">
                                    <li><a href="/hrms/cur/getTeaGroByAdmin?pageNo=${itemPage}&couId=${couId}&curYear=${curYear}">${itemPage}</a></li>
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
                            <li><a href="/hrms/cur/getTeaGroByAdmin?pageNo=${totalPages}&couId=${couId}&curYear=${curYear}">尾页</a></li>
                        </ul>
                    </nav>
                </div>
            </div><!-- /.panel panel-success -->
        </div><!-- /.course_info -->
    </div><!-- /.hrms_course_body -->

    <%--<%@ include file="adminCourseAdd.jsp"%>--%>
    <%@ include file="adminCurriculumAdd.jsp"%>
    <%@ include file="TimeInstall.jsp"%>
    <%@ include file="adminProportionUpdate.jsp"%>
    <!-- 尾部-->
    <%--<%@ include file="./commom/foot.jsp"%>--%>

</div><!-- /.hrms_course_container -->

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
            $(this).attr("href", "/hrms/cur/getTeaGroByAdmin?pageNo="+pageNo+"&couId=${couId}&curYear=${curYear}");
        }
    });
    //下一页
    $(".nextPage").click(function () {
        if (curPageNo < totalPages){
            var pageNo = curPageNo + 1;
            $(this).attr("href", "/hrms/cur/getTeaGroByAdmin?pageNo="+pageNo+"&couId=${couId}&curYear=${curYear}");
        }
    });


    <!-- ==========================部门删除操作=================================== -->
    $(".cur_delete_btn").click(function () {
        var delCurId = $(this).parent().parent().find("td:eq(1)").text();
        var curPageNo = ${curPageNo};
        if (confirm("确认删除信息吗？")){
            $.ajax({
                url:"/hrms/cur/delCur/"+delCurId,
                type:"DELETE",
                success:function (result) {
                    if (result.code == 100){
                        alert("删除成功！");
                        window.location.href = "/hrms/cur/getTeaGroByAdmin?pageNo="+curPageNo+"&couId=${couId}&curYear=${curYear}";
                    }else {
                        alert(result.extendInfo.cur_del_error);
                    }
                }
            });
        }
    });
</script>
</body>
</html>
