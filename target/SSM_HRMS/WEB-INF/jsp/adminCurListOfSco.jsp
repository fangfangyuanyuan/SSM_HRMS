<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>选择课程</title>
</head>
<body>
<div class="modal fade sco-get-modal" tabindex="-1" role="dialog" aria-labelledby="sco-get-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">选择课程</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="curName" class="col-sm-2 control-label">课设名称</label>
                        <div class="col-sm-10">
                            <div class="checkbox">
                                <select class="form-control" name="curName" id="curName">
                                    <%-- <option value="1">CEO</option>--%>
                                </select>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary stuSco_save_btn">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script type="text/javascript">

//    $('.teaList').hide();

    $(".sco_get_btn").click(function () {
        $.ajax({
            url:"/hrms/cur/getCurNameByAdmin",
            type:"GET",
            success:function (result) {
                if (result.code == 100) {
                    $("#curName").empty();
                    $("#teaId").empty();
                    $.each(result.extendInfo.curriculumList, function () {
                        var optionEle = $("<option></option>").append(this.curYear + "学年" + this.couName).attr("value", this.curYear+"_"+this.couId);
                        optionEle.appendTo("#curName");
                        $("#curName option:first").prop("selected", 'selected');
                    });
                } else{
                    alert(result.extendInfo.courseList_get_err);
                }
            }
        });

        $('.sco-get-modal').modal({
            backdrop:static,
            keyboard:true
        });
    });

    $(".stuSco_save_btn").click(function () {
        var curName =   $("#curName ").val();
        $.ajax({
            url:"/hrms/cur/checkScoEndByCurName/"+curName,
            type:"GET",
            success:function (result) {
                if (result.code == 100) {
                    window.location.href="/hrms/group/getStuScoByAdmin?curName="+curName;
                } else{
                    alert(result.extendInfo.scoEnd_before_err);
                }
            }
        });
    });
</script>
</body>
</html>
