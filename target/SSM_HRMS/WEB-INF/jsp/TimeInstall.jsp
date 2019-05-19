<%--
  Created by IntelliJ IDEA.
  User: me
  Date: 2019/4/28
  Time: 16:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>设置选题时间</title>
    <link type="text/css" rel="stylesheet" href="/jedate/test/jeDate-test.css">
    <link type="text/css" rel="stylesheet" href="/jedate/skin/jedate.css">
    <script type="text/javascript" src="/jedate/src/jedate.js"></script>
</head>
<body>
<div class="modal fade time_install-modal" tabindex="-1" role="dialog" aria-labelledby="time_install-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">设置时间</h4>
            </div>
            <div class="modal-body">
                <form name="file" class="form-horizontal time-install-form" enctype="multipart/form-data">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="startTime">开始时间</label>
                        <div class="col-sm-8">
                            <input type="text" class="jeinput" name="startTime" id="startTime" placeholder="YYYY-MM-DD hh:mm:ss" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="endTime">结束时间</label>
                        <div class="col-sm-8">
                            <input type="text" class="jeinput" name="endTime"  id="endTime" placeholder="YYYY-MM-DD hh:mm:ss" required>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <%--<input type="submit" value="提交" />--%>
                <button type="button" class="btn btn-primary time_save_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript" src="/jedate/test/demo2.js"></script>
<script type="text/javascript">
    var  curId = 0;
    var start = null;
    var end = null;
    $(".time_install_btn").click(function () {
        curId = $(this).parent().parent().find("td:eq(1)").text();
        start = $(this).parent().parent().find("td:eq(2)").text();
        end = $(this).parent().parent().find("td:eq(3)").text();
        if(start != null && end != null){
            $("#startTime").val(start);
            $("#endTime").val(end);
        }
        // alert(curId);
        $('.time_install-modal').modal({
            backdrop:static,
            keyboard:true
        });
    });

    $(".scoTime_install_btn").click(function () {
        start = '${curriculums[0].scoStart}';
        end = '${curriculums[0].scoEnd}';
        if(start != null && end != null){
            $("#startTime").val(start);
            $("#endTime").val(end);
        }
        $('.time_install-modal').modal({
            backdrop:static,
            keyboard:true
        });
    });

    $(".time_save_btn").click(function () {
        var startTime = new Date($("#startTime").val());
        var endTime = new Date($("#endTime").val());
        var role ="<%=session.getAttribute("role")%>";
        alert(role)
        var link;
        if(endTime <= startTime){
            alert("结束时间不能小于开始时间!");
            return;
        }
        if("T" == role){
            link = "/hrms/cur/optTimeInstall/"+curId;
        }else{
            link = "/hrms/cur/scoTimeInstall?couId=${couId}&curYear=${curYear}";
        }
        $.ajax({
            url:link,
            type:"PUT",
            data:$(".time-install-form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("设置成功！");
                    $('.time_install-modal').modal("hide");
                    if("A" == role){
                        window.location.href = "/hrms/cur/getTeaGroByAdmin?couId=${couId}&curYear=${curYear}";
                    }
                }else {
                    $('.time_install-modal').modal("hide");
                    alert(result.extendInfo.timeInstall_err);
                }
            }
        });
    });
</script>
</body>
</html>

