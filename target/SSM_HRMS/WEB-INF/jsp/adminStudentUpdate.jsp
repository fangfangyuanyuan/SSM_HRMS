<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>学生更新</title>
</head>
<body>
<div class="modal fade stu-update-modal" tabindex="-1" role="dialog" aria-labelledby="stu-update-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">学生更改</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal update_stu_form">
                    <div class="form-group">
                        <label for="update_stuId" class="col-sm-2 control-label">学号</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuId" class="form-control" id="update_stuId" readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_stuName" class="col-sm-2 control-label">姓名</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuName" class="form-control" id="update_stuName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_stuClass" class="col-sm-2 control-label">班级</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuClass" class="form-control" id="update_stuClass">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_stuMajor" class="col-sm-2 control-label">专业</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuMajor" class="form-control" id="update_stuMajor">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_stuDept" class="col-sm-2 control-label">院系</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuDept" class="form-control" id="update_stuDept">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_stuPass" class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuPass" class="form-control" id="update_stuPass">
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary stu_update_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<script type="text/javascript">
    <!-- ==========================部门新增操作=================================== -->
    //1 点击编辑按钮，发送AJAX请求查询对应id的部门信息，进行回显；
    //2 进行修改，点击更新按钮发送AJAX请求，将更改后的信息保存到数据库中；
    //3 跳转到当前更改页；
    var edit_stuId= 0;
    var curPageNo = ${curPageNo};

    $(".stu_edit_btn").click(function () {
        edit_stuId = $(this).parent().parent().find("td:eq(1)").text();
        alert("id"+edit_stuId);
        //查询对应stuId的部门信息
        $.ajax({
            url:"/hrms/student/getStuById/"+edit_stuId,
            type:"GET",
            success:function (result) {
                if (result.code == 100){
                    var stuData = result.extendInfo.student;
                    //回显
                    $("#update_stuId").val(stuData.stuId);
                    $("#update_stuName").val(stuData.stuName);
                    $("#update_stuClass").val(stuData.stuClass);
                    $("#update_stuMajor").val(stuData.stuMajor);
                    $("#update_stuDept").val(stuData.stuDept);
                    $("#update_stuPass").val(stuData.stuPass);
                }else {
                    alert(result.extendInfo.stu_get_error);
                }
            }
        });
    });

    $(".stu_update_btn").click(function () {
        $.ajax({
            url:"/hrms/student/updateStu/"+edit_stuId,
            type:"PUT",
            data:$(".update_stu_form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("更新成功！");
                    // $('.stu-update-modal').modal("hide");
                    window.location.href = "/hrms/student/getStuList?pageNo="+curPageNo;
                } else {
                    alert(result.extendInfo.stu_edit_error);
                }
            }

        });
    });
</script>
</body>
</html>

