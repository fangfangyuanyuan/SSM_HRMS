<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传课题</title>
</head>
<body>
<div class="modal fade sco-import-modal" tabindex="-1" role="dialog" aria-labelledby="sco-import-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">成绩上传</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" id="score-upload-form" enctype="multipart/form-data">
                    <div class="form-group topicDiv">
                        <label for="add_score" class="col-sm-2 control-label">上传成绩</label>
                        <div class="col-sm-10">
                            <input type="file" name="scoreFile" class="form-control" id="add_score" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="sco_mould" class="col-sm-2 control-label">成绩模板</label>
                        <div class="col-sm-8">
                            <a href="#" class="scoMould" id="sco_mould">成绩导入模板</a>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary sco_save_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript">
    $(".score_import").click(function () {
        curId = $(this).parent().parent().find("td:eq(1)").text();
        // alert(curId);
        $('.sco-import-modal').modal({
            backdrop:static,
            keyboard:true
        });
    });

    //下载成绩模板
    $(".scoMould").click(function () {
         window.location.href ="/hrms/file/getScoModel?curId="+${curId};
    });

    $(".sco_save_btn").click(function () {
        alert("导入前请仔细核对学生成绩!");
        var fileName = $("#add_score").val();
        // alert(fileName);
        if(fileName.length >0 && fileName != ''){
            var idx = fileName.lastIndexOf(".");
            var suffix = fileName.substring(idx+1);
            if(suffix.toLowerCase() != 'xls' && suffix.toLowerCase() != 'xlsx'){
                alert("文件必须为xls或xlsx格式");
            }else{
                //上传文件
                var formData = new FormData($('#score-upload-form')[0]);
                // alert(formData.get("scoreFile"));
                $.ajax({
                    url:"/hrms/group/importScore/"+${curId},
                    type:"POST",
                    data:formData,
                    processData:false,
                    contentType:false,
                    success:function (result) {
                        if (result.code == 100) {
                            alert("成绩导入成功!");
                            window.location.href="/hrms/group/getScoListByTea?curId="+${curId};
                        } else{
                            $("#add_score ").val('');
                            alert(result.extendInfo.scoreUpload_err);
                        }
                    }
                });
            }
        }else{
            alert("请选中成绩文件!");
        }
    });
</script>
</body>
</html>

