<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>成绩更新</title>
</head>
<body>
<div class="modal fade sco-update-modal" tabindex="-1" role="dialog" aria-labelledby="sco-update-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">成绩更新</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal update_top_form">
                    <div class="form-group">
                        <label for="update_stuId" class="col-sm-2 control-label">学号</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuId" class="form-control" id="update_stuId" readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_stuName" class="col-sm-2 control-label">姓名</label>
                        <div class="col-sm-8">
                            <input type="text" name="stuName" class="form-control" id="update_stuName" readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_topName" class="col-sm-2 control-label">课题</label>
                        <div class="col-sm-8">
                            <input type="text" name="topName" class="form-control" id="update_topName" readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_score1" class="col-sm-2 control-label">成绩一</label>
                        <div class="col-sm-8">
                            <input type="text" name="score1" class="form-control" id="update_score1" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_score2" class="col-sm-2 control-label">成绩二</label>
                        <div class="col-sm-8">
                            <input type="text" name="score2" class="form-control" id="update_score2" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_score3" class="col-sm-2 control-label">成绩三</label>
                        <div class="col-sm-8">
                            <input type="text" name="score3" class="form-control" id="update_score3" required>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary sco_update_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<script type="text/javascript">
    <!-- ==========================课题操作=================================== -->
    //1 点击编辑按钮，发送AJAX请求查询对应id的部门信息，进行回显；
    //2 进行修改，点击更新按钮发送AJAX请求，将更改后的信息保存到数据库中；
    //3 跳转到当前更改页；
    // var edit_groId = 0;
    var curId = 0;
    var curPageNo = ${curPageNo};

    $(".sco_edit_btn").click(function () {
        edit_groId = $(this).parent().parent().find("td:eq(1)").text();
        // alert("id:"+edit_groId);
        //查询对应deptId的部门信息
        $.ajax({
            url:"/hrms/group/getGroById/"+edit_groId,
            type:"GET",
            success:function (result) {
                if (result.code == 100){
                    var topData = result.extendInfo.group;
                    var curId = topData.curId;
                    // alert(curId);
                    //回显
                    $("#update_stuId").val(topData.stuId);
                    $("#update_stuName").val(topData.stuName);
                    $("#update_topName").val(topData.topName);
                    $("#update_score1").val(topData.score1);
                    $("#update_score2").val(topData.score2);
                    $("#update_score3").val(topData.score3);
                }else {
                    alert(result.extendInfo.sco_get_err);
                }
            }
        });
    });

    $(".sco_update_btn").click(function () {
        $.ajax({
            url:"/hrms/group/updateScore/"+edit_groId,
            type:"PUT",
            data:$(".update_top_form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("更新成功！");
                    window.location.href = "/hrms/group/getScoListByTea?curId=${curId}";
                } else {
                    alert(result.extendInfo.sco_update_error);
                }
            }

        });
    });
</script>
</body>
</html>
