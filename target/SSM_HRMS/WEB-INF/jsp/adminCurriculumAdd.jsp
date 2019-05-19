<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>选择课程</title>
</head>
<body>
<div class="modal fade cur-add-modal" tabindex="-1" role="dialog" aria-labelledby="cur-add-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">选择教师</h4>
            </div>
            <div class="modal-body">
                <form  enctype ="multipart/form-data" class="form-horizontal score-upload-form" method="GET">
                    <div class="form-group">
                        <label for="teaMajor" class="col-sm-2 control-label">专业</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                                <select class="form-control" name="teaMajor" id="teaMajor">
                                    <%-- <option value="1">CEO</option>--%>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group teaList">
                        <label for="teaName" class="col-sm-2 control-label">教师名字</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                                <select class="form-control" name="teaId" id="teaName">
                                    <%-- <option value="1">CEO</option>--%>
                                </select>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary cur_save_btn">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script type="text/javascript">
    // js中数字字符串转换string时前面的0丢失，方法加单引号
    var couId = '${couId}';
    var curYear = ${curYear};

    $(".cur_add_btn").click(function () {
        $.ajax({
            url:"/hrms/tea/getTeaMajorList",
            type:"GET",
            success:function (result) {
                if (result.code == 100) {
                    $("#teaMajor").empty();
                    $("#teaName").empty();
                    $.each(result.extendInfo.majors, function () {
                        var optionEle = $("<option></option>").append(this.teaMajor).attr("value", this.teaMajor);
                        optionEle.appendTo("#teaMajor");
                        $("#curId option:first").prop("selected", 'selected');
                    });
                    $.each(result.extendInfo.teachers, function () {
                        var optionEle = $("<option></option>").append(this.teaName).attr("value", this.teaId);
                        optionEle.appendTo("#teaName");
                        $("#teaName option:first").prop("selected", 'selected');
                    });
                } else{
                    alert(result.extendInfo.major_get_error);
                }
            }
        });

        $('.cur-add-modal').modal({
            backdrop:static,
            keyboard:true
        });
    });

    $("#teaMajor").change(function (){
        var teaMajor = $("#teaMajor").val();
        // alert(couYear);
        $.ajax({
            url:"/hrms/tea/getTeasByMajor/"+teaMajor,
            type:"GET",
            success:function (result) {
                if (result.code == 100) {
                    $("#teaName").empty();
                    $.each(result.extendInfo.teachers, function () {
                        var optionEle = $("<option></option>").append(this.teaName).attr("value", this.teaId);
                        optionEle.appendTo("#teaName");
                        $("#teaName option:first").prop("selected", 'selected');
                    });
                } else{
                    alert(result.extendInfo.tea_get_error);
                }
            }
        });
    });

    $(".cur_save_btn").click(function () {
        var teaId =   $("#teaName ").val();
        // alert(couId);
        $.ajax({
            url:"/hrms/cur/addCur?couId="+couId+"&teaId="+teaId+"&curYear="+curYear,
            type:"POST",
            success:function (result) {
                if (result.code == 100) {
                    alert("添加成功！");
                    $('.cur-add-modal').modal("hide");
                    window.location.href="/hrms/cur/getTeaGroByAdmin?couId="+couId+"&curYear= ${curYear}";
                } else{
                    alert(result.extendInfo.cur_add_error);
                }
            }
        });
    });
</script>
</body>
</html>
