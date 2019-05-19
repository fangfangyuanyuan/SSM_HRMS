<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>课程新增</title>
</head>
<body>
<div class="modal fade course-add-modal" tabindex="-1" role="dialog" aria-labelledby="course-add-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">课程新增</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal add_cou_form">
                    <div class="form-group">
                        <label for="add_courseId" class="col-sm-2 control-label">课程编号</label>
                        <div class="col-sm-8">
                            <input type="text" name="couId" class="form-control" id="add_courseId" placeholder="长度:10" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_couName" class="col-sm-2 control-label">课程名称</label>
                        <div class="col-sm-8">
                            <input type="text" name="couName" class="form-control" id="add_couName" placeholder="长度：（0,15]" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_couNature" class="col-sm-2 control-label">课程性质</label>
                        <div class="col-sm-8">
                            <input type="text" name="couNature" class="form-control" id="add_couNature" placeholder="必修课/选修课" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_couCredit" class="col-sm-2 control-label">学分</label>
                        <div class="col-sm-8">
                            <input type="text" name="couCredit" class="form-control" id="add_couCredit" placeholder="范围:(0,5]" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_couDept" class="col-sm-2 control-label">开设院系</label>
                        <div class="col-sm-8">
                            <input type="text" name="couDept" class="form-control" id="add_couDept" placeholder="长度：(0,40]" required>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary cou_save_btn">保存</button>
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
    $(".cou_add_btn").click(function () {
        $('.course-add-modal').modal({
            backdrop:static,
            keyboard:true
        });

    });

    $(".cou_save_btn").click(function () {
        var re = /^[0-9]+.?[0-9]*$/;
        var couId = $.trim($('#add_courseId').val());
        var couName = $.trim($('#add_couName').val());
        var couNature = $.trim($('#add_couNature').val());
        var couCredit = $.trim($('#add_couCredit').val());
        var couDept = $.trim($('#add_couDept').val());
        if(couId.length != 10 || !re.test(couId)){
            alert("课请检查课程编号格式！");
            return;
        }
        if(couName.length >30)
        {
            alert("请检查课程名称格式！");
            return;
        }
        if(couNature != "选修课" && couNature!="必修课")
        {
            alert("请检查课程性质格式！");
            return;
        }
        if(couCredit <3 && couCredit >5 && !re.test(couCredit))
        {
            alert("请检查学分格式！");
            return;
        }
        if(couDept.length > 40 )
        {
            alert("请检查开设院校格式！");
            return;
        }

        alert(couId);
        //验证省略...
        $.ajax({
            url:"/hrms/course/addCou",
            type:"POST",
            data:$(".add_cou_form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("新增成功");
                    $('.course-add-modal').modal("hide");
                    $.ajax({
                        url:"/hrms/course/getTotalPages",
                        type:"GET",
                        success:function (result) {
                            if (result.code == 100){
                                var totalPage = result.extendInfo.totalPages;
                                window.location.href="/hrms/course/getCouList?pageNo="+totalPage;
                            }
                        }
                    });
                }else {
                    alert(result.extendInfo.cou_add_error);
                }
            }
        });
    });
</script>
</body>
</html>
