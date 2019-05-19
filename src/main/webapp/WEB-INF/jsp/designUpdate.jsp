<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>课程设计更改页面</title>
    <script src="/static/js/myJsFile.js"></script>
</head>
<body>
<div class="modal fade desi-update-modal" tabindex="-1" role="dialog" aria-labelledby="desi-update-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">课程设计更改</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal update_desi_form">
                    <div class="form-group">
                        <label for="update_desiStuyear" class="col-sm-2 control-label">学年</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuyear" class="form-control" id="update_desiStuyear">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_desiTerm" class="col-sm-2 control-label">学期</label>
                        <div class="col-sm-8">
                            <input type="text" name="term" class="form-control" id="update_desiTerm">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_desiCid" class="col-sm-2 control-label">课程编号</label>
                        <div class="col-sm-8">
                            <input type="text" name="c_id" class="form-control" id="update_desiCid">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="update_desiTid" class="col-sm-2 control-label">教师编号</label>
                        <div class="col-sm-8">
                            <input type="text" name="t_id" class="form-control" id="update_desiTid">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_desiSid" class="col-sm-2 control-label">学生编号</label>
                        <div class="col-sm-8">
                            <input type="text" name="s_id" class="form-control" id="update_desiSid">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_desiDscore" class="col-sm-2 control-label">设计成绩</label>
                        <div class="col-sm-8">
                            <input type="text" name="d_score" class="form-control" id="update_desiDscore">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_desiAscore" class="col-sm-2 control-label">答辩成绩</label>
                        <div class="col-sm-8">
                            <input type="text" name="a_score" class="form-control" id="update_desiAscore">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_desiRscore" class="col-sm-2 control-label">报告成绩</label>
                        <div class="col-sm-8">
                            <input type="text" name="r_score" class="form-control" id="update_desiRscore">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_desiTotal" class="col-sm-2 control-label">总成绩</label>
                        <div class="col-sm-8">
                            <input type="text" name="total" class="form-control" id="update_desiTotal">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary desi_update_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<script type="text/javascript">
    <!-- ==========================部门新增操作=================================== -->
    //1 点击编辑按钮，发送AJAX请求查询对应id的部门信息，进行回显；
    //2 进行修改，点击更新按钮发送AJAX请求，将更改后的信息保存到数据库中；
    //3 跳转到当前更改页；
    var edit_desiId = 0;
    var curPageNo = ${curPageNo};

    $(".desi_edit_btn").click(function () {
        edit_desiId = $(this).parent().parent().find("td:eq(0)").text();
        alert("id" + edit_desiId);
        //查询对应deptId的部门信息
        $.ajax({
            url:"/hrms/desi/getDesiById/"+edit_desiId,
            type:"GET",
            success:function (result) {
                if (result.code == 100){
                    var desiData = result.extendInfo.design;
                    //回显
                    $("#update_desiStuyear").val(desiData.stuyear);
                    $("#update_desiTerm").val(desiData.term);
                    $("#update_desiCid").val(desiData.c_id);
                    $("#update_desiTid").val(desiData.t_id);
                    $("#update_desiSid").val(desiData.s_id);
                    $("#update_desiDscore").val(desiData.d_score);
                    $("#update_desiAscore").val(desiData.a_score);
                    $("#update_desiRscore").val(desiData.r_score);
                    $("#update_desiTotal").val(desiData.total);
                }else {
                    alert(result.extendInfo.get_desi_error);
                }
            }
        });
    });

    $(".desi_update_btn").click(function () {
        alert("id" + edit_desiId);
        $.ajax({
            url:"/hrms/desi/updateDesiById/"+edit_desiId,
            type:"PUT",
            data:$(".update_desi_form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("更新成功！");
                    window.location.href = "/hrms/desi/getDesiList?pageNo="+curPageNo;
                } else {
                    alert(result.extendInfo.update_desi_error);
                }
            }

        });
    });


</script>
</body>
</html>
