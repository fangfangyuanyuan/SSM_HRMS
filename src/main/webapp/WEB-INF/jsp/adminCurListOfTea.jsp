<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>课程教师分组</title>
    <link type="text/css" rel="stylesheet" href="/jedate/test/jeDate-test.css">
    <link type="text/css" rel="stylesheet" href="/jedate/skin/jedate.css">
    <script type="text/javascript" src="/jedate/src/jedate.js"></script>
</head>
<body>
<div class="modal fade cou-tea-modal" tabindex="-1" role="dialog" aria-labelledby="cou-tea-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">选择课程</h4>
            </div>
            <div class="modal-body">
                <form  enctype ="multipart/form-data" class="form-horizontal score-upload-form" method="GET">
                    <div class="form-group">
                        <label for="couId" class="col-sm-2 control-label">课设名称</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                                <select class="form-control" name="couId" id="couId">
                                    <%-- <option value="1">CEO</option>--%>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="curYear">学年</label>
                        <div class="col-sm-8">
                            <input type="text" class="jeinput" id="curYear" placeholder="2019">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary teaGro_save_btn">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript" src="/jedate/test/demo3.js"></script>
<script type="text/javascript">
    $(".cou_tea_btn").click(function () {
        $.ajax({
            url:"/hrms/course/getCouNameByAdmin",
            type:"GET",
            success:function (result) {
                if (result.code == 100) {
                    $("#couId").empty();
                    $.each(result.extendInfo.courseList, function () {
                        var optionEle = $("<option></option>").append( this.couName).attr("value", this.couId);
                        optionEle.appendTo("#couId");
                        $("#couId option:first").prop("selected", 'selected');
                    });
                } else{
                    alert(result.extendInfo.courseList_get_err);
                }
            }
        });

        $('.cou-tea-modal').modal({
            backdrop:static,
            keyboard:true
        });
    });

    $(".teaGro_save_btn").click(function () {
        var couId =   $("#couId").val();
        var curYear = $("#curYear").val();
        // alert(couId);
        window.location.href="/hrms/cur/getTeaGroByAdmin?couId="+couId+"&curYear="+curYear;
    });
</script>
</body>
</html>
