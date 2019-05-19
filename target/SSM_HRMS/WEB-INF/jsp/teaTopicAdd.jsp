<%--
  Created by IntelliJ IDEA.
  User: me
  Date: 2019/5/1
  Time: 10:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加课题</title>
</head>
<body>
<div class="modal fade top_add_modal" tabindex="-1" role="dialog" aria-labelledby="top_add_modal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">课题添加</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal add_top_form">
                    <div class="form-group">
                        <label for="add_topName" class="col-sm-2 control-label">课题名称</label>
                        <div class="col-sm-8">
                            <input type="text" name="topName" class="form-control" id="add_topName" placeholder="长度:(0,25]" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_topNature" class="col-sm-2 control-label">课题性质</label>
                        <div class="col-sm-8">
                            <input type="text" name="topNature" class="form-control" id="add_topNature" placeholder="长度:(0,10]" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="add_topIntroduce" class="col-sm-2 control-label">课题简介</label>
                        <div class="col-sm-8">
                            <%--<input type="text" name="couIntroduce" class="form-control" id="add_couIntroduce" placeholder="XXX">--%>
                            <textarea name="topIntroduce" id="add_topIntroduce"  class="form-control"  placeholder="长度:(0,50]" cols="30" rows="5" ></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary top_save_btn">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript">
    var curId = ${curId};

    $(".top_add_btn").click(function () {
        $('.top_add_modal').modal({
            backdrop:static,
            keyboard:true
        });
    });

    $(".top_save_btn").click(function () {
        var topName = $("#add_topName").val();
        var topNature = $("#add_topNature").val();
        var topIntroduce = $('#add_topIntroduce').val();

        if(topName.length >50)
        {
            alert("请检查课题名称格式！");
            return;
        }
        if(topNature.length >20)
        {
            alert("请检查课题性质格式！");
            return;
        }
        if(topIntroduce.length > 100 )
        {
            alert("请检查课题简介格式！");
            return;
        }

        $.ajax({
            url:"/hrms/topic/addTop/"+curId,
            type:"POST",
            data:$(".add_top_form").serialize(),
            success:function (result) {
                if(result.code == 100){
                    alert("课题提添加成功！");
                    $('.top_add_modal').modal("hide");
                    window.location.href="/hrms/topic/getTopList?curId="+curId;
                }else {
                    alert(result.extendInfo.top_draft_error);
                    $('.top_add_modal').modal("hide");
                }
            }
        });
    });
</script>
</body>
</html>