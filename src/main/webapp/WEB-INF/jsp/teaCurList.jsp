<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>选择课程</title>
</head>
<body>
<div class="modal fade curId-get-modal" tabindex="-1" role="dialog" aria-labelledby="curId-get-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">选择课程</h4>
            </div>
            <div class="modal-body">
                <form  enctype ="multipart/form-data" class="form-horizontal" method="GET">
                    <div class="form-group">
                        <label for="get_curId" class="col-sm-2 control-label">课设名称</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                                <select class="form-control" name="curId" id="get_curId">
                                    <%-- <option value="1">CEO</option>--%>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">操作</label>
                        <div class="col-sm-10">
                            <label class="radio-inline" for="top_Info">
                                <input type="radio" value="top_Info" name="sel_type" id="top_Info">课题信息
                            </label>
                            <label class="radio-inline" for="top_review">
                                <input type="radio" value="top_review" name="sel_type" id="top_review">课题审核
                            </label>
                            <label class="radio-inline" for="stu_Info">
                                <input type="radio" value="stu_Info" name="sel_type" id="stu_Info">学生信息
                            </label>
                            <label class="radio-inline" for="score_Info">
                                <input type="radio" value="score_Info" name="sel_type" id="score_Info" checked="checked">学生成绩
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"></label>
                        <div class="col-sm-8">
                            <label class="radio-inline" for="stu_top_Info">
                                <input type="radio" value="stu_top_Info" name="sel_type" id="stu_top_Info">选题结果
                            </label>
                            <label class="radio-inline" for="report_Info">
                                <input type="radio" value="report_Info" name="sel_type" id="report_Info">下载报告
                            </label>
                            <label class="radio-inline" for="mould_download">
                                <input type="radio" value="mould_download" name="sel_type" id="mould_download">报告模板
                            </label>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary curId_save_btn">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script type="text/javascript">

    // $('.uploadScore').hide();
    // $('.sco_mouldDiv').hide();
    // $("#score_import").click(function(){
    //     $(".uploadScore").show();
    //     $('.sco_mouldDiv').show();
    // });
    // $("#stu_top_Info").click(function(){
    //     $(".uploadScore").hide();
    //     $('.sco_mouldDiv').hide();
    // });
    // $("#top_Info").click(function(){
    //     $(".uploadScore").hide();
    //     $('.sco_mouldDiv').hide();
    // });
    // $("#top_review").click(function(){
    //     $(".uploadScore").hide();
    //     $('.sco_mouldDiv').hide();
    // });
    // $("#stu_Info").click(function(){
    //     $(".uploadScore").hide();
    //     $('.sco_mouldDiv').hide();
    // });
    // $("#report_Info").click(function(){
    //     $(".uploadScore").hide();
    //     $('.sco_mouldDiv').hide();
    // });
    // $("#mould_download").click(function(){
    //     $(".uploadScore").hide();
    //     $('.sco_mouldDiv').hide();
    // });

    $(".curId_get_btn").click(function () {
        $.ajax({
            url:"/hrms/cur/getCouNameByTea",
            type:"GET",
            success:function (result) {
                $("#get_curId").empty();
                if (result.code == 100) {
                    $.each(result.extendInfo.courseList, function () {
                        var optionEle = $("<option></option>").append(this.curYear + "学年" + this.couName).attr("value", this.curId);
                        optionEle.appendTo("#get_curId");
                        $("#get_curId option:first").prop("selected", 'selected');
                    });
                } else{
                    alert(result.extendInfo.courseList_get_err);
                }
            }
        });

        $('.curId-get-modal').modal({
            backdrop:static,
            keyboard:true
        });
    });

    $(".curId_save_btn").click(function () {
        var curId =   $("#get_curId ").val();
        // alert(curId);
        var sel_type =  $('input[name="sel_type"]:checked').val();
        if(sel_type == "top_Info")
             window.location.href="/hrms/topic/getTopList?curId="+curId;
        if(sel_type == "top_review")
            window.location.href="/hrms/topic/getTopCheckList?curId="+curId;
        if(sel_type == "stu_Info")
            window.location.href="/hrms/student/getStuListVaTea?curId="+curId;
        if(sel_type == "stu_top_Info")
            window.location.href="/hrms/group/getOptTopByTea?curId="+curId;
        if(sel_type == "report_Info"){
            $.ajax({
                url:"/hrms/cur/checkStuReport?curId="+curId,
                type:"GET",
                success:function (result) {
                    if (result.code == 100) {
                        window.location.href= "/hrms/group/getstuReport?curId="+curId;
                    } else{
                        alert(result.extendInfo.stu_upload_error);
                    }
                }
            });
        }
        if(sel_type == "mould_download"){
            $.ajax({
                url:"/hrms/cur/checkReportMould?curId="+curId,
                type:"GET",
                success:function (result) {
                    if (result.code == 100) {
                        window.location.href="/hrms/file/getReportMould?curId="+curId;
                    } else{
                        alert(result.extendInfo.mould_upload_error);
                    }
                }
            });
        }
        if(sel_type == "score_Info"){
            $.ajax({
                url:"/hrms/cur/checkScoTime/"+curId,
                type:"GET",
                success:function (result) {
                    if (result.code == 100) {
                        window.location.href="/hrms/group/getScoListByTea?curId="+curId;
                    } else{
                        alert(result.extendInfo.sco_time_error);
                    }
                }
            });
        }
    });
</script>
</body>
</html>
