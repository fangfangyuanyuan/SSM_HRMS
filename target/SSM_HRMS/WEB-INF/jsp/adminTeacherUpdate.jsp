<%--
  Created by IntelliJ IDEA.
  User: me
  Date: 2019/4/23
  Time: 23:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>教师更改页面</title>
</head>
<div class="modal fade tea-update-modal" tabindex="-1" role="dialog" aria-labelledby="tea-update-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">教师更改</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal update_tea_form">
                    <div class="form-group">
                        <label for="update_teaId" class="col-sm-2 control-label">教工号</label>
                        <div class="col-sm-8">
                            <input type="text" name="teaId" class="form-control" id="update_teaId" readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_teaName" class="col-sm-2 control-label">教师名称</label>
                        <div class="col-sm-8">
                            <input type="text" name="teaName" class="form-control" id="update_teaName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_teaMajor" class="col-sm-2 control-label">专业</label>
                        <div class="col-sm-8">
                            <input type="text" name="teaMajor" class="form-control" id="update_teaMajor">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_teaDept" class="col-sm-2 control-label">学院</label>
                        <div class="col-sm-8">
                            <input type="text" name="teaDept" class="form-control" id="update_teaDept">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_teaPass" class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-8">
                            <input type="text" name="teaPass" class="form-control" id="update_teaPass">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary tea_update_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<script type="text/javascript">
    <!-- ==========================教师新增操作=================================== -->
    //1 点击编辑按钮，发送AJAX请求查询对应id的部门信息，进行回显；
    //2 进行修改，点击更新按钮发送AJAX请求，将更改后的信息保存到数据库中；
    //3 跳转到当前更改页；
    var edit_teaId = 0;
    var curPageNo = ${curPageNo};

    $(".tea_edit_btn").click(function () {
        edit_teaId = $(this).parent().parent().find("td:eq(1)").text();
        alert("id"+edit_teaId);
        //查询对应deptId的部门信息
        $.ajax({
            url:"/hrms/tea/getTeaById/"+edit_teaId,
            type:"GET",
            success:function (result) {
                if (result.code == 100){
                    var teaData = result.extendInfo.teacher;
                    alert(teaData);
                    //回显
                    $("#update_teaId").val(teaData.teaId);
                    $("#update_teaName").val(teaData.teaName);
                    $("#update_teaMajor").val(teaData.teaMajor);
                    $("#update_teaDept").val(teaData.teaDept);
                    $("#update_teaPass").val(teaData.teaPass);
                }else {
                    alert(result.extendInfo.tea_get_error);
                }
            }
        });
    });

    $(".tea_update_btn").click(function () {
        $.ajax({
            url:"/hrms/tea/updateTea/"+edit_teaId,
            type:"PUT",
            data:$(".update_tea_form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("更新成功！");
                    window.location.href = "/hrms/tea/getTeaList?pageNo="+curPageNo;
                } else {
                    alert(result.extendInfo.tea_edit_error);
                }
            }

        });
    });
</script>
</body>
</html>
