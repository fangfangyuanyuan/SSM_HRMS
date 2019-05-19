<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>课程设计管理页面</title>
</head>
<body>
<div class="hrms_dept_container">
    <!-- 导航栏-->
    <%@ include file="./commom/head.jsp"%>


    <!-- 中间部分（左侧栏+表格内容） -->
    <div class="hrms_dept_body">
        <!-- 左侧栏 -->
        <%@ include file="commom/adminLeftsidebar.jsp"%>

        <!-- 部门表格内容 -->
        <div class="design_info col-sm-10">
            <div class="panel panel-success">
                <!-- 路径导航 -->
                <div class="panel-heading">
                    <ol class="breadcrumb">
                        <li><a href="#">课程设计管理</a></li>
                        <li class="active">课程设计信息</li>
                    </ol>
                </div>
                <!-- Table -->
                <table class="table table-bordered table-hover" id="dept_table">
                    <thead>
                    <th>课设编号</th>
                    <th>学年</th>
                    <th>学期</th>
                    <th>课程编号</th>
                    <th>教师编号</th>
                    <th>学生编号</th>
                    <th>设计成绩</th>
                    <th>答辩成绩</th>
                    <th>报告成绩</th>
                    <th>总成绩</th>
                    </thead>
                    <tbody>
                    <c:forEach items="${designs}" var="desi">
                        <tr>
                            <td>${desi.id}</td>
                            <td>${desi.stuyear}</td>
                            <td>${desi.term}</td>
                            <td>${desi.c_id}</td>
                            <td>${desi.t_id}</td>
                            <td>${desi.s_id}</td>
                            <td>${desi.d_score}</td>
                            <td>${desi.a_score}</td>
                            <td>${desi.r_score}</td>
                            <td>${desi.total}</td>
                            <td>
                                <a href="#" role="button" class="btn btn-primary desi_edit_btn" data-toggle="modal" data-target=".desi-update-modal">编辑</a>
                                <a href="#" role="button" class="btn btn-danger desi_delete_btn">删除</a>
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
                            <li><a href="/hrms/desi/getDesiList?pageNo=1">首页</a></li>
                            <c:if test="${curPageNo==1}">
                                <li class="disabled">departments
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
                                    <li class="active"><a href="/hrms/desi/getDesiList?pageNo=${itemPage}">${itemPage}</a></li>
                                </c:if>
                                <c:if test="${curPageNo != itemPage}">
                                    <li><a href="/hrms/desi/getDesiList?pageNo=${itemPage}">${itemPage}</a></li>
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
                            <li><a href="/hrms/desi/getDesiList?pageNo=${totalPages}">尾页</a></li>
                        </ul>
                    </nav>
                </div>
            </div><!-- /.panel panel-success -->
        </div><!-- /.design_info -->
    </div><!-- /.hrms_dept_body -->

    <%@ include file="designAdd.jsp"%>
    <%@ include file="designUpdate.jsp"%>

    <!-- 尾部-->
    <%--<%@ include file="./commom/foot.jsp"%>--%>

</div><!-- /.hrms_dept_container -->

<script type="text/javascript">
    var curPageNo = ${curPageNo};
    var totalPages = ${totalPages};
    //上一页
    $(".prePage").click(function () {
        if (curPageNo > 1){
            var pageNo = curPageNo - 1;
            $(this).attr("href", "/hrms/desi/getDesiList?pageNo="+pageNo);
        }
    });
    //下一页
    $(".nextPage").click(function () {
        if (curPageNo < totalPages){
            var pageNo = curPageNo + 1;
            $(this).attr("href", "/hrms/desi/getDesiList?pageNo="+pageNo);
        }
    });


    <!-- ==========================部门删除操作=================================== -->
    $(".desi_delete_btn").click(function () {
        var delDesiId = $(this).parent().parent().find("td:eq(0)").text();
        var curPageNo = ${curPageNo};
        if (confirm("确认删除信息吗？")){
            $.ajax({
                url:"/hrms/desi/delDesi/"+delDesiId,
                type:"DELETE",
                success:function (result) {
                    if (result.code == 100){
                        alert("删除成功！");
                        window.location.href = "/hrms/desi/getDesiList?pageNo="+curPageNo;
                    }else {
                        alert(result.extendInfo.del_desi_error);
                    }
                }
            });
        }
    });
</script>
</body>
</html>
