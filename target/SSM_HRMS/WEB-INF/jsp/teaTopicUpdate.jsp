<%--
  Created by IntelliJ IDEA.
  User: me
  Date: 2019/4/30
  Time: 11:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>课题更新</title>
</head>
<body>
<div class="modal fade top-update-modal" tabindex="-1" role="dialog" aria-labelledby="top-update-modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">课题更新</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal update_top_form">
                    <div class="form-group">
                        <label for="update_topId" class="col-sm-2 control-label">课题编号</label>
                        <div class="col-sm-8">
                            <input type="text" name="topId" class="form-control" id="update_topId" readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_topName" class="col-sm-2 control-label">课题名称</label>
                        <div class="col-sm-8">
                            <input type="text" name="topName" class="form-control" id="update_topName" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_topNature" class="col-sm-2 control-label">课题性质</label>
                        <div class="col-sm-8">
                            <input type="text" name="topNature" class="form-control" id="update_topNature" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="update_topIntroduce" class="col-sm-2 control-label">课题介绍</label>
                        <div class="col-sm-8">
                            <input type="text" name="topIntroduce" class="form-control" id="update_topIntroduce">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary top_update_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<script type="text/javascript">
    <!-- ==========================课题操作=================================== -->
    //1 点击编辑按钮，发送AJAX请求查询对应id的部门信息，进行回显；
    //2 进行修改，点击更新按钮发送AJAX请求，将更改后的信息保存到数据库中；
    //3 跳转到当前更改页；
    var edit_topId = 0;
    <%--var curPageNo = ${curPageNo};--%>

    $(".top_edit_btn").click(function () {
        edit_topId = $(this).parent().parent().find("td:eq(1)").text();
        alert("id:"+edit_topId);
        //查询对应deptId的部门信息
        $.ajax({
            url:"/hrms/topic/getTopById/"+edit_topId,
            type:"GET",
            success:function (result) {
                if (result.code == 100){
                    var topData = result.extendInfo.topic;
                    var curId = topData.curId;
                    alert(curId);
                    //回显
                    $("#update_topId").val(topData.topId);
                    $("#update_topName").val(topData.topName);
                    $("#update_topNature").val(topData.topNature);
                    $("#update_topIntroduce").val(topData.topIntroduce);
                }else {
                    alert(result.extendInfo.top_get_error);
                }
            }
        });
    });

    $(".top_update_btn").click(function () {
        $.ajax({
            url:"/hrms/topic/updateTop/"+edit_topId,
            type:"PUT",
            data:$(".update_top_form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("更新成功！");
                    window.location.href = "/hrms/topic/getTopList?curId=${curId}";
                } else {
                    alert(result.extendInfo.top_edit_error);
                }
            }

        });
    });
</script>
</body>
</html>
