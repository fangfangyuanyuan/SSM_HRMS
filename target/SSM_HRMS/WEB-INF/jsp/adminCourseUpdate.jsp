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
<div class="modal fade cou-update-modal" tabindex="-1" role="dialog" aria-labelledby="cou-update-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">课程编辑</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal update_cou_form">
                    <div class="form-group">
                        <label for="update_couId" class="col-sm-2 control-label">课程编号</label>
                        <div class="col-sm-8">
                            <input type="text" name="couId" class="form-control" id="update_couId" readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_couName" class="col-sm-2 control-label">课程名称</label>
                        <div class="col-sm-8">
                            <input type="text" name="couName" class="form-control" id="update_couName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_couNature" class="col-sm-2 control-label">课程性质</label>
                        <div class="col-sm-8">
                            <input type="text" name="couNature" class="form-control" id="update_couNature">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_couCredit" class="col-sm-2 control-label">学分</label>
                        <div class="col-sm-8">
                            <input type="text" name="couCredit" class="form-control" id="update_couCredit">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_couDept" class="col-sm-2 control-label">开设院系</label>
                        <div class="col-sm-8">
                            <input type="text" name="couDept" class="form-control" id="update_couDept">
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
    var edit_couId = 0;
    var curPageNo = ${curPageNo};

    $(".cou_edit_btn").click(function () {
        edit_couId = $(this).parent().parent().find("td:eq(1)").text();
        // alert("id"+edit_couId);
        //查询对应deptId的部门信息
        $.ajax({
            url:"/hrms/course/getCouByCouId/"+edit_couId,
            type:"GET",
            success:function (result) {
                if (result.code == 100){
                    var course = result.extendInfo.course;
                    //回显
                    $("#update_couId").val(course.couId);
                    $("#update_couName").val(course.couName);
                    $("#update_couYear").val(course.couYear);
                    $("#update_couTerm").val(course.couTerm);
                    $("#update_couNature").val(course.couNature);
                    $("#update_couCredit").val(course.couCredit);
                    $("#update_couDept").val(course.couDept);
                }else {
                    alert(result.extendInfo.cou_get_error);
                }
            }
        });
    });

    $(".cou_update_btn").click(function () {
        $.ajax({
            url:"/hrms/course/updateCou/"+edit_couId,
            type:"PUT",
            data:$(".update_cou_form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("更新成功！");
                    window.location.href = "/hrms/course/getCouList?pageNo="+curPageNo;
                } else {
                    alert(result.extendInfo.cou_update_error);
                }
            }
        });
    });


</script>
</body>
</html>
