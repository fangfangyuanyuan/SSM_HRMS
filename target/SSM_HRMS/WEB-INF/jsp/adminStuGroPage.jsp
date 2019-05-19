<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>教师学生分组页面</title>
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
                        <li class="active">教师学生分组</li>
                    </ol>
                </div>
                <!-- Table -->
                <table class="table table-bordered table-hover" id="course_table">
                    <thead>
                    <th>序号</th>
                    <th hidden>成绩编号</th>
                    <th>课程名称</th>
                    <th>学年</th>
                    <th>指导教师</th>
                    <th>学号</th>
                    <th>学生姓名</th>
                    <th>
                        <a href="#" style="color:blue;text-decoration:none;" class="gro_add_btn" data-toggle="modal" data-target=".gro-add-modal">增加</a>
                    </th>
                    </thead>
                    <tbody>
                    <c:forEach items="${gros}" var="gro">
                        <tr>
                            <td></td>
                            <td hidden>${gro.groId}</td>
                            <td>${gro.couName}</td>
                            <td>${gro.curYear}</td>
                            <td>${gro.teaName}</td>
                            <td>${gro.stuId}</td>
                            <td>${gro.stuName}</td>
                            <td>
                                <a href="#" role="button" class="btn btn-danger gro_del_btn">删除</a>
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
                            <li><a href="/hrms/group/getStuGroupByAdmin?pageNo=1&curId=${curId}">首页</a></li>
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
                                    <li class="active"><a href="/hrms/group/getStuGroupByAdmin?pageNo=${itemPage}&curId=${curId}">${itemPage}</a></li>
                                </c:if>
                                <c:if test="${curPageNo != itemPage}">
                                    <li><a href="/hrms/group/getStuGroupByAdmin?pageNo=${itemPage}&curId=${curId}">${itemPage}</a></li>
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
                            <li><a href="/hrms/group/getStuGroupByAdmin?pageNo=${totalPages}&curId=${curId}">尾页</a></li>
                        </ul>
                    </nav>
                </div>
            </div><!-- /.panel panel-success -->
        </div><!-- /.course_info -->
    </div><!-- /.hrms_course_body -->

</div><!-- /.hrms_course_container -->
<%@ include file="adminGroupAdd.jsp"%>
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
            $(this).attr("href", "/hrms/group/getStuGroupByAdmin?pageNo="+pageNo+"&curId=${curId}");
        }
    });
    //下一页
    $(".nextPage").click(function () {
        if (curPageNo < totalPages){
            var pageNo = curPageNo + 1;
            $(this).attr("href", "/hrms/group/getStuGroupByAdmin?pageNo="+pageNo+"&curId=${curId}");
        }
    });


    <!-- ==========================课程学生分组删除操作=================================== -->
    $(".gro_del_btn").click(function () {
        var delgroId = $(this).parent().parent().find("td:eq(1)").text();
        var curPageNo = ${curPageNo};
        if (confirm("确认删除信息吗？")){
            $.ajax({
                url:"/hrms/group/delGro/"+delgroId,
                type:"DELETE",
                success:function (result) {
                    if (result.code == 100){
                        alert("删除成功！");
                        window.location.href = "/hrms/group/getStuGroupByAdmin?pageNo="+curPageNo+"&curId=${curId}";
                    }else {
                        alert(result.extendInfo.gro_del_err);
                    }
                }
            });
        }
    });
</script>
</body>
</html>
