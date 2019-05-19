<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>教师管理</title>
</head>
<body>
<div class="hrms_tea_container">
<!-- 导航栏-->
<%@ include file="./commom/head.jsp"%>


<!-- 中间部分（左侧栏+表格内容） -->
<div class="hrms_tea_body">
    <!-- 左侧栏 -->
    <%@ include file="commom/adminLeftsidebar.jsp"%>

    <!-- 部门表格内容 -->
    <div class="tea_info col-sm-10">
        <div class="panel panel-success">
            <!-- 路径导航 -->
            <div class="panel-heading">
                <ol class="breadcrumb">
                    <li><a href="#">教师板块</a></li>
                    <li class="active">教师信息</li>
                </ol>
            </div>
            <!-- Table -->
            <table class="table table-bordered table-hover" id="tea_table">
                <thead>
                <th>序号</th>
                <th>教工号</th>
                <th>名字</th>
                <th>专业</th>
                <th>院系</th>
                <th>密码</th>
                <th>
                    <a href="#" style="color:blue;text-decoration:none;" class="tea_add_btn" data-toggle="modal" data-target=".tea-add-modal">教师新增</a>
                </th>
                </thead>
                <tbody>
                <c:forEach items="${teachers}" var="tea">
                    <tr>
                        <td></td>
                        <td>${tea.teaId}</td>
                        <td>${tea.teaName}</td>
                        <td>${tea.teaMajor}</td>
                        <td>${tea.teaDept}</td>
                        <td>${tea.teaPass}</td>
                        <td>
                            <a href="#" role="button" class="btn btn-primary tea_edit_btn" data-toggle="modal" data-target=".tea-update-modal">编辑</a>
                            <a href="#" role="button" class="btn btn-danger tea_delete_btn">删除</a>
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
                        <li><a href="/hrms/tea/getTeaList?pageNo=1">首页</a></li>
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
                                <li class="active"><a href="/hrms/tea/getTeaList?pageNo=${itemPage}">${itemPage}</a></li>
                            </c:if>
                            <c:if test="${curPageNo != itemPage}">
                                <li><a href="/hrms/tea/getTeaList?pageNo=${itemPage}">${itemPage}</a></li>
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
                        <li><a href="/hrms/tea/getTeaList?pageNo=${totalPages}">尾页</a></li>
                    </ul>
                </nav>
            </div>
        </div><!-- /.panel panel-success -->
    </div><!-- /.dept_info -->
</div><!-- /.hrms_dept_body -->

<%@ include file="adminTeacherUpdate.jsp"%>

<!-- 尾部-->
<%--<%@ include file="./commom/foot.jsp"%>--%>

</div><!-- /.hrms_dept_container -->
<%@ include file="adminTeacherAdd.jsp"%>
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
            $(this).attr("href", "/hrms/tea/getTeaList?pageNo="+pageNo);
        }
    });
    //下一页
    $(".nextPage").click(function () {
        if (curPageNo < totalPages){
            var pageNo = curPageNo + 1;
            $(this).attr("href", "/hrms/tea/getTeaList?pageNo="+pageNo);
        }
    });


    <!-- ==========================教师删除操作=================================== -->
    $(".tea_delete_btn").click(function () {
        var delTeaId = $(this).parent().parent().find("td:eq(1)").text();
        var delTeaName = $(this).parent().parent().find("td:eq(2)").text();
        var curPageNo = ${curPageNo};
        if (confirm("确认删除【"+ delTeaName +"】的信息吗？")){
            $.ajax({
                url:"/hrms/tea/delTea/"+delTeaId,
                type:"DELETE",
                success:function (result) {
                    if (result.code == 100){
                        alert("删除成功！");
                        window.location.href = "/hrms/tea/getTeaList?pageNo="+curPageNo;
                    }else {
                        alert(result.extendInfo.tea_del_error);
                    }
                }
            });
        }
    });
</script>
</body>
</html>
