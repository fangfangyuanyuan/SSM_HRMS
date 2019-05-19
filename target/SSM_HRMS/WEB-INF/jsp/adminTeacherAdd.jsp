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
    <title>教师增加</title>
</head>
<body>
<div class="modal fade tea-add-modal" tabindex="-1" role="dialog" aria-labelledby="tea-add-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">教师新增</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal add_tea_form">
                    <div class="form-group">
                        <label for="add_teaId" class="col-sm-2 control-label">教工号</label>
                        <div class="col-sm-8">
                            <input type="text" name="teaId" class="form-control" id="add_teaId" placeholder="长度:8" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_teaName" class="col-sm-2 control-label">名字</label>
                        <div class="col-sm-8">
                            <input type="text" name="teaName" class="form-control" id="add_teaName" placeholder="长度:(0,15]" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_teaMajor" class="col-sm-2 control-label">专业</label>
                        <div class="col-sm-8">
                            <input type="text" name="teaMajor" class="form-control" id="add_teaMajor" placeholder="长度:(0,15]" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_teaDept" class="col-sm-2 control-label">学院</label>
                        <div class="col-sm-8">
                            <input type="text" name="teaDept" class="form-control" id="add_teaDept" placeholder="长度:(0,20]" required>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary tea_save_btn">保存</button>
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
    $(".tea_add_btn").click(function () {
        $('.tea-add-modal').modal({
            backdrop:static,
            keyboard:true
        });

    });

    $(".tea_save_btn").click(function () {
        var re = /^[0-9]+.?[0-9]*$/;
        var teaId = $("#add_teaId").val();
        var teaName = $("#add_teaName").val();
        var teaMajor = $('#add_teaMajor').val();
        var teaDept = $('#add_teaDept').val();

        if(teaId.length != 8 || !re.test(teaId)){
            alert("请检查教工号格式！");
            return;
        }
        if(teaName.length >30)
        {
            alert("请检查教师姓名格式！");
            return;
        }
        if(teaMajor.length >30 )
        {
            alert("请检查专业格式！");
            return;
        }
        if(teaDept.length > 40 )
        {
            alert("请检查学院格式！");
            return;
        }
        $.ajax({
            url:"/hrms/tea/addTea",
            type:"PUT",
            data:$(".add_tea_form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("新增成功");
                    $('.tea-add-modal').modal("hide");
                    $.ajax({
                        url:"/hrms/tea/getTotalPages",
                        type:"GET",
                        success:function (result) {
                            if (result.code == 100){
                                var totalPage = result.extendInfo.totalPages;
                                window.location.href="/hrms/tea/getTeaList?pageNo="+totalPage;
                            }
                        }
                    });
                }else {
                    alert(result.extendInfo.add_dept_error);
                }
            }
        });
    });
</script>
</body>
</html>


