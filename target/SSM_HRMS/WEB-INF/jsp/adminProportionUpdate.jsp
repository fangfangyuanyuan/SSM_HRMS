<%--
  Created by IntelliJ IDEA.
  User: me
  Date: 2019/4/20
  Time: 16:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>课程编辑</title>
</head>
<body>
<div class="modal fade count-update-modal" tabindex="-1" role="dialog" aria-labelledby="count-update-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">成绩比例编辑</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal update_cur_form">
                    <div class="form-group">
                        <label for="update_curName" class="col-sm-2 control-label">课程名称</label>
                        <div class="col-sm-8">
                            <input type="text" name="curName" class="form-control" id="update_curName" readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_teaName" class="col-sm-2 control-label">教师名字</label>
                        <div class="col-sm-8">
                            <input type="text" name="teaName" class="form-control" id="update_teaName" readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_sco1" class="col-sm-2 control-label">比例一</label>
                        <div class="col-sm-8">
                            <input type="text" name="sco1Count" class="form-control" id="update_sco1" required="required">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_sco2" class="col-sm-2 control-label">比例二</label>
                        <div class="col-sm-8">
                            <input type="text" name="sco2Count" class="form-control" id="update_sco2" required="required">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_sco3" class="col-sm-2 control-label">比例三</label>
                        <div class="col-sm-8">
                            <input type="text" name="sco3Count" class="form-control" id="update_sco3" required="required">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary cou_update_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<script type="text/javascript">
    var edit_curId = 0;
    var curPageNo = ${curPageNo};

    $(".count_edit_btn").click(function () {
        edit_curId = $(this).parent().parent().find("td:eq(1)").text();
        // alert("id"+edit_couId);
        //查询对应deptId的部门信息
        $.ajax({
            url:"/hrms/cur/getCurById/"+edit_curId,
            type:"GET",
            success:function (result) {
                if (result.code == 100){
                    var cur = result.extendInfo.cur;
                    //回显
                    $("#update_curName").val(cur.curYear+"学年"+cur.couName);
                    $("#update_teaName").val(cur.teaName);
                    $("#update_sco1").val(cur.sco1Count);
                    $("#update_sco2").val(cur.sco2Count);
                    $("#update_sco3").val(cur.sco3Count);
                }else {
                    alert(result.extendInfo.cur_get_error);
                }
            }
        });
    });

    $(".cou_update_btn").click(function () {
        var sco1 = $.trim($('#update_sco1').val());
        var sco2 = $.trim($('#update_sco2').val());
        var sco3 = $.trim($('#update_sco3').val());
        $.ajax({
            url:"/hrms/cur/updatePartition/"+edit_curId,
            type:"PUT",
            data:$(".update_cur_form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("更新成功！");
                    window.location.href = "/hrms/cur/getTeaGroByAdmin?pageNo=${curPageNo}&couId=${couId}&curYear=${curYear}";
                } else {
                    alert(result.extendInfo.partition_update_error);
                }
            }
        });
    });


</script>
</body>
</html>
