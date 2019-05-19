<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传课题</title>
</head>
<body>
<div class="modal fade topic-import-modal" tabindex="-1" role="dialog" aria-labelledby="topic-import-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">文件上传</h4>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">文件类型</label>
                <div class="col-sm-10">
                    <label class="radio-inline" for="top_upload">
                        <input type="radio" value="top_btn" name="file_type" id="top_upload" checked>上传课题
                    </label>
                    <label class="radio-inline" for="reportMode_upload">
                        <input type="radio" value="report_btn" name="file_type" id="reportMode_upload">上传报告模板
                    </label>
                </div>
            </div>
            <div class="modal-body top-import">
                <form class="form-horizontal topic-import-form" enctype="multipart/form-data">
                    <div class="form-group topicDiv">
                        <label for="add_topic" class="col-sm-2 control-label">上传课题</label>
                        <div class="col-sm-8">
                            <input type="file" name="topicFile" class="form-control" id="add_topic" required>
                        </div>
                    </div>
                    <div class="form-group reportDiv">
                        <label for="add_report" class="col-sm-2 control-label">报告模板</label>
                        <div class="col-sm-8">
                            <input type="file" name="reportMould" class="form-control" id="add_report" required>
                        </div>
                    </div>
                    <div class="form-group top_mouldDiv">
                        <label for="top_mould" class="col-sm-2 control-label">课题模板</label>
                        <div class="col-sm-8">
                            <a href="#" class="topModel" id="top_mould">课题导入模板</a>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary topic_save_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript">
    var  curId = 0;

    $('.reportDiv').hide();
    $("#top_upload").click(function(){
        $(".topicDiv").show();
        $('.reportDiv').hide();
        $('.top_mouldDiv').show();
    });
    $("#reportMode_upload").click(function(){
        $(".topicDiv").hide();
        $('.reportDiv').show();
        $('.top_mouldDiv').hide();
    });

    $(".topic_import_btn").click(function () {
        curId = $(this).parent().parent().find("td:eq(1)").text();
        // alert(curId);
        $('.topic-import-modal').modal({
            backdrop:static,
            keyboard:true
        });
    });

    // 下载课题模板
    $(".topModel").click(function () {
        $(this).attr("href", "/hrms/file/getTopicModel");
    });

    $(".topic_save_btn").click(function () {
        var file_type =  $('input[name="file_type"]:checked').val();
        var link = null;
        if(file_type == "top_btn"){
            var fileName = $("#add_topic").val();
            var idx = fileName.lastIndexOf(".");
            var suffix = fileName.substring(idx+1);
            if(suffix.toLowerCase() != 'xls' && suffix.toLowerCase() != 'xlsx'){
                alert("文件必须为xls或xlsx格式");
                return;
            }
            link = "/hrms/topic/importTopic/"+curId;
        }else{
            var fileName = $("#add_report").val();
            // alert(fileName);
            var idx = fileName.lastIndexOf(".");
            var suffix = fileName.substring(idx+1);
            if(suffix != 'tar' && suffix != 'gz' && suffix != 'tar.gz'
                && suffix != 'tgz' && suffix != 'bz2' && suffix != 'tar.bz2'
                && suffix != 'Z' && suffix != 'tar.Z' && suffix != 'zip'
                && suffix != 'rar'){
                alert("文件必须以.tar、.gz、.tar.gz、.tgz、.bz2、.tar.bz2、.Z、. tar.Z、.zip、.rar结尾");
                return;
            }
            link = "/hrms/cur/importReportMould/"+curId;
        }
        var formData = new FormData($('.topic-import-form')[0]);
        // alert(formData.get("reportMould"));
        $.ajax({
            url:link,
            type:"POST",
            data:formData,
            processData:false,
            contentType:false,
            success:function (result) {
                if(result.code == 100){
                    alert("导入成功！");
                    $('.topic-import-modal').modal("hide");
                }else {
                    $("#add_topic").val('');
                    $("#add_report").val('');
                    alert(result.extendInfo.topicUpload_err);
                }
            }
        });
    });
</script>
</body>
</html>

