<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>课程管理页面</title>
    <%--<script type="text/javascript" src="d3.js/d3.min.js" charset="utf-8"></script>--%>
    <script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
</head>
<body>
<div class="hrms_course_container">
    <!-- 导航栏-->
    <%@ include file="./commom/head.jsp"%>


    <!-- 中间部分（左侧栏+表格内容） -->
    <div class="hrms_course_body">
        <!-- 左侧栏 -->
        <%@ include file="./commom/adminLeftsidebar.jsp"%>

        <!-- 部门表格内容 -->
        <div class="course_info col-sm-10">
            <div class="panel panel-success">
                <!-- 路径导航 -->
                <div class="panel-heading">
                    <ol class="breadcrumb">
                        <li><a href="#">成绩管理</a></li>
                        <li class="active">成绩图表</li>
                    </ol>
                </div>
                <!-- Table -->
            </div>
            <div class="panel-body">

                <h2>考试总体情况</h2><br>
                <table class="table table-bordered table-hover" id="course_table">
                    <thead>
                    <th>应考人数</th>
                    <th>缺考人数</th>
                    <th>实考人数</th>
                    <th>最高分</th>
                    <th>最低分</th>
                    </thead>
                    <tbody>
                        <tr>
                            <td>${g.totalPeople}</td>
                            <td>${g.absentees}</td>
                            <td>${g.actualPeople}</td>
                            <td>${g.maxSco}</td>
                            <td>${g.minSco}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="panel-body">
                <h2>目标达成情况</h2><br>
                <table class="table table-bordered table-hover" id="sco_table">
                    <thead>
                    <th>目标</th>
                    <th>成绩一</th>
                    <th>成绩二</th>
                    <th>成绩三</th>
                    <th>总成绩</th>
                    </thead>
                    <tbody>
                    <tr>
                        <td>平均分</td>
                        <td>${g.avg1}</td>
                        <td>${g.avg2}</td>
                        <td>${g.avg3}</td>
                        <td>${g.avgTotal}</td>
                    </tr>
                    <tr>
                        <td>标准差</td>
                        <td>${g.stdScore1}</td>
                        <td>${g.stdScore2}</td>
                        <td>${g.stdScore3}</td>
                        <td>${g.stdTotal}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="panel-body">
                <h2>成绩分布饼状图</h2>
                <div class="gradeChart" width="350" height="350">
                </div>
            </div>
            <!-- /.panel panel-success -->
        </div><!-- /.course_info -->
    </div><!-- /.hrms_course_body -->
</div><!-- /.hrms_course_container -->

<script type="text/javascript">
    var width=350;
    var height=350;
    var svg=d3.select(".gradeChart")
        .append("svg")
        .attr("width",width)
        .attr("height",height);

    var dataset=[ ["优",${g.great}] , ["良",${g.good}] , ["中",${g.general}] , ["差",${g.weak}] ];

    //转换数据
    var pie=d3.layout.pie() //创建饼状图
        .value(function(d){
            return d[1];
        });//值访问器
    //dataset为转换前的数据 piedata为转换后的数据
    var piedata=pie(dataset);

    //绘制
    var outerRadius=width/3;
    var innerRadius=0;//内半径和外半径

    //创建弧生成器
    var arc=d3.svg.arc()
        .innerRadius(innerRadius)
        .outerRadius(outerRadius);
    var color=d3.scale.category20();
    //添加对应数目的弧组
    var arcs=svg.selectAll("g")
        .data(piedata)
        .enter()
        .append("g")
        .attr("transform","translate("+(width/2)+","+(height/2)+")");
    //添加弧的路径元素
    arcs.append("path")
        .attr("fill",function(d,i){
            return color(i);
        })
        .attr("d",function(d){
            return arc(d);//使用弧生成器获取路径
        });
    //添加弧内的文字
    arcs.append("text")
        .attr("transform",function(d){
            var x=arc.centroid(d)[0]*1.4;//文字的x坐标
            var y=arc.centroid(d)[1]*1.4;
            return "translate("+x+","+y+")";
        })
        .attr("text-anchor","middle")
        .text(function(d){
            //计算市场份额的百分比
            var percent=Number(d.value)/d3.sum(dataset,function(d){
                return d[1];
            })*100;
            //保留一位小数点 末尾加一个百分号返回
            return percent.toFixed(1)+"%";
        });
    //添加连接弧外文字的直线元素
    arcs.append("line")
        .attr("stroke","black")
        .attr("x1",function(d){
            return arc.centroid(d)[0]*2;
        })
        .attr("y1",function(d){
            return arc.centroid(d)[1]*2;
        })
        .attr("x2",function(d){
            return arc.centroid(d)[0]*2.2;
        })
        .attr("y2",function(d){
            return arc.centroid(d)[1]*2.2;
        });

    var fontsize=14;
    arcs.append("line")
        .style("stroke","black")
        .each(function(d){
            d.textLine={x1:0,y1:0,x2:0,y2:0};
        })
        .attr("x1",function(d){
            d.textLine.x1=arc.centroid(d)[0]*2.2;
            return d.textLine.x1;
        })
        .attr("y1",function(d){
            d.textLine.y1=arc.centroid(d)[1]*2.2;
            return d.textLine.y1;
        })
        .attr("x2",function(d){
            // console.log("d.data[0]:  "+d.data[0]);//产商名
            var strLen=getPixelLength(d.data[0],fontsize)*1.5;
            var bx=arc.centroid(d)[0]*2.2;
            d.textLine.x2=bx>=0?bx+strLen:bx-strLen;
            return d.textLine.x2;
        })
        .attr("y2",function(d){
            d.textLine.y2=arc.centroid(d)[1]*2.2;
            return d.textLine.y2;
        });

    arcs.append("text")
        .attr("transform",function(d){
            var x=0;
            var y=0;
            x=(d.textLine.x1+d.textLine.x2)/2;
            y=d.textLine.y1;
            y=y>0?y+fontsize*1.1:y-fontsize*0.4;
            return "translate("+x+","+y+")";
        })
        .style("text-anchor","middle")
        .style("font-size",fontsize)
        .text(function(d){
            return d.data[0];
        });

    //添加一个提示框
    var tooltip=d3.select("body")
        .append("div")
        .attr("class","tooltip")
        .style("opacity",0.0);

    arcs.on("mouseover",function(d,i){
        /*
        鼠标移入时，
        （1）通过 selection.html() 来更改提示框的文字
        （2）通过更改样式 left 和 top 来设定提示框的位置
        （3）设定提示框的透明度为1.0（完全不透明）
        */
        console.log(d.data[0]+"的人数为"+"<br />"+d.data[1]+" 人")
        tooltip.html(d.data[0]+"的人数为"+"<br />"+d.data[1]+" 人")
            .style("left",(d3.event.pageX)+"px")
            .style("top",(d3.event.pageY+20)+"px")
            .style("opacity",1.0);
        tooltip.style("box-shadow","10px 0px 0px"+color(i));//在提示框后添加阴影
    })
        .on("mousemove",function(d){
            /* 鼠标移动时，更改样式 left 和 top 来改变提示框的位置 */
            tooltip.style("left",(d3.event.pageX)+"px")
                .style("top",(d3.event.pageY+20)+"px");
        })
        .on("mouseout",function(d){
            //鼠标移除 透明度设为0
            tooltip.style("opacity",0.0);
        })

    function getPixelLength(str,fontsize){
        var curLen=0;
        for(var i=0;i<str.length;i++){
            var code=str.charCodeAt(i);
            var pixelLen=code>255?fontsize:fontsize/2;
            curLen+=pixelLen;
        }
        return curLen;
    }
</script>

</body>
</html>
