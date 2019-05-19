<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>选择课程</title>
</head>
<body>
<div class="modal fade tea-stu-modal" tabindex="-1" role="dialog" aria-labelledby="tea-stu-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">选择课程</h4>
            </div>
            <div class="modal-body">
                <form  enctype ="multipart/form-data" class="form-horizontal score-upload-form" method="GET">
                    <div class="form-group">
                        <label for="curId" class="col-sm-2 control-label">课设名称</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                                <select class="form-control" name="curId" id="curId">
                                    <%-- <option value="1">CEO</option>--%>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group teaList">
                        <label for="teaId" class="col-sm-2 control-label">教师名字</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                                <select class="form-control" name="teaId" id="teaId">
                                    <%-- <option value="1">CEO</option>--%>
                                </select>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary stuGro_save_btn">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script type="text/javascript">

//    $('.teaList').hide();

    $(".tea_stu_btn").click(function () {
        $.ajax({
            url:"/hrms/cur/getCurNameByAdmin",
            type:"GET",
            success:function (result) {
                if (result.code == 100) {
                    $("#curId").empty();
                    $("#teaId").empty();
                    $.each(result.extendInfo.curriculumList, function () {
                        var optionEle = $("<option></option>").append(this.curYear + "学年" + this.couName).attr("value", this.curYear+"_"+this.couId);
                        optionEle.appendTo("#curId");
                        $("#curId option:first").prop("selected", 'selected');
                    });
                    $.each(result.extendInfo.teaList, function () {
                        var optionEle = $("<option></option>").append(this.teaName).attr("value", this.curId);
                        optionEle.appendTo("#teaId");
                        $("#teaId option:first").prop("selected", 'selected');
                    });
                } else{
                    alert(result.extendInfo.courseList_get_err);
                }
            }
        });

        $('.tea-stu-modal').modal({
            backdrop:static,
            keyboard:true
        });
    });

    $("#curId").change(function (){
        var couYear = $("#curId").val();
        // alert(couYear);
        $.ajax({
            url:"/hrms/cur/getTeaNameByAdmin/"+couYear,
            type:"GET",
            success:function (result) {
                if (result.code == 100) {
                    $("#teaId").empty();
                    $.each(result.extendInfo.teaList, function () {
                        var optionEle = $("<option></option>").append(this.teaName).attr("value", this.curId);
                        optionEle.appendTo("#teaId");
                        $("#teaId option:first").prop("selected", 'selected');
                    });
                } else{
                    alert(result.extendInfo.teaList_get_err);
                }
            }
        });
    });

    $(".stuGro_save_btn").click(function () {
        var curId =   $("#teaId ").val();
        alert(curId);
        window.location.href="/hrms/group/getStuGroupByAdmin?curId="+curId;
    });
</script>
</body>
</html>
