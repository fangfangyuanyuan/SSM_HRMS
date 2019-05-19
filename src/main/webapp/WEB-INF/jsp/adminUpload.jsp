<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>文件上传</title>
</head>
<body>
<div class="modal fade file-upload-modal" tabindex="-1" role="dialog" aria-labelledby="file-upload-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">文件上传</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal admin_upload_form" enctype="multipart/form-data">

                    <div class="form-group">
                        <label class="col-sm-2 control-label">附件类型</label>
                        <div class="col-sm-8">
                            <label class="radio-inline" for="cou_Info">
                                <input type="radio" id="cou_Info" value="cou_Info" name="type" checked="checked">课程信息
                            </label>
                            <label class="radio-inline" for="cou_tea_Info">
                                <input type="radio" id="cou_tea_Info" value="cou_tea_Info" name="type">课程教师分组
                            </label>
                            <label class="radio-inline" for="cou_tea_stu_Info">
                                <input type="radio" id="cou_tea_stu_Info" value="cou_tea_stu_Info" name="type">教师学生分组
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="myFile" class="col-sm-2 control-label">选择文件</label>
                        <div class="col-sm-8">
                            <input type="file" name="myFile" id="myFile" class="form-control" data-show-preview="false" required/>
                        </div>
                    </div>
                    <div class="form-group course_mould">
                        <label for="cou_mould" class="col-sm-2 control-label">文件模板</label>
                        <div class="col-sm-8">
                            <a href="#" class="couModel" id="cou_mould">课程信息导入模板</a>
                        </div>
                    </div>
                    <div class="form-group cou_tea_mould">
                        <label for="cou_tea_mould" class="col-sm-2 control-label">文件模板</label>
                        <div class="col-sm-8">
                            <a href="#" class="couTeaModel" id="cou_tea_mould">课程教师分组导入模板</a>
                        </div>
                    </div>
                    <div class="form-group tea_stu_mould">
                        <label for="tea_stu_mould" class="col-sm-2 control-label">文件模板</label>
                        <div class="col-sm-8">
                            <a href="#" class="teaStuModel" id="tea_stu_mould">教师学生分组导入模板</a>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <%--<input type="submit" value="提交" />--%>
                <button type="button" class="btn btn-primary file_save_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script type="text/javascript">

    $('.cou_tea_mould').hide();
    $('.tea_stu_mould').hide();

    $("#cou_Info").click(function(){
        $(".course_mould").show();
        $('.cou_tea_mould').hide();
        $('.tea_stu_mould').hide();
    });
    $("#cou_tea_Info").click(function(){
        $(".course_mould").hide();
        $('.cou_tea_mould').show();
        $('.tea_stu_mould').hide();
    });
    $("#cou_tea_stu_Info").click(function(){
        $(".course_mould").hide();
        $('.cou_tea_mould').hide();
        $('.tea_stu_mould').show();
    });


    $(".admin-upload-btn").click(function () {
        $('.file-upload-modal').modal({
            backdrop:static,
            keyboard:true
        });
    });

    //模板下载链接
    $(".couModel").click(function () {
        var cou_info = "cou_model";
        $(this).attr("href", "/hrms/file/getModel/"+cou_info);
    });
    $(".couTeaModel").click(function () {
        var cou_tea_info = "cou_tea_model";
        $(this).attr("href", "/hrms/file/getModel/"+cou_tea_info);
    });
    $(".teaStuModel").click(function () {
        var tea_stu_info = "tea_stu_model";
        $(this).attr("href", "/hrms/file/getModel/"+tea_stu_info);
    });

    //上传按钮
    $(".file_save_btn").click(function () {
        //检查文件格式
        var fileName = $("#myFile").val();
        if(fileName.length >0 && fileName != ''){
            var idx = fileName.lastIndexOf(".");
            var suffix = fileName.substring(idx+1);
            if(suffix.toLowerCase() != 'xls' && suffix.toLowerCase() != 'xlsx'){
                alert("文件必须为xls或xlsx格式");
            }else{
                var type =  $('input[name="type"]:checked').val();
                var formData = new FormData($('.admin_upload_form')[0]);
                var urls;
                console.log(formData);
                if(type == "cou_Info")
                    urls = "/hrms/course/importCourse";
                else if(type == "cou_tea_Info")
                    urls = "/hrms/tea/importTea";
                else
                    urls = "/hrms/student/importStudent";

                // alert("urls:"+urls);
                //验证省略...
                $.ajax({
                    url:urls,
                    type:"POST",
                    data:formData,
                    processData:false,
                    contentType:false,
                    success:function (result) {
                        if(result.code == 100){
                            alert(result.extendInfo.fileUpload_suc);
                            $('.file-upload-modal').modal("hide");
                        }else {
                            alert(result.extendInfo.fileUpload_err);
                        }
                    }
                });
            }
        }else{
            alert("请选择文件！");
        }

    });

</script>
</body>
</html>
