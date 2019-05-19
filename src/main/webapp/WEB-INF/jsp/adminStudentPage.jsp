<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>学生管理页面</title>
</head>
<body>
<div class="hrms_container">
    <!-- 导航条 -->
    <%@ include file="./commom/head.jsp"%>

    <!-- 中间部分（包括左边栏和员工/部门表单显示部分） -->
    <div class="hrms_body" style="position:relative; top:-15px;">

        <!-- 左侧栏 -->
        <%@ include file="commom/adminLeftsidebar.jsp"%>

        <!-- 中间员工表格信息展示内容 -->
        <div class="stu_info col-sm-10">

            <div class="panel panel-success">
                <!-- 路径导航 -->
                <div class="panel-heading">
                    <ol class="breadcrumb">
                        <li><a href="#">学生板块</a></li>
                        <li class="active">学生信息</li>
                    </ol>
                </div>
                <!-- Table -->
                <table class="table table-bordered table-hover" id="stu_table">
                    <thead>
                    <th>序号</th>
                    <th>学号</th>
                    <th>姓名</th>
                    <th>班级</th>
                    <th>专业</th>
                    <th>院系</th>
                    <th>密码</th>
                    <th>
                        <a href="#" style="color:blue;text-decoration:none;" class="stu_add_btn" data-toggle="modal" data-target=".stu-add-modal">学生新增</a>
                    </th>
                    </thead>
                    <tbody>
                    <c:forEach items="${students}" var="stu">
                        <tr>
                            <td></td>
                            <td>${stu.stuId}</td>
                            <td>${stu.stuName}</td>
                            <td>${stu.stuClass}</td>
                            <td>${stu.stuMajor}</td>
                            <td>${stu.stuDept}</td>
                            <td>${stu.stuPass}</td>
                            <td>
                                <a href="#" role="button" class="btn btn-primary stu_edit_btn" data-toggle="modal" data-target=".stu-update-modal">编辑</a>
                                <a href="#" role="button" class="btn btn-danger stu_delete_btn">删除</a>
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
                            <li><a href="/hrms/student/getStuList?pageNo=1">首页</a></li>
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
                                    <li class="active"><a href="/hrms/student/getStuList?pageNo=${itemPage}">${itemPage}</a></li>
                                </c:if>
                                <c:if test="${curPageNo != itemPage}">
                                    <li><a href="/hrms/student/getStuList?pageNo=${itemPage}">${itemPage}</a></li>
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
                            <li><a href="/hrms/student/getStuList?pageNo=${totalPages}">尾页</a></li>
                        </ul>
                    </nav>
                </div>
            </div><!-- /.panel panel-success -->
        </div><!-- /.emp_info -->
        <!-- 尾部 -->
        <%--<%@ include file="./commom/foot.jsp"%>--%>
    </div><!-- /.hrms_body -->
</div><!-- /.container -->

<%--<%@ include file="employeeAdd.jsp"%>--%>
<%@ include file="adminStudentUpdate.jsp"%>
<%@ include file="adminStudentAdd.jsp"%>
<script>
    $(function(){
        //$('table tr:not(:first)').remove();
        var len = $('table tr').length;
        for(var i = 1;i<len;i++){
            $('table tr:eq('+i+') td:first').text(i);
        }
    });
    $(function () {
        //上一页
        var curPageNo = ${curPageNo};
        var totalPages = ${totalPages};
        $(".prePage").click(function () {
            if (curPageNo > 1){
                var pageNo = curPageNo-1;
                $(this).attr("href", "/hrms/student/getStuList?pageNo="+pageNo);
            }
        });
        //下一页
        $(".nextPage").click(function () {
            if (curPageNo < totalPages){
                var pageNo = curPageNo+1;
                $(this).attr("href", "/hrms/student/getStuList?pageNo="+pageNo);
            }
        });
    })

    // <!-- ==========================学生删除操作=================================== -->
    $(".stu_delete_btn").click(function () {
        var curPageNo = ${curPageNo};
        var delStuId = $(this).parent().parent().find("td:eq(1)").text();
        var delStuName = $(this).parent().parent().find("td:eq(2)").text();
        if (confirm("确认删除【" + delStuId+ "】的信息吗？")){
            $.ajax({
                url:"/hrms/student/delStu/"+delStuId,
                type:"DELETE",
                success:function (result) {
                    if (result.code == 100){
                        alert("删除成功！");
                        window.location.href="/hrms/student/getStuList?pageNo="+curPageNo;
                    }else {
                        alert(result.extendInfo.stu_del_error);
                    }
                }
            });
        }
    });


</script>
</body>

</html>