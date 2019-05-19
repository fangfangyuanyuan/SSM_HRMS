<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>选择课程</title>
</head>
<body>
<div class="modal fade top-opt-modal" tabindex="-1" role="dialog" aria-labelledby="top-opt-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">选择课程</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal topic-upload-form" enctype="multipart/form-data" mehtod="post">
                    <div class="form-group">
                        <label for="get_curId" class="col-sm-2 control-label">课设名称</label>
                        <div class="col-sm-8">
                            <div class="checkbox">
                                <select class="form-control" name="curId" id="get_curId">
                                    <%-- <option value="1">CEO</option>--%>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">操作</label>
                        <div class="col-sm-8">
                            <label class="radio-inline" for="opt">
                                <input type="radio" value="top_Info" name="sel_type" id="opt" checked="checked">选择课题
                            </label>
                            <label class="radio-inline" for="upload">
                                <input type="radio" value="top_upload" name="sel_type" id="upload">上传报告
                            </label>
                            <label class="radio-inline" for="report_down">
                                <input type="radio" value="report_down" name="sel_type" id="report_down">下载报告模板
                            </label>
                            <label class="radio-inline">
                                <%--<a href="#" class="top_draft_btn" data-toggle="modal" data-target=".draft-add-modal">自拟课题</a>--%>
                                <a href="#" class="top_draft_btn" data-toggle="modal">自拟课题</a>
                            </label>
                        </div>
                    </div>
                    <div class="form-group uploadReport">
                        <label class="col-sm-2 control-label" for="report">上传报告</label>
                        <div class="col-sm-8">
                            <input type="file" value="top_upload" class="form-control" name="stuReport" id="report" required>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary top_save_btn">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<%@ include file="stuTopicAdd.jsp"%>
<script type="text/javascript">
    $('.uploadReport').hide();
    $("#upload").click(function(){
        $(".uploadReport").show();
    });
    $("#opt").click(function(){
        $(".uploadReport").hide();
    });
    $("#report_down").click(function(){
        $(".uploadReport").hide();
    });

    $(".top_get_btn").click(function () {
        $.ajax({
            url:"/hrms/cur/getCouNameByStu",
            type:"GET",
            success:function (result) {
                if (result.code == 100) {
                    $.each(result.extendInfo.courseList, function () {
                        var optionEle = $("<option></option>").append(this.curYear + "学年" + this.couName + "(" + this.teaName + ")").attr("value", this.curId);
                        optionEle.appendTo("#get_curId");
                        $("#get_curId option:first").prop("selected", 'selected');
                    });
                } else{
                    alert(result.extendInfo.courseList_get_err);
                }
            }
        });

        $('.top-opt-modal').modal({
            backdrop:static,//没有这个，登陆进来会显示模态框
            keyboard:true
        });
    });


    $(".top_save_btn").click(function () {
        var curId =   $("#get_curId ").val();
        // alert("curId:"+curId);
        var sel_type =  $('input[name="sel_type"]:checked').val();
        // alert(sel_type);
        if(sel_type == "top_Info"){
            $.ajax({
                url:"/hrms/topic/checkTopTime/"+curId,
                type:"GET",
                success:function (result) {
                    if (result.code == 100) {
                        // var curId1 = result.extendInfo.curId;
                        window.location.href="/hrms/topic/getTopList?curId="+curId;
                    } else{
                        alert(result.extendInfo.time_error);
                    }
                }
            });
        }
        if(sel_type == "top_upload"){
            var fileName = $("#report").val();
            if(fileName.length >0 && fileName != ''){
                var idx = fileName.indexOf(".");
                var suffix = fileName.substring(idx+1);
                var suffix = suffix.toLowerCase();
                alert(suffix);
                if(suffix != 'tar' && suffix != 'gz' && suffix != 'tar.gz'
                    && suffix != 'tgz' && suffix != 'bz2' && suffix != 'tar.bz2'
                    && suffix != 'Z' && suffix != 'tar.Z' && suffix != 'zip'
                    && suffix != 'rar'){
                    $(".uploadReport").val('');
                    alert("文件必须以.tar、.gz、.tar.gz、.tgz、.bz2、.tar.bz2、.Z、. tar.Z、.zip、.rar结尾");
                }else{
                    var formData = new FormData($('.topic-upload-form')[0]);
                    $.ajax({
                        url:"/hrms/group/importReport/"+curId,
                        type:"POST",
                        data:formData,
                        processData:false,
                        contentType:false,
                        success:function (result) {
                            if(result.code == 100){
                                alert("报告上传成功！");
                                $('.top-opt-modal').modal("hide");
                            }else {
                                alert(result.extendInfo.reportUpload_err);
                            }
                        }
                    });
                }
            }
            else{
                alert("请选择上传的报告！");
            }
        }
        if(sel_type == "report_down"){
            // window.location.href= "/hrms/file/getReportMould?curId="+curId;
            $.ajax({
                url:"/hrms/topic/checkReportTime/"+curId,
                type:"GET",
                success:function (result) {
                    if (result.code == 100) {
                        window.location.href="/hrms/file/getReportMould?curId="+curId;
                    } else{
                        alert(result.extendInfo.time_error);
                    }
                }
            });
        }
    });
</script>
</body>
</html>
