<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>成绩页面</title>
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
                        <li><a href="#">成绩板块</a></li>
                        <li class="active">成绩查询</li>
                    </ol>
                </div>
                <!-- Table -->
                <table class="table table-bordered table-hover" id="sco_table">
                    <thead>
                    <th>序号</th>
                    <th>课程名称</th>
                    <th>学年</th>
                    <th>指导教师</th>
                    <th>设计能力</th>
                    <th>编码能力</th>
                    <th>报告成绩</th>
                    <th>总分</th>
                    </thead>
                    <tbody>
                    <c:forEach items="${scores}" var="sco">
                        <tr>
                            <td></td>
                            <td>${sco.couName}</td>
                            <td>${sco.curYear}</td>
                            <td>${sco.teaName}</td>
                            <td>${sco.score1}</td>
                            <td>${sco.score2}</td>
                            <td>${sco.score3}</td>
                            <td>${sco.total}</td>
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
                            <li><a href="/hrms/group/getScoreByStu?pageNo=1">首页</a></li>
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
                                    <li class="active"><a href="/hrms/group/getScoreByStu?pageNo=${itemPage}}">${itemPage}</a></li>
                                </c:if>
                                <c:if test="${curPageNo != itemPage}">
                                    <li><a href="/hrms/group/getScoreByStu?pageNo=${itemPage}">${itemPage}</a></li>
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
                            <li><a href="/hrms/group/getScoreByStu?pageNo=${totalPages}">尾页</a></li>
                        </ul>
                    </nav>
                </div>
            </div><!-- /.panel panel-success -->
        </div><!-- /.emp_info -->
        <!-- 尾部 -->
        <%--<%@ include file="./commom/foot.jsp"%>--%>
    </div><!-- /.hrms_body -->
</div><!-- /.container -->

<%@ include file="teaTopicUpdate.jsp" %>
<script type="text/javascript">
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
                $(this).attr("href", "/hrms/group/getScoreByStu?pageNo="+pageNo);
            }
        });
        //下一页
        $(".nextPage").click(function () {
            if (curPageNo < totalPages){
                var pageNo = curPageNo+1;
                $(this).attr("href", "/hrms/group/getScoreByStu?pageNo="+pageNo);
            }
        });
    })
</script>
</body>
</html>
