<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>选择课程</title>
</head>
<body>
<div class="modal fade gro-add-modal" tabindex="-1" role="dialog" aria-labelledby="gro-add-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">选择专业</h4>
            </div>
            <div class="modal-body">
                <form  enctype ="multipart/form-data" class="form-horizontal" method="GET">
                    <div class="form-group">
                        <label for="stuMajor" class="col-sm-2 control-label">专业</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                                <select class="form-control" name="stuMajor" id="stuMajor">
                                    <%-- <option value="1">CEO</option>--%>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="stuClass" class="col-sm-2 control-label">班级</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                                <select class="form-control" name="stuClass" id="stuClass">
                                    <%-- <option value="1">CEO</option>--%>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="stuName" class="col-sm-2 control-label">学生</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                                <select class="form-control" name="stuId" id="stuName">
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
    var curId = '${curId}';

    //初始列表
    $(".gro_add_btn").click(function () {
        $.ajax({
            url:"/hrms/student/getStuMajorList",
            type:"GET",
            success:function (result) {
                if (result.code == 100) {
                    $("#stuMajor").empty();
                    $("#stuClass").empty();
                    $("#stuName").empty();
                    $.each(result.extendInfo.majors, function () {
                        var optionEle = $("<option></option>").append(this.stuMajor).attr("value", this.stuMajor);
                        // alert(JSON.stringify(optionEle));
                        optionEle.appendTo("#stuMajor");
                        $("#stuMajor option:first").prop("selected", 'selected');
                    });
                    $.each(result.extendInfo.classes, function () {
                        var optionEle = $("<option></option>").append(this.stuClass).attr("value", this.stuClass);
                        optionEle.appendTo("#stuClass");
                        $("#stuClass option:first").prop("selected", 'selected');
                    });
                    $.each(result.extendInfo.stus, function () {
                        var optionEle = $("<option></option>").append(this.stuName).attr("value", this.stuId);
                        optionEle.appendTo("#stuName");
                        $("#stuName option:first").prop("selected", 'selected');
                    });
                } else{
                    alert(result.extendInfo.major_get_error);
                }
            }
        });

        $('.gro-add-modal').modal({
            backdrop:static,
            keyboard:true
        });
    });

    // 专业列表变化,班级学生列表变
    $("#stuMajor").change(function (){
        var stuMajor = $("#stuMajor").val();
        // alert(couYear);
        $.ajax({
            url:"/hrms/student/getClassesByMajor/"+stuMajor,
            type:"GET",
            success:function (result) {
                if (result.code == 100) {
                    $("#stuClass").empty();
                    $("#stuName").empty();
                    $.each(result.extendInfo.classes, function () {
                        var optionEle = $("<option></option>").append(this.stuClass).attr("value", this.stuClass);
                        optionEle.appendTo("#stuClass");
                        $("#stuClass option:first").prop("selected", 'selected');
                    });
                    $.each(result.extendInfo.stus, function () {
                        var optionEle = $("<option></option>").append(this.stuName).attr("value", this.stuId);
                        optionEle.appendTo("#stuName");
                        $("#stuName option:first").prop("selected", 'selected');
                    });
                } else{
                    alert(result.extendInfo.class_get_error);
                }
            }
        });
    });

    // 班级列表变化学生列表变
    $("#stuClass").change(function (){
        var stuMajor = $("#stuMajor").val();
        var stuClass = $("#stuClass").val();
        // alert(stuMajor);
        // alert(stuClass);
        $.ajax({
            url:"/hrms/student/getStusByMajorAndClass?stuMajor="+stuMajor+"&stuClass="+stuClass,
            type:"GET",
            success:function (result) {
                if (result.code == 100) {
                    $("#stuName").empty();
                    $.each(result.extendInfo.stus, function () {
                        var optionEle = $("<option></option>").append(this.stuName).attr("value", this.stuId);
                        optionEle.appendTo("#stuName");
                        $("#stuName option:first").prop("selected", 'selected');
                    });
                } else{
                    alert(result.extendInfo.stu_get_error);
                }
            }
        });
    });


    $(".cur_save_btn").click(function () {
        var stuId =   $("#stuName ").val();
        // alert(couId);
        $.ajax({
            url:"/hrms/group/addGro?curId="+curId+"&stuId="+stuId,
            type:"POST",
            success:function (result) {
                if (result.code == 100) {
                    alert("添加成功！");
                    $('.gro-add-modal').modal("hide");
                    window.location.href="/hrms/group/getStuGroupByAdmin?curId="+curId;
                } else{
                    alert(result.extendInfo.gro_add_error);
                }
            }
        });
    });
</script>
</body>
</html>
