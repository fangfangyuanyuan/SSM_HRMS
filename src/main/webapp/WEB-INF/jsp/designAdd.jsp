<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>课程设计新增页面</title>
    <script src="/static/js/myJsFile.js"></script>
</head>
<body>
<div class="modal fade desi-add-modal" tabindex="-1" role="dialog" aria-labelledby="desi-add-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">课程设计新增</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal add_desi_form">
                    <div class="form-group">
                        <label for="add_stuyear" class="col-sm-2 control-label">学年</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuyear" class="form-control" id="add_stuyear" placeholder="2019">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_term" class="col-sm-2 control-label">学期</label>
                        <div class="col-sm-8">
                            <input type="text" name="term" class="form-control" id="add_term" placeholder="1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_cid" class="col-sm-2 control-label">课程号</label>
                        <div class="col-sm-8">
                            <input type="text" name="c_id" class="form-control" id="add_cid" placeholder="1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_tid" class="col-sm-2 control-label">教工号</label>
                        <div class="col-sm-8">
                            <input type="text" name="t_id" class="form-control" id="add_tid" placeholder="1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_sid" class="col-sm-2 control-label">学生号</label>
                        <div class="col-sm-8">
                            <input type="text" name="s_id" class="form-control" id="add_sid" placeholder="1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_dscore" class="col-sm-2 control-label">设计成绩</label>
                        <div class="col-sm-8">
                            <input type="text" name="d_score" class="form-control" id="add_dscore" placeholder="1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_ascore" class="col-sm-2 control-label">答辩成绩</label>
                        <div class="col-sm-8">
                            <input type="text" name="a_score" class="form-control" id="add_ascore" placeholder="1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_rscore" class="col-sm-2 control-label">报告成绩</label>
                        <div class="col-sm-8">
                            <input type="text" name="r_score" class="form-control" id="add_rscore" placeholder="1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_total" class="col-sm-2 control-label">总成绩</label>
                        <div class="col-sm-8">
                            <input type="text" name="total" class="form-control" id="add_total" placeholder="1">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary desi_save_btn">保存</button>
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
    $(".desi_add_btn").click(function () {
        $('.desi-add-modal').modal({
            backdrop:static,
            keyboard:true
        });

    });

    $(".desi_save_btn").click(function () {
        var stuyear = $("#add_stuyear").val();
        var term = $("#add_term").val();
        var cid = $("#add_cid").val();
        var tid = $("#add_tid").val();
        var sid = $("#add_sid").val();
        var dscore = $("#add_dscore").val();
        var ascore = $("#add_ascore").val();
        var rscore = $("#add_rscore").val();
        var total = $("#add_total").val();
        //验证省略...
        $.ajax({
            url:"/hrms/desi/addDesi",
            type:"PUT",
            data:$(".add_desi_form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("新增成功");
                    $('.desi-add-modal').modal("hide");
                    $.ajax({
                        url:"/hrms/desi/getTotalPages",
                        type:"GET",
                        success:function (result) {
                            if (result.code == 100){
                                var totalPage = result.extendInfo.totalPages;
                                window.location.href="/hrms/desi/getDesiList?pageNo="+totalPage;
                            }
                        }
                    });
                }else {
                    alert(result.extendInfo.add_desi_error);
                }
            }
        });
    });

</script>
</body>
</html>
