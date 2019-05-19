<%--
  Created by IntelliJ IDEA.
  User: me
  Date: 2019/4/23
  Time: 11:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>学生新增</title>
</head>
<body>
<div class="modal fade stu-add-modal" tabindex="-1" role="dialog" aria-labelledby="stu-add-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">学生新增</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal add_stu_form">
                    <div class="form-group">
                        <label for="add_stuId" class="col-sm-2 control-label">学号</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuId" class="form-control" id="add_stuId" placeholder="长度：12" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_stuName" class="col-sm-2 control-label">姓名</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuName" class="form-control" id="add_stuName" placeholder="长度:(0,15]" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_stuClass" class="col-sm-2 control-label">班级</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuClass" class="form-control" id="add_stuClass" placeholder="长度:4" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_stuMajor" class="col-sm-2 control-label">专业</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuMajor" class="form-control" id="add_stuMajor" placeholder="长度:(0,15]" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_stuDept" class="col-sm-2 control-label">院系</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuDept" class="form-control" id="add_stuDept" placeholder="长度:(0,20]" required>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary stu_save_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script type="text/javascript">
    <!-- ==========================部门新增操作=================================== -->
    // 为简单操作，省去了输入名称的验证、错误信息提示等操作
    //1 点击部门新增按钮，弹出模态框；
    //2 输入新增部门信息，点击保存按钮，发送AJAX请求到后台进行保存；
    //3 保存成功跳转最后一页
    $(".stu_add_btn").click(function () {
        $('.stu-add-modal').modal({
            backdrop:static,
            keyboard:true
        });

    });

    $(".stu_save_btn").click(function () {
        var re = /^[0-9]+.?[0-9]*$/;
        var stuId = $('#add_stuId').val();
        var stuName = $('#add_stuName').val();
        var stuClass = $('#add_stuClass').val();
        var stuMajor = $('#add_stuMajor').val();
        var stuDept = $('#add_stuDept').val();

        if(stuId.length != 12 || !re.test(stuId)){
            alert("请检查学号格式！");
            return;
        }
        if(stuName.length >30)
        {
            alert("请检查学生姓名格式！");
            return;
        }
        if(stuClass.length != 4 || !re.test(stuClass))
        {
            alert("请检查班级格式！");
            return;
        }
        if(stuMajor.length >30 )
        {
            alert("请检查专业格式！");
            return;
        }
        if(stuDept.length > 40 )
        {
            alert("请检查院系格式！");
            return;
        }
        $.ajax({
            url:"/hrms/student/addStu",
            type:"PUT",
            data:$(".add_stu_form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("新增成功");
                    $('.stu-add-modal').modal("hide");
                    $.ajax({
                        url:"/hrms/student/getTotalPages",
                        type:"GET",
                        success:function (result) {
                            if (result.code == 100){
                                var totalPage = result.extendInfo.totalPages;
                                window.location.href="/hrms/student/getStuList?pageNo="+totalPage;
                            }
                        }
                    });
                }else {
                    alert(result.extendInfo.stu_add_error);
                }
            }
        });
    });
</script>
</body>
</html>

