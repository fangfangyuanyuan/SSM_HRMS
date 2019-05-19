<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>学生成绩页面</title>
</head>
<body>
<div class="hrms_container">
    <!-- 导航条 -->
    <%@ include file="./commom/head.jsp"%>

    <!-- 中间部分（包括左边栏和员工/部门表单显示部分） -->
    <div class="hrms_body" style="position:relative; top:-15px;">

        <!-- 左侧栏 -->
        <%@ include file="commom/teaLeftsidebar.jsp"%>

        <!-- 中间员工表格信息展示内容 -->
        <div class="stu_info col-sm-10">

            <div class="panel panel-success">
                <!-- 路径导航 -->
                <div class="panel-heading">
                    <ol class="breadcrumb">
                        <li>课设板块</li>
                        <li class="active">学生成绩</li>
                    </ol>
                </div>
                <form class="form-horizontal update_partition_form">
                    <%--设置成绩比例--%>
                    <div class="table_items">
                        <div class="form-group">
                            <input type="text" name="curId"  value="${cur.curId}" hidden="hidden">
                            <label for="update_sco1" class="col-sm-2 control-label">成绩一比例[0~1]</label>
                            <div class="col-sm-1">
                                <input type="text" name="sco1Count" class="form-control" id="update_sco1" value="${cur.sco1Count}">
                            </div>
                            <label for="update_sco2" class="col-sm-2 control-label">成绩二比例[0~1]</label>
                            <div class="col-sm-1">
                                <input type="text" name="sco2Count" class="form-control" id="update_sco2" value="${cur.sco2Count}">
                            </div>
                            <label for="update_sco3" class="col-sm-2 control-label">成绩三比例[0~1]</label>
                            <div class="col-sm-1">
                                <input type="text" name="sco3Count" class="form-control" id="update_sco3" value="${cur.sco3Count}">
                            </div>
                            <button type="button" class="btn btn-danger partition_update_btn">保存</button>
                            <button type="button" class="btn btn-primary score_import" data-toggle="modal" data-target=".sco-import-modal">导入成绩</button>
                            <button type="button" id="score_Analysis" class="btn btn-primary">成绩分析</button>
                        </div>
                    </div>
                </form>
                <form class="form-horizontal update_sco_form">
                    <table class="table table-bordered table-hover" id="stu_table">
                        <thead>
                        <th>序号</th>
                        <th hidden="hidden">成绩编号</th>
                        <th>学号</th>
                        <th>姓名</th>
                        <th>目标一</th>
                        <th>目标二</th>
                        <th>目标三</th>
                        <th>总成绩</th>
                        </thead>
                        <tbody id="history_income_list">
                        <c:forEach items="${scores}" var="sco">
                            <tr class="scoList" id="${sco.groId}">
                                <td></td>
                                <td hidden="hidden"><input type="text" name="groId"  value="${sco.groId}"></td>
                                <td>${sco.stuId}</td>
                                <td>${sco.stuName}</td>
                                <td><input type="text" name="score1"  value="${sco.score1}"></td>
                                <td><input type="text" name="score2"  value="${sco.score2}"></td>
                                <td><input type="text" name="score3"  value="${sco.score3}"></td>
                                <td>${sco.total}</td>
                                <%--<td>--%>
                                    <%--<a href="#" role="button" class="btn btn-primary sco_edit_btn" data-toggle="modal" data-target=".sco-update-modal">编辑</a>--%>
                                <%--</td>--%>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <div class="panel-body">
                            <a href="#" style="color:blue;text-decoration:none;" class="sco_import_tea">导出成绩</a>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary cur_save_btn">保存</button>
                            <button type="button" class="btn btn-primary cur_submit_btn">提交</button>
                        </div>
                    </div>
                </form>
        </div><!-- /.emp_info -->
        <!-- 尾部 -->
        <%--<%@ include file="./commom/foot.jsp"%>--%>
    </div><!-- /.hrms_body -->
</div><!-- /.container -->
</div>
<%@include file="teaScoreUpload.jsp"%>
<script>
    var curId = ${curId};
    var scoEnd = new Date('${cur.scoEnd}');
    var scoStatus = ${cur.scoStatus};
    var current = new Date();
    $(function(){
        //$('table tr:not(:first)').remove();
        var len = $('table tr').length;
        for(var i = 1;i<len;i++){
            $('table tr:eq('+i+') td:first').text(i);
        }
    });
    //检查时间是否登记成绩结束
    $("#history_income_list").find("tr").each(function(){
        var tdArr = $(this).children();
        if(current>scoEnd || scoStatus==1){
            tdArr.eq(4).find('input').attr({"readonly":true});
            tdArr.eq(5).find('input').attr({"readonly":true});
            tdArr.eq(6).find('input').attr({"readonly":true});
            $(".cur_save_btn").attr({"disabled":true});//分数保存禁用
            $(".cur_submit_btn").attr({"disabled":true});//分数提交禁用
            $(".score_import").attr({"disabled":true});//导入分数禁用
            $(".partition_update_btn").attr({"disabled":true});//修改比例禁用
        }
    });


    //导出成绩报表
    $(".sco_import_tea").click(function () {
        $.ajax({
            url:"/hrms/cur/checkScoIsEmptyByCur?curId=${curId}",
            type:"GET",
            success:function (result) {
                if (result.code == 100){
                    window.location.href= "/hrms/group/exportScoByTea?curId=${curId}";
                }else {
                    alert(result.extendInfo.scoEmpty_check_error);
                }
            }
        });
    });
    //成绩分析
    $("#score_Analysis").click(function () {
        $.ajax({
            url:"/hrms/cur/checkScoIsEmptyByCur?curId=${curId}",
            type:"GET",
            success:function (result) {
                if (result.code == 100){
                    window.location.href="/hrms/group/getAnalysisValue?curId=${curId}";
                }else {
                    alert(result.extendInfo.scoEmpty_check_error);
                }
            }
        });
    });
    //更新比例
    $(".partition_update_btn").click(function () {
        $.ajax({
            url: "/hrms/cur/updatePartition/"+'${curId}',
            type: "PUT",
            data: $(".update_partition_form").serialize(),
            success:function (result) {
                if (result.code == 100){
                    alert("提交成功！");
                    window.location.href="/hrms/group/getScoListByTea?curId=${curId}";
                }else {
                    alert(result.extendInfo.partition_update_error);
                }
            }
        });
    });
    //保存成绩
    $(".cur_save_btn").click(function () {
        var trList = $("#history_income_list").children("tr");
        var customerArray = new Array();
        for (var i=0;i<trList.length;i++) {
            var tdArr = trList.eq(i).find("td");
            var history_groId = tdArr.eq(1).find('input').val();
            var history_sco1 = tdArr.eq(4).find('input').val();
            var history_sco2 = tdArr.eq(5).find('input').val();
            var history_sco3 = tdArr.eq(6).find('input').val();
            //批量保存数据时使用map存。
            customerArray.push({groId: history_groId, score1: history_sco1, score2: history_sco2,score3:history_sco3});
        }
        $.ajax({
            url:"/hrms/group/updateScoreList/"+0,
            type:"POST",
            contentType : 'application/json;charset=utf-8',
            dataType:"json",
            data: JSON.stringify(customerArray),
            // data: $.toJSON(customerArray),
            success:function (result) {
                if (result.code == 100){
                    alert("保存成功！");
                    window.location.href="/hrms/group/getScoListByTea?curId=${curId}";
                }else {
                    alert(result.extendInfo.sco_update_error);
                }
            }
        });
    });
    //提交成绩
    $(".cur_submit_btn").click(function () {
        var trList = $("#history_income_list").children("tr");
        var customerArray = new Array();
        for (var i=0;i<trList.length;i++) {
            var tdArr = trList.eq(i).find("td");
            var history_groId = tdArr.eq(1).find('input').val();
            var history_sco1 = tdArr.eq(4).find('input').val();
            var history_sco2 = tdArr.eq(5).find('input').val();
            var history_sco3 = tdArr.eq(6).find('input').val();
            //批量保存数据时使用map存。
            customerArray.push({groId: history_groId, score1: history_sco1, score2: history_sco2,score3:history_sco3});
        }
        $.ajax({
            url:"/hrms/group/updateScoreList/"+1,
            type:"POST",
            contentType : 'application/json;charset=utf-8',
            dataType:"json",
            data: JSON.stringify(customerArray),
            // data: $.toJSON(customerArray),
            success:function (result) {
                if (result.code == 100){
                    alert("提交成功！");
                    window.location.href="/hrms/group/getScoListByTea?curId=${curId}";
                }else {
                    alert(result.extendInfo.sco_update_error);
                }
            }
        });
    });
</script>
</body>

</html>
