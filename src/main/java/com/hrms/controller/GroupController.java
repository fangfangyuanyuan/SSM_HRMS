package com.hrms.controller;

import com.hrms.bean.*;
import com.hrms.util.ExcelUtil;
import com.hrms.util.FileToZip;
import com.hrms.service.CourseService;
import com.hrms.service.CurriculumService;
import com.hrms.service.GroupService;
import com.hrms.service.TopicService;
import com.hrms.util.JsonMsg;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.PageData;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/hrms/group")
public class GroupController {

    @Autowired
    GroupService groupService;
    @Autowired
    TopicService topicService;
    @Autowired
    CourseService courseService;
    @Autowired
    CurriculumService curriculumService;

    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    //    管理员删除学生和课程关系
    @RequestMapping(value = "/delGro/{groId}", method = RequestMethod.DELETE)
    @ResponseBody
    JsonMsg delGro(@PathVariable("groId")String groId){
        int res = 0;
        Group group;
        if(groId.length() > 0){
            group = groupService.selectGroById(groId);
            if(group.getTopId() != null )
                return JsonMsg.fail().addInfo("gro_del_err","不能删除该分组，学生已经选题！");

            res = groupService.delGro(groId);
            if(res != 1){
                return JsonMsg.fail().addInfo("gro_del_err","教师学生分组删除异常");
            }
            return JsonMsg.success();
        }
        return JsonMsg.fail().addInfo("gro_del_err","学生分组编号异常");
    }

    //添加选题
    @RequestMapping(value = "/AddOptTopic",method = RequestMethod.PUT)
    @ResponseBody
    public JsonMsg AddOptTopic(@RequestParam(value = "topId") String topId,
                            @RequestParam(value = "curId") String curId, HttpSession session){
        Student student = (Student) session.getAttribute("student");
        int res = 0;
        if(topId.length() > 0 && curId.length() > 0 && student != null){
            //判断登陆的学生是否已经选过课题
            Group group =  groupService.selGroByCurIdAndStuId(curId,student.getStuId());
            System.out.println("group:"+ group.toString());
            if(group.getTopId() != null){
                return JsonMsg.fail().addInfo("top_opt_error","选题失败,你已经选过课题！");
            }
           //判断该课题是否已经有人选过
            Group group1 = groupService.selectByTopId(topId);
            System.out.println("topId:"+topId);
//            System.out.println("group1:"+group1.toString());
            if(group1 != null){
                return JsonMsg.fail().addInfo("top_opt_error","选题失败,该课题已经被选过！");
            }
            //检查学生是否自拟课题
            Topic t = topicService.selTopByCurAndTopGiver(curId,student.getStuId());
            if(t != null){
                if(t.getTopStatus() == 0)
                    return JsonMsg.fail().addInfo("top_opt_error","选题失败,你已经自拟课题！");
            }

            //插入Group，建立学生与选题关系
            group.setTopId(topId);
            Topic t1 = topicService.selectOneByTopId(topId);
            t1.setTopStatus(1);//设置选题状态为已选状态
            topicService.updateTopById(topId,t1);
            res = groupService.updateTopOfGroup(group.getGroId(),group);
            if(res != 1)
                return JsonMsg.fail().addInfo("top_opt_error","选题失败");

            Topic topic = topicService.selectOneByTopId(topId);
            session.setAttribute("msg","你选择的课题："+ topic.getTopName());
            return JsonMsg.success();
        }
        else{
            return JsonMsg.fail().addInfo("top_opt_error","选题信息失败异常");
        }
    }

    //教师删除选题
    @RequestMapping(value = "/delOpt/{groId}",method = RequestMethod.PUT)
    @ResponseBody
    public JsonMsg delOptById(@PathVariable(value = "groId") String groId){
        int res = 0;
        int res1 = 0;
        System.out.println("groId:"+groId);
        if(groId.length() > 0){
            Group  group = groupService.selectByGroId(groId);
            Topic topic = topicService.selectOneByTopId(group.getTopId());
            topic.setTopStatus(0);
            System.out.println("topic"+topic.toString());
            res1 = topicService.updateTopById(topic.getTopId(),topic);
            res = groupService.cancelTop(groId);
            if(res != 1 && res1 != 1)
                return JsonMsg.fail().addInfo("opt_del_error","删除选题失败！");

            return JsonMsg.success();
        } else{
            return JsonMsg.fail().addInfo("opt_del_error","选题信息异常");
        }
    }

   //教师查询某条成绩
    @RequestMapping(value = "/getGroById/{groId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getEmpById(@PathVariable("groId") String groId){
        Group group = groupService.selectGroById(groId);
        if (group != null){
            return JsonMsg.success().addInfo("group", group);
        }else {
            return JsonMsg.fail().addInfo("sco_get_err","没有该学生成绩信息");
        }
    }

    //教师修改成绩
    @RequestMapping(value ="/updateScoreList/{scoStatus}", method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg UpdateScoreList(@PathVariable(value = "scoStatus")int scoStatus,@RequestBody List<Group> groups) throws ParseException {
        if(groups == null || groups.size() <= 0){
            return JsonMsg.fail().addInfo("sco_update_error", "修改异常：成绩列表为空！");}

        Group group = groupService.selectGroById(groups.get(0).getGroId());
        String scoEnd = group.getScoEnd();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date end = sdf.parse(scoEnd);
        Date current = new Date();

         if(current.after(end))
             return JsonMsg.fail().addInfo("sco_update_error", "修改异常：登记成绩时间已经结束！");

         //检查比例
        double sco1Count = group.getSco1Count();
        double sco2Count = group.getSco2Count();
        double sco3Count = group.getSco3Count();

        if(sco1Count == 0 && sco2Count == 0 && sco3Count == 0 ){
            return JsonMsg.fail().addInfo("sco_update_error", "修改异常：请先设置成绩比例！");
        }
      //更新数据
        for(Group g: groups){
            int res = 0;
            Group g1 = groupService.selectGroById(g.getGroId());
            double score1 =(double) Math.round(g.getScore1()*100)/100;
            double score2 = (double)Math.round(g.getScore2()*100)/100;
            double score3 = (double)Math.round(g.getScore3()*100)/100;
            g1.setScore1(score1);
            g1.setScore2(score2);
            g1.setScore3(score3);
            double total = score1 *  g1.getSco1Count() +score2  * g1.getSco2Count()+ score3 *g1.getSco3Count();
            double total1 =(double) Math.round(total*100)/100;
            g1.setTotal(total1);
            res = groupService.updateTopOfGroup(g.getGroId(), g1);
            if (res != 1){
                 return JsonMsg.fail().addInfo("sco_update_error", "修改异常：请再次尝试！");
            }
        }
        //更新成绩状态
        Curriculum c = curriculumService.selCurById(group.getCurId());
        c.setScoStatus(scoStatus);
        int res3 = 0;
        res3 = curriculumService.UpdateCur(group.getCurId(),c);
        if(res3 != 1)
            return JsonMsg.fail().addInfo("sco_update_error", "修改异常：成绩状态更新失败！");
        return JsonMsg.success();
    }

    //教师成绩分析
    @RequestMapping(value = "/getAnalysisValue", method = RequestMethod.GET)
    public ModelAndView getAnalysisValue(@RequestParam(value = "curId")String curId){
        ModelAndView mdv = new ModelAndView("teaScoreGraph");

        Curriculum c = curriculumService.selCurById(curId);
        Group g = groupService. getScoValuesByCurId(curId);

        double avg1 = (double)Math.round(g.getAvg1()*100)/100.0;
        double avg2 = (double)Math.round(g.getAvg2()*100)/100.0;
        double avg3 = (double)Math.round(g.getAvg3()*100)/100.0;
        double avgTotal = (double)Math.round(g.getAvgTotal()*100)/100.0;
        double stdScore1 = (double)Math.round(g.getStdScore1()*100)/100.0;
        double stdScore2 = (double)Math.round(g.getStdScore2()*100)/100.0;
        double stdScore3 = (double)Math.round(g.getStdScore2()*100)/100.0;
        double stdTotal = (double)Math.round(g.getStdTotal()*100)/100.0;
        double totalRate= (double)Math.round(g.getAvgTotal()*100)/100.0;

        g.setAvg1(avg1);
        g.setAvg2(avg2);
        g.setAvg3(avg3);
        g.setAvgTotal(avgTotal);
        g.setStdScore1(stdScore1);
        g.setStdScore2(stdScore2);
        g.setStdScore3(stdScore3);
        g.setStdTotal(stdTotal);
        g.setTotalRate(totalRate);
        System.out.println(c.toString());
        System.out.println(g.toString());
        mdv.addObject("g",g);
        return mdv;
    }


    //获得某课程课题列表
    @RequestMapping(value = "/getOptTopByTea", method = RequestMethod.GET)
    public ModelAndView getOptTopList(@RequestParam(value = "curId") String curId, HttpSession session){
//        System.out.println("getTopList中curId："+curId);
        ModelAndView mdv = new ModelAndView("teaTopOptPage");

        List<Group> groups = groupService.getOptTopListByTea(curId);

//        for(Group group:groups){
//            System.out.println("getOptTop中groups"+group.toString());
//        }

        mdv.addObject("groups",groups)
                .addObject("curId",curId);
        return mdv;
    }

    //管理员查看某课程教师下的学生
    @RequestMapping(value = "/getStuGroupByAdmin", method = RequestMethod.GET)
    public ModelAndView getStuGroupByAdmin(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(value = "curId")String curId){
        ModelAndView mdv = new ModelAndView("adminStuGroPage");
        //每页页数
        int limit = 5;
        //总记录数
        int totalItems = groupService.countStuGro(curId);
        int temp = totalItems / limit ;
        //页数
        int totalPages = (totalItems % limit == 0) ? temp : temp+1;
        //每页第一行
        int offset = (pageNo-1)*limit;
        List<Group> gros = groupService.getStuGroupByAdmin(curId, offset, limit);
        for(Group g:gros){
            System.out.println(g.toString());
        }

        mdv.addObject("gros",gros)
                .addObject("totalItems",totalItems)
                .addObject("totalPages",totalPages)
                .addObject("curPageNo",pageNo)
                .addObject("curId",curId);
        return mdv;
    }

    //学生查看成绩
    @RequestMapping(value = "/getScoreByStu", method = RequestMethod.GET)
    public ModelAndView getCouList(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                   HttpSession session){
        Student student = (Student) session.getAttribute("student");
        ModelAndView mdv = new ModelAndView("stuScorePage");
        //每页页数
        int limit = 5;
        //总记录数
        int totalItems = groupService.countScosByStu(student.getStuId());
        int temp = totalItems / limit ;
        //页数
        int totalPages = (totalItems % limit == 0) ? temp : temp+1;
        //每页第一行
        int offset = (pageNo-1)*limit;
        List<Group> scores = groupService.getScoListByStu(student.getStuId(),offset, limit);
        for(Group s:scores)
            System.out.println("scores:"+s.toString());

        mdv.addObject("scores",scores)
                .addObject("totalItems",totalItems)
                .addObject("totalPages",totalPages)
                .addObject("curPageNo",pageNo);
        return mdv;
    }

    //学生查看选题结果
    @RequestMapping(value = "/getOptResule", method = RequestMethod.GET)
    public ModelAndView getOptResule(HttpSession session){
        Student student = (Student) session.getAttribute("student");
        ModelAndView mdv = new ModelAndView("stuOptPage");
        //总记录数
        int totalItems = groupService.countOptsByStu(student.getStuId());
        System.out.println("选题总记录数："+totalItems);
        //每页第一行
        List<Group> groups = groupService.getOptListByStu(student.getStuId());
        for(Group g:groups)
            System.out.println("groups:"+g.toString());

        mdv.addObject("groups",groups)
                .addObject("totalItems",totalItems);
        return mdv;
    }

    //管理员添加教师学生关系
    @RequestMapping(value = "/addGro",method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg addGro(@RequestParam(value = "curId") String curId,@RequestParam(value = "stuId")String stuId){
        int res = 0;
        Group g = groupService.selGroByCurIdAndStuId(curId,stuId);
        if(g != null)
            return JsonMsg.fail().addInfo("gro_add_error","添加失败：教师学生分组已经添加过了!");

        //检查学生是否已经被分给其他教师
        Curriculum c = curriculumService.selCurById(curId);
        Group g1 = groupService.selGroByCouIdAndStuIdAndCurYear(c.getCouId(),stuId,c.getCurYear());
        if(g1 != null){
            if(g1.getTeaId() != c.getTeaId()){
                return JsonMsg.fail().addInfo("gro_add_error","添加失败，该课程学生已经分配给其他教师！");
            }else{
                return JsonMsg.fail().addInfo("gro_add_error","添加失败，该课程学生已经分配给该教师！");
            }
        }

        res = groupService.insertGroup(curId,stuId);

        if(res != 1)
            return JsonMsg.fail().addInfo("gro_add_error","教师学生分组插入异常");

        return JsonMsg.success();
    }


    //计算成绩页面的值:检查登记成绩结束时间
    @RequestMapping(value = "/getScoreValues", method = RequestMethod.GET)
    public ModelAndView getScoreValues(@RequestParam("curName")String curName){
        ModelAndView mdv = new ModelAndView("adminScoreGraph");
        String as [ ] = curName.split("_");
        int curYear = Integer.parseInt(as[0]);
        String couId = as[1];

        Course c = courseService.getCouByCouId(couId);
        Group g = groupService. getScoValuesByCouId(couId,curYear);

        double avg1 = (double)Math.round(g.getAvg1()*100)/100.0;
        double avg2 = (double)Math.round(g.getAvg2()*100)/100.0;
        double avg3 = (double)Math.round(g.getAvg3()*100)/100.0;
        double avgTotal = (double)Math.round(g.getAvgTotal()*100)/100.0;
        double stdScore1 = (double)Math.round(g.getStdScore1()*100)/100.0;
        double stdScore2 = (double)Math.round(g.getStdScore2()*100)/100.0;
        double stdScore3 = (double)Math.round(g.getStdScore2()*100)/100.0;
        double stdTotal = (double)Math.round(g.getStdTotal()*100)/100.0;

//        double score1Rate= Math.round(g.getAvg1()/(100*c.getScore1Count())*100)/100.0;
//        double score2Rate= Math.round(g.getAvg2()/(100*c.getScore2Count())*100)/100.0;
//        double score3Rate= Math.round(g.getAvg3()/(100*c.getScore3Count())*100)/100.0;
        double totalRate= (double)Math.round(g.getAvgTotal()*100)/100.0;

        g.setAvg1(avg1);
        g.setAvg2(avg2);
        g.setAvg3(avg3);
        g.setAvgTotal(avgTotal);
        g.setStdScore1(stdScore1);
        g.setStdScore2(stdScore2);
        g.setStdScore3(stdScore3);
        g.setStdTotal(stdTotal);
//        g.setScore1Rate(score1Rate);
//        g.setScore2Rate(score2Rate);
//        g.setScore3Rate(score3Rate);
        g.setTotalRate(totalRate);
        System.out.println(c.toString());
        System.out.println(g.toString());
        mdv.addObject("g",g);
        return mdv;
    }

    //管理员查看学生成绩报告:检查成绩登记时间
    @RequestMapping(value = "/getStuScoByAdmin", method = RequestMethod.GET)
    public ModelAndView getStuScoByAdmin(@RequestParam("curName")String curName){
        ModelAndView mdv = new ModelAndView("adminScorePage");
        String as [ ] = curName.split("_");
        int curYear = Integer.parseInt(as[0]);
        String couId = as[1];
        List<Curriculum> curriculumList = curriculumService.selCurByCouAndCurYear(couId,curYear);

             //获得学生成绩列表以及各项平均分
        List<Group> groups = null;
        for(Curriculum c:curriculumList) {
            groups = groupService.getScoListByTea(c.getCurId());
            Group avgSco = groupService.getAvgScoByTea(c.getCurId());
            for(Group g:groups){
                Double avg1 = (double)Math.round(avgSco.getAvg1()*100)/100.0;
                Double avg2 = (double)Math.round(avgSco.getAvg2()*100)/100.0;
                Double avg3 =(double) Math.round(avgSco.getAvg3()*100)/100.0;
                Double avgTotal = (double)Math.round(avgSco.getAvgTotal()*100)/100.0;
                g.setAvg1(avg1);
                g.setAvg2(avg2);
                g.setAvg3(avg3);
                g.setAvgTotal(avgTotal);
            }
        }

        //每页第一行
        for(Group g:groups)
            System.out.println("groups:"+g.toString());

        mdv.addObject("groups",groups)
                .addObject("curName",curName);
        return mdv;
    }


    // 学生上传报告:时间为选题结束后，教师登记成绩结束之前
    @RequestMapping(value = "/importReport/{curId}", method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg updateReport(@PathVariable(value = "curId") String curId, @RequestParam(value = "stuReport")MultipartFile stuReport,
                                HttpSession session, HttpServletRequest request) throws IOException, ParseException {

        Student student = (Student) session.getAttribute("student");
        Group group = groupService.getGroByStuAndCur(curId,student.getStuId());
        System.out.println("updateReport:"+group.toString());
        if(group.getTopId() == null) {
            return JsonMsg.fail().addInfo("reportUpload_err", "上传报告失败：你还未选择课题！");
        }else{
            Curriculum curriculum = curriculumService.selCurById(group.getCurId());
            String endTime = curriculum.getEndTime();
            String scoEnd = curriculum.getScoEnd();
            if(endTime != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date end = sdf.parse(endTime);
                Date scoEnd1 = sdf.parse(scoEnd);
                //获取当前时间
                Date current = new Date();
                System.out.println("选题结束时间："+end);
                System.out.println("现在时间"+current);
                if(current.before(end)){
                    return  JsonMsg.fail().addInfo("reportUpload_err","选题时间结束之后才可以上传报告！");
                }
                if(current.after(scoEnd1)){
                    return  JsonMsg.fail().addInfo("reportUpload_err","教师已经登记完成绩不可以在上传报告！");
                }
            }
        }
        String curName = group.getCurYear()+"学年"+group.getCouName();
        // uploads文件夹位置
        String rootPath = request.getSession().getServletContext().getRealPath("studentReport\\"+curName+"\\"+group.getTeaName());
        // 原始名称
        String originalFileName = stuReport.getOriginalFilename();
        // 新文件名
        String newFileName = student.getStuName()+"("+student.getStuId()+")"+"-"+  curName +originalFileName.substring(originalFileName.lastIndexOf("."));

        System.out.println("新文件名："+newFileName);
        // 新文件
        File newFile = new File(rootPath + File.separator + newFileName);
        // 判断目标文件所在目录是否存在
        if( !newFile.getParentFile().exists()) {
            // 如果目标文件所在的目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }
        // 将内存中的数据写入磁盘
        stuReport.transferTo(newFile);

        // 完整的url
        String fileUrl = rootPath + "\\" + newFileName;

        System.out.println("文件路径："+fileUrl);
        group.setReport(fileUrl);
        int res = 0;
        res = groupService.updateTopOfGroup(group.getGroId(),group);
        return JsonMsg.success();
    }

    //教师下载打包的学生报告:时间为选题时结束之后
    @RequestMapping(value = "/getstuReport", method = RequestMethod.GET)
    public void getstuReport(@RequestParam(value = "curId")String curId, HttpSession session,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        Curriculum curriculum = curriculumService.selCurById(curId);
        System.out.println("tar文件:"+curriculum.toString());

        String curName = curriculum.getCurYear()+"学年"+curriculum.getCouName();
        String sourceFilePath = request.getSession().getServletContext().getRealPath("studentReport\\"+curName+"\\"+curriculum.getTeaName());
        String zipFilePath0 = request.getSession().getServletContext().getRealPath("zipFile");
        String fileName = curName + curriculum.getTeaName();
        String zipFilePath1 = zipFilePath0 +"\\"+ fileName;
        String zipFilePathWithFileName = zipFilePath0 +"\\"+ fileName+".zip";
        boolean flag = false;
        FileToZip fileToZip = new FileToZip();
        //文件打包
        flag =  fileToZip.fileToZip(sourceFilePath, zipFilePath0, fileName);
        //导出压缩包
        fileToZip.downLoad(zipFilePathWithFileName,response);
    }

    //管理员导出成绩报表
    @RequestMapping(value = "/importSco")
    @ResponseBody
    public void export(@RequestParam(value="curName")String curName,HttpServletResponse response) throws Exception {

        String as [ ] = curName.split("_");
        int curYear = Integer.parseInt(as[0]);
        String couId = as[1];
        List<Curriculum> curriculumList = curriculumService.selCurByCouAndCurYear(couId,curYear);

        //获得学生成绩列表以及各项平均分
        List<Group> groups = null;
        for(Curriculum c:curriculumList) {
            groups = groupService.getScoReport(c.getCurId());
            Group avgSco = groupService.getAvgScoByTea(c.getCurId());
            for(Group g:groups){
                double avg1 = (double)Math.round(avgSco.getAvg1()*100)/100.0;
                double avg2 = (double)Math.round(avgSco.getAvg2()*100)/100.0;
                double avg3 = (double)Math.round(avgSco.getAvg3()*100)/100.0;
                double avgTotal = (double)Math.round(avgSco.getAvgTotal()*100)/100.0;
                g.setAvg1(avg1);
                g.setAvg2(avg2);
                g.setAvg3(avg3);
                g.setAvgTotal(avgTotal);
            }
        }

        //excel标题
        String[] title = {"学号","姓名","班级","成绩一","平均分","成绩二","平均分","成绩三","平均分","总成绩","平均分","名次"};
        //文件表名
        String couName = courseService.getCouByCouId(couId).getCouName();
        String fileName = curYear+"学年"+couName+"成绩报表"+System.currentTimeMillis()+".xls";
        //sheet名
        String sheetName = "学生成绩报表";
        String[][] content = new String[ groups.size() ][ title.length ];
        for (int i = 0; i < groups.size(); i++) {
            Group obj = groups.get(i);
            content[i][0] = obj.getStuId();
            content[i][1] = obj.getStuName();
            content[i][2] = obj.getStuMajor()+obj.getStuClass();
            if(obj.getScore1() == null && obj.getScore2() == null && obj.getScore3() == null){
                continue;
            }
            content[i][3] = obj.getScore1().toString();
            content[i][4] = obj.getAvg1().toString();
            content[i][5] = obj.getScore2().toString();
            content[i][6] = obj.getAvg2().toString();
            content[i][7] = obj.getScore3().toString();
            content[i][8] = obj.getAvg3().toString();
            content[i][9] = obj.getTotal().toString();
            content[i][10] = obj.getAvgTotal().toString();
            content[i][11] = String.valueOf(obj.getRowNo());
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //教师导出成绩表
    @RequestMapping(value = "/exportScoByTea")
    @ResponseBody
    public void exportScoByTea(@RequestParam(value="curId")String curId, HttpServletResponse response) throws Exception {

       List<Group> groups = groupService.getScoListExportByTea(curId);
       for(Group g:groups)
           System.out.println("exportScoByTea:"+g.toString());

       Curriculum c = curriculumService.selCurById(curId);

        //excel标题
        String[] title = {"学号","姓名","专业班级","成绩一","成绩二","成绩三","总成绩","名次"};
        //文件表名
        String fileName = c.getCurYear()+"学年"+c.getCouName()+"("+c.getTeaName()+")"+System.currentTimeMillis()+".xls";
        //sheet名
        String sheetName = "学生成绩表";
        String[][] content = new String[ groups.size() ][ title.length ];
        for (int i = 0; i < groups.size(); i++) {
            Group obj = groups.get(i);
            content[i][0] = obj.getStuId();
            content[i][1] = obj.getStuName();
            content[i][2] = obj.getStuMajor()+obj.getStuClass();
            if(obj.getScore1() == null && obj.getScore2() == null && obj.getScore3() == null){
                continue;
            }
            content[i][3] = obj.getScore1().toString();
            content[i][4] = obj.getScore2().toString();
            content[i][5] = obj.getScore3().toString();
            content[i][6] = obj.getTotal().toString();
            content[i][7] = String.valueOf(obj.getRowNo());
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //教师导出选题表
    @RequestMapping(value = "/importOpt")
    @ResponseBody
    public void importOpt(@RequestParam(value="curId")String curId,HttpSession session,
                       HttpServletRequest request,HttpServletResponse response) throws Exception {

        Teacher t = (Teacher) session.getAttribute("teacher");
        List<Group> groups = groupService.getOptTopListByCur(curId);

        //excel标题
        String[] title = {"学号","姓名","专业班级","课题"};

       Curriculum c= curriculumService.selCurById(curId);
        String fileName = c.getCurYear()+"学年"+c.getCouName()+"学生选题表"+System.currentTimeMillis()+".xls";

        //sheet名
        String sheetName = t.getTeaName();
        String[][] content = new String[ groups.size() ][ title.length ];
        for (int i = 0; i < groups.size(); i++) {
            Group obj = groups.get(i);
            content[i][0] = obj.getStuId();
            content[i][1] = obj.getStuName();
            content[i][2] = obj.getStuMajor()+obj.getStuClass();
            content[i][3] = obj.getTopName();
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //教师导入成绩
    @RequestMapping(value = "/importScore/{curId}", method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg importScore(@PathVariable(value ="curId") String curId, @RequestParam(value = "scoreFile") MultipartFile scoreFile) throws IOException, ParseException {
        //检查是否是导入成绩时间
        Curriculum c = curriculumService.selCurById(curId);
//        System.out.println("importScore中c"+c.toString());
//        if(c.getScoEnd() != null){
//            //检查此刻是否在登记成绩结束时间之后
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date end = sdf.parse(c.getScoEnd());
//            Date current = new Date();
//            if(current.after(end)){
//                return JsonMsg.fail().addInfo("scoreUpload_err","成绩导入失败：导入成绩时间已经结束！");
//            }
//        }
        double sco1Count = c.getSco1Count();
        double sco2Count = c.getSco2Count();
        double sco3Count = c.getSco3Count();
        if(sco1Count == 0 && sco2Count == 0 && sco3Count == 0)
            return JsonMsg.fail().addInfo("scoreUpload_err","成绩导入失败：成绩比例还未设置！");

        JsonMsg jsonMsg = checkCourseFile(scoreFile,curId);

        if(jsonMsg.getCode() != 100){
            return  jsonMsg;
        }

        String fileName = scoreFile.getOriginalFilename();
        Workbook workbook = null;

        try {
            workbook = WorkbookFactory.create(scoreFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        int sheetCount = workbook.getNumberOfSheets();// 获得工作表个数
        int rows = 0;

        // 遍历工作表
        for (int i = 0; i < sheetCount; i++)
        {
            Sheet sheet = workbook.getSheetAt(i);
            rows = sheet.getLastRowNum();// 获得行数
            System.out.println("importScore行数rows："+rows);
            // 获得列数，先获得一行，在得到列数
            Row tmp = sheet.getRow(0);
            if (tmp == null)
                continue;

            // 读取数据 跳过表头
            for (int row = 1; row < rows + 1; row++)
            {
                Row r = sheet.getRow(row);
                String cellValue;
                Group gro = null;

                //处理学号
                Cell cell = r.getCell(0);
                cellValue = String.valueOf(cell.getStringCellValue());
                gro = groupService.selGroByCurIdAndStuId(curId,cellValue);

                //处理姓名
                cell = r.getCell(1);
                cellValue = String.valueOf(cell.getStringCellValue());

                //处理成绩一
                cell = r.getCell(2);
                double score1 = (double)Math.round(cell.getNumericCellValue()*100)/100.0;
                gro.setScore1(score1);

                //处理成绩二
                cell = r.getCell(3);
                double score2 = (double)Math.round(cell.getNumericCellValue()*100)/100.0;
                gro.setScore2(score2);

                //处理成绩三
                cell = r.getCell(4);
                double score3 = (double) Math.round(cell.getNumericCellValue()*100)/100.0;
                gro.setScore3(score3);

                //处理总成绩
                double total = score1 * c.getSco1Count() + score2 * c.getSco2Count()  + score3 * c.getSco3Count() ;
                gro.setTotal((double)Math.round(total*100)/100.0);
               groupService.updateTopOfGroup(gro.getGroId(),gro);
            }
        }
        return jsonMsg.success();
    }


    //检查课程文件
    public JsonMsg checkCourseFile(MultipartFile scoreFile,String curId){

        Curriculum c = curriculumService.selCurById(curId);
        String fileName = scoreFile.getOriginalFilename();
        Workbook workbook = null;

//        if(fileName.endsWith(XLS)) {
//            try {
//                workbook = new HSSFWorkbook(scoreFile.getInputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
            try {
//                workbook = new XSSFWorkbook(scoreFile.getInputStream());
                workbook = WorkbookFactory.create(scoreFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            }
//        }

        int sheetCount = workbook.getNumberOfSheets();// 获得工作表个数
        int rows = 0;

        // 遍历工作表
        for (int i = 0; i < sheetCount; i++)
        {
            Sheet sheet = workbook.getSheetAt(i);
            rows += sheet.getLastRowNum();// 获得行数

            // 获得列数，先获得一行，在得到列数
            Row tmp = sheet.getRow(0);
            if (tmp == null)
            {
                continue;
            }

            //检查列数
            System.out.println("列数："+sheet.getRow(0).getPhysicalNumberOfCells());
            if(sheet.getRow(0).getPhysicalNumberOfCells() != 5){
                return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中列数不正确（正确列数：5）" );
            }

            //检查表头
            Row r0 = sheet.getRow(0);
            String cellHead;

            Cell cell0 = r0.getCell(0);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("学号")){
                return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ i +"中的第1行第1列列名不正确！正确应为\"学号\" " );
            }

            cell0 = r0.getCell(1);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("姓名")){
                return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第1行第2列列名不正确！正确应为\"姓名\" " );
            }

            cell0 = r0.getCell(2);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("成绩一")){
                return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第1行第3列列名不正确！正确应为\"目标一\" " );
            }

            cell0 = r0.getCell(3);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("成绩二")){
                return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第1行第4列列名不正确！正确应为\"目标二\" " );
            }

            cell0 = r0.getCell(4);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("成绩三")){
                return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第1行第5列列名不正确！正确应为\"目标三\" " );
            }

            // 读取数据 跳过表头
            for (int row = 1; row < rows + 1; row++)
            {
                Row r = sheet.getRow(row);
                String cellValue;
                Course course = new Course();

                //处理学号
                Cell cell = r.getCell(0);
                System.out.println("学号：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第1列学号不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第1列课程号应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());

                    if(cellValue.length() != 12)
                        return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第1列学号长度应为12 " );

                    Group gro = groupService.selGroByCurIdAndStuId(curId, cellValue);
//                    System.out.println(gro.toString());
                    if(gro == null)
                        return JsonMsg.fail().addInfo("scoreUpload_err", "导入失败：" + fileName + "中sheet" + (i + 1) + "中的第" + (row + 1) + "行学生并未分配给该教师！");
                    if(gro.getTotal() != null) {
                        System.out.println("成绩导入失败的学号："+ cellValue);
                        return JsonMsg.fail().addInfo("scoreUpload_err", "导入失败：" + fileName + "中sheet" + (i + 1) + "中的第" + (row + 1) + "行成绩已经评定");
                    }
                }

                //处理姓名
                cell = r.getCell(1);
                System.out.println("姓名：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第2列姓名不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第2列姓名应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());

                    if(cellValue.length() > 20 || cellValue.length() < 1)
                        return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第2列姓名长度不在规定范围内（1-20） " );
                }

                //处理目标一成绩
                cell = r.getCell(2);
                System.out.println("成绩一：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第3列成绩一不能为空 " );
                else{
                    if(cell.getCellType() != 0)
                        return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第3列成绩一应为数字类型 " );

                    double score1 = (double)cell.getNumericCellValue();

                    if(score1 > 100)
                        return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第3列成绩一不能超过"+ 100 +"分! " );
                }

                //处理目标二
                cell = r.getCell(3);
                System.out.println("成绩二：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第4列成绩二不能为空 " );
                else{
                    if(cell.getCellType() != 0)
                        return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第4列成绩二应为数字类型 " );

                    double score2 = (double)cell.getNumericCellValue();

                    if(score2 > 100)
                        return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第3列成绩二不能超过"+100+"分! " );
                }

                //处理目标三
                cell = r.getCell(4);
                System.out.println("成绩三：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    continue;
                else{
                    if(cell.getCellType() != 0)
                        return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第4列成绩三应为数字类型 " );

                    double score3 = (double)cell.getNumericCellValue();
                    if(score3 >  100)
                        return JsonMsg.fail().addInfo("scoreUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第3列成绩三不能超过"+100+"分! " );
                }
            }
        }
        return JsonMsg.success();
    }

    //教师查看成绩
    @RequestMapping(value = "/getScoListByTea",method = RequestMethod.GET)
    public ModelAndView getScoListByTea(@RequestParam(value = "curId")String curId){
        ModelAndView mdv = new ModelAndView("teaScorePage");

        List<Group> scores = groupService.getScoListByTea(curId);
        Curriculum c = curriculumService.selCurById(curId);
        mdv.addObject("scores",scores)
                .addObject("curId",curId)
                 .addObject("cur",c);
        return mdv;
    }
}
