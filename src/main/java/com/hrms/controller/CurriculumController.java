package com.hrms.controller;

import com.hrms.bean.*;
import com.hrms.service.*;
import com.hrms.util.JsonMsg;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/hrms/cur")
public class CurriculumController {

    @Autowired
    CurriculumService curriculumService;
    @Autowired
    TopicService topicService;
    @Autowired
    GroupService groupService;
    @Autowired
    StudentService studentService;

    //管理员设置登记成绩时间
    @RequestMapping(value = "/scoTimeInstall", method = RequestMethod.PUT)
        @ResponseBody
        public JsonMsg scoTimeInstall(@RequestParam("couId") String couId, @RequestParam("curYear") int curYear,
                                      @RequestParam("startTime")String startTime,
                                      @RequestParam("endTime") String endTime){
            int res = 0;
            System.out.println("startTime:" +startTime);
            System.out.println("endTime:" +endTime);
            if (couId.length() > 0){
                res = curriculumService.UpdateScoTime(couId,curYear,startTime,endTime);
            }
            if (res == 0){
                return JsonMsg.fail().addInfo("timeInstall_err", "时间设置失败，请重新设置！");
            }
            return JsonMsg.success();
        }

        //更新成绩比例
    @RequestMapping(value = "/updatePartition/{curId}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonMsg updatePartition(@PathVariable(value = "curId") String curId, Curriculum curriculum) throws ParseException {
            int res = 0;
            Curriculum curriculum1 = curriculumService.selCurById(curId);
            Double sco1Count = curriculum.getSco1Count();
            Double sco2Count = curriculum.getSco2Count();
            Double sco3Count = curriculum.getSco3Count();
            if(sco1Count + sco2Count + sco3Count != 1)
                return JsonMsg.fail().addInfo("partition_update_error", "成绩比例设置失败，比例之和为1！");

            curriculum1.setSco1Count(sco1Count);
            curriculum1.setSco2Count(sco2Count);
            curriculum1.setSco3Count(sco3Count);
            res = curriculumService.UpdateCur(curId,curriculum1);

            //更新总成绩
        List<Group> groups = groupService.getScoListByTea(curId);
        for(Group g:groups){
            int res1 = 0;
            double sco1 = g.getScore1();
            double sco2 = g.getScore2();
            double sco3 = g.getScore3();
            double total = sco1 *  g.getSco1Count() +sco2  * g.getSco2Count()+ sco3 *g.getSco3Count();
            double total1 = Math.round(total*100)/100;
            g.setTotal(total1);
            res1 = groupService.updateTopOfGroup(g.getGroId(), g);
            if (res1 != 1){
                return JsonMsg.fail().addInfo("partition_update_error", "成绩比例设置失败：请再次尝试！");
            }
        }

        if (res == 0){
            return JsonMsg.fail().addInfo("partition_update_error", "成绩比例设置失败，请重新设置！");
        }
        return JsonMsg.success();
    }


    //设置选题时间,先检查教师上传课题,报告模板情况
    @RequestMapping(value = "/optTimeInstall/{curId}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonMsg timeInstall(@PathVariable("curId") String curId, @RequestParam("startTime")String startTime,
                                  @RequestParam("endTime") String endTime) throws ParseException {
        //检查教师上传课题
        List<Topic> topic = topicService.selTopByCurId(curId);
        Curriculum c = curriculumService.selCurById(curId);
        if(topic.size() == 0){
            return JsonMsg.fail().addInfo("timeInstall_err", "时间设置失败，请先上传课题！");
        }
        if(c.getMould() == null){
            return JsonMsg.fail().addInfo("timeInstall_err", "时间设置失败，请先上传报告模板！");
        }
        if(c.getScoEnd() == null)
        {
            return JsonMsg.fail().addInfo("timeInstall_err", "时间设置失败，管理员还未设置登记成绩时间！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date end = sdf.parse(endTime);
        Date scoEnd = sdf.parse(c.getScoEnd());
        if(end.after(scoEnd)){
            return JsonMsg.fail().addInfo("timeInstall_err", "时间设置失败，不能超过登记成绩结束时间！");
        }
        int res = 0;
        System.out.println("timeInstall中curId:" +curId);
        System.out.println("startTime:" +startTime);
        System.out.println("endTime:" +endTime);
        if (curId.length() > 0){
            c.setStartTime(startTime);
            c.setEndTime(endTime);
            res = curriculumService.UpdateCur(curId,c);
        }
        if (res != 1){
            return JsonMsg.fail().addInfo("timeInstall_err", "时间设置失败，请重新设置！");
        }
        return JsonMsg.success();
    }

    //检查成绩是否登记完，检查比例是否设置
    @RequestMapping(value ="/checkScoIsEmptyByCur", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg checkScoIsEmptyByCur(@RequestParam("curId") String curId){
        int res = 0;
        Curriculum c = curriculumService.selCurById(curId);
        if(c.getSco1Count() == 0 && c.getSco2Count() == 0 && c.getSco3Count()==0)
            return JsonMsg.fail().addInfo("scoEmpty_check_error", "请先设置成绩比例！");
        res = curriculumService.checkScoIsEmptyByCur(curId);
        System.out.println("scoEmpty:"+res);
        if (res == 0){
            return JsonMsg.fail().addInfo("scoEmpty_check_error", "成绩未登记完！");
        }
        return JsonMsg.success();
    }


    //检查登记成绩时间
    @RequestMapping(value = "/checkScoTime/{curId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg checkScoTime(@PathVariable(value = "curId") String curId) throws ParseException {
        Curriculum curriculum = curriculumService.selCurById(curId);
        System.out.println("curriculum:"+curriculum.toString());
        String scoStart = curriculum.getScoStart();
        if(scoStart == null){
            return  JsonMsg.fail().addInfo("sco_time_error","管理员还没有设定登记成绩时间！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(scoStart);
        //获取当前时间
        Date current = new Date();
        System.out.println(current);

        //现在时间在登记成绩结束时间之前
        if(current.before(start)){
            return  JsonMsg.fail().addInfo("sco_time_error","未到登记成绩时间，请耐心等待！");
        }else {
            return JsonMsg.success();
        }
    }


    //检查教师是否上传报告
    @RequestMapping(value = "/checkReportMould", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg checkReportMould(@RequestParam(value = "curId") String curId) throws ParseException {
        Curriculum c = curriculumService.selCurById(curId);
        if(c.getMould() == null)
            return JsonMsg.fail().addInfo("mould_upload_error","你还没有上传报告！");
        else
            return JsonMsg.success();
    }

    //检查学生报告
    @RequestMapping(value = "/checkStuReport", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg checkStuReport(@RequestParam(value = "curId") String curId) throws ParseException {
        List<Group> groups = groupService.getScoListExportByTea(curId);
        int count = 0;
        for(Group g:groups)
        {
            if(g.getReport()!= null)
                count++;
        }
        if(count == 0)
            return JsonMsg.fail().addInfo("stu_upload_error","还没有学生上传报告！");
        else
            return JsonMsg.success();
    }

    //教师上传报告模板
    @RequestMapping(value = "/importReportMould/{curId}", method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg importReportMould(@PathVariable(value = "curId") String curId, @RequestParam(value = "reportMould")MultipartFile reportMould,
                                HttpSession session, HttpServletRequest request) throws IOException, ParseException {

        Teacher t = (Teacher) session.getAttribute("teacher");
        Curriculum c = curriculumService.selCurById(curId);
        if(c.getStartTime() != null){
            //检查此刻是否在选题时间之前
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = sdf.parse(c.getStartTime());
            Date current = new Date();
            if(current.after(start)){
                return JsonMsg.fail().addInfo("topicUpload_err","上传报告失败：选题时间已经开始，请在选题时间开始前上传报告模板！");
            }
        }

        String curName = c.getCurYear()+"学年"+c.getCouName();
        // uploads文件夹位置
        String rootPath = request.getSession().getServletContext().getRealPath("Model\\"+curName);
        // 原始名称
        String originalFileName = reportMould.getOriginalFilename();
        // 新文件名
        String newFileName = curName+"("+t.getTeaName()+")" +originalFileName.substring(originalFileName.lastIndexOf("."));

        System.out.println("新文件名："+newFileName);
        // 新文件
        File newFile = new File(rootPath + File.separator + newFileName);
        // 判断目标文件所在目录是否存在
        if( !newFile.getParentFile().exists()) {
            // 如果目标文件所在的目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }
        // 将内存中的数据写入磁盘
        reportMould.transferTo(newFile);

        // 完整的url
        String fileUrl = rootPath + "\\" + newFileName;
        String fileUrl1 = "Model\\"+curName+"\\"+newFileName;
        System.out.println("文件路径："+fileUrl1);
        //设置成相对路径
        c.setMould(fileUrl1);
        int res = 0;
        res = curriculumService.UpdateCur(c.getCurId(),c);
        return JsonMsg.success();
    }

    //    管理员通过课程号和学年获得教师列表
    @RequestMapping(value = "/getTeaGroByAdmin", method = RequestMethod.GET)
    public ModelAndView getTeaGroByAdmin(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(value = "couId")String couId, @RequestParam(value = "curYear")int curYear){
        ModelAndView mdv = new ModelAndView("adminTeaGroPage");
        //每页页数
        int limit = 5;
        //总记录数
        int totalItems = curriculumService.countTeaGro(couId,curYear);
        int temp = totalItems / limit ;
        //页数
        int totalPages = (totalItems % limit == 0) ? temp : temp+1;
        //每页第一行
        int offset = (pageNo-1)*limit;
        List<Curriculum> curriculums = curriculumService.getTeaGroListByAdmin(couId, curYear, offset, limit);
        for(Curriculum c:curriculums){
            System.out.println(c.toString());
        }

        mdv.addObject("curriculums",curriculums)
                .addObject("totalItems",totalItems)
                .addObject("totalPages",totalPages)
                .addObject("curPageNo",pageNo)
                .addObject("couId",couId)
                .addObject("curYear",curYear);
        return mdv;
    }

    //    管理员通过学年+课程号获取教师信息
    @RequestMapping(value = "/getCurNameByAdmin", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getCurNameByAdmin(){
        List<Curriculum> curriculumList = curriculumService.selectCurNameByAdmin();
        List<Curriculum> teaList = null;
        if (curriculumList != null){
            for(Curriculum c:curriculumList){
                System.out.println(c.getCurYear()+"学年"+c.getCouName());
            }
            Curriculum c1 = curriculumList.get(0);
            teaList = curriculumService.getTeaListByCouAndYear(c1.getCurYear(),c1.getCouId());
            return JsonMsg.success().addInfo("curriculumList", curriculumList)
                                     .addInfo("teaList", teaList);
        }
        return JsonMsg.fail().addInfo("curriculumList_get_err", "没有课程,请添加课程!");
    }

    //管理员通过课程号和学年获得教师列表
    @RequestMapping(value = "/getTeaNameByAdmin/{couYear}", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getTeaNameByAdmin(@PathVariable(value = "couYear")String couYear){
        String as [ ] = couYear.split("_");
        int curYear = Integer.parseInt(as[0]);
        String couId = as[1];
        System.out.println("curYear+couId:"+curYear+"_"+couId);
        List<Curriculum> teaList = curriculumService.getTeaListByCouAndYear(curYear,couId);
        if (teaList != null){
            for(Curriculum c:teaList){
                System.out.println(c.getCurYear()+"学年"+c.getCouName()+"("+c.getTeaName()+")");
            }
            return JsonMsg.success().addInfo("teaList", teaList);
        }
        return JsonMsg.fail().addInfo("teaList_get_err", "该课程还未分配指导教师!");
    }

//    管理员删除任课表
    @RequestMapping(value = "/delCur/{curId}", method = RequestMethod.DELETE)
    @ResponseBody
    JsonMsg delCur(@PathVariable("curId")String curId){
        int res = 0;
        Curriculum curriculum;
        if(curId.length() > 0){
            curriculum = curriculumService.selCurById(curId);
            String statTime = curriculum.getStartTime();
            String endTime= curriculum.getEndTime();
            if(statTime != null && endTime != null)
                return JsonMsg.fail().addInfo("cur_del_error","不能删除该分组，教师已为该课程设置选题时间！");

            res = curriculumService.delCurById(curId);
            if(res != 1){
                return JsonMsg.fail().addInfo("cur_del_error","课程删除异常");
            }

            return JsonMsg.success();
        }
        return JsonMsg.fail().addInfo("cur_del_error","课程分组编号异常");
    }

    //管理员增加任课记录
    @RequestMapping(value = "/addCur",method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg addCur(@RequestParam(value = "couId") String couId,@RequestParam(value = "teaId")String teaId,
                          @RequestParam(value = "curYear")int curYear){

        int res = 0;

        Curriculum c = curriculumService.selCurByCouAndTeaAndYear(couId, teaId, curYear);
        if(c != null){
            return JsonMsg.fail().addInfo("cur_add_error","添加失败:该课程教师关系已经存在！");
        }
        List<Curriculum> curriculumList = curriculumService.selCurByCouAndCurYear(couId, curYear);
        if(curriculumList.size() > 0){
            String scoStart = curriculumList.get(0).getScoStart();
            String scoEnd = curriculumList.get(0).getScoEnd();
            if(scoStart != null && scoEnd != null){
                res = curriculumService.insertCurWithScoTime(couId,teaId,curYear,scoStart,scoEnd);
            }else{
                res = curriculumService.insertCur(couId,teaId,curYear);
            }

        }else{
            res = curriculumService.insertCur(couId,teaId,curYear);
        }

        if(res != 1)
            return JsonMsg.fail().addInfo("cur_add_error","课程教师分组插入异常");

        return JsonMsg.success();
    }

    //教师课程名字列表
    @RequestMapping(value = "/getCouNameByTea", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getCouNameByTea(HttpSession session){
        Teacher tea = (Teacher) session.getAttribute("teacher");
        List<Curriculum> courseList = curriculumService.selectCouNameByTea(tea.getTeaId());
        if (courseList != null){
            for(Curriculum c:courseList){
                System.out.println(c.getCurYear()+"_"+c.getCouName());
            }
            return JsonMsg.success().addInfo("courseList", courseList);
        }
        return JsonMsg.fail().addInfo("courseList_get_err", "该教师没有安排课程");
    }

    //教师课程列表
    @RequestMapping(value = "/getCouListByTea", method = RequestMethod.GET)
    public ModelAndView selectCouByTeaIdAndCurYear(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo
            , HttpSession session){
        ModelAndView mdv = new ModelAndView("teaCoursePage");
        Teacher tea = (Teacher) session.getAttribute("teacher");
//        System.out.println("teacher:"+ tea.toString());
        int limit = 5;
        System.out.println("getCouListByTea中teacher："+tea.getTeaId());
        int totalItems = curriculumService.countCousByTea(tea.getTeaId());
        int temp = totalItems / limit ;
        int totalPages = (totalItems % limit == 0) ? temp : temp+1;
        int offset = (pageNo-1)*limit;
        List<Curriculum> courses = curriculumService.selectCouByTeaIdAndCurYear(tea.getTeaId(),offset, limit);
        for(Curriculum c :courses){
            System.out.println(c.toString());
        }
        mdv.addObject("courses",courses)
                .addObject("totalItems",totalItems)
                .addObject("totalPages",totalPages)
                .addObject("curPageNo",pageNo);
        return mdv;
    }

    //学生课程名字列表
    @RequestMapping(value = "/getCouNameByStu", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getCouNameByStu(HttpSession session){
        Student stu = (Student) session.getAttribute("student");
        List<Curriculum> courseList = curriculumService.selectCouNameByStu(stu.getStuId());
        if (courseList != null){
            for(Curriculum c:courseList){
                System.out.println(c.getCurYear() + "_" + c.getCouName() + "_" + c.getTeaName());
                System.out.println(c.getCurId());
            }
            return JsonMsg.success().addInfo("courseList", courseList);
        }
        return JsonMsg.fail().addInfo("courseList_get_err", "该学生没有课程");
    }

//    管理员通过课程号获得教师信息以及课程比例信息
    @RequestMapping(value = "/getCurById/{curId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getCurById(@PathVariable(value = "curId")String curId){
        Curriculum cur = curriculumService.selCurById(curId);
        if (cur != null){
            return JsonMsg.success().addInfo("cur", cur);
        }
        return JsonMsg.fail().addInfo("cur_get_error", "课程获取失败!");
    }

    //学生课程列表
    @RequestMapping(value = "/getCouListByStu", method = RequestMethod.GET)
    public ModelAndView getCouListByStu(@RequestParam(value = "pageNo", defaultValue = "1")
                                                Integer pageNo, HttpSession session){
        ModelAndView mdv = new ModelAndView("stuCoursePage");
        Student stu = (Student) session.getAttribute("student");
        System.out.println(stu.toString());
        int limit = 5;
        int totalItems = curriculumService.countCousByStu(stu.getStuId());
        int temp = totalItems / limit ;
        int totalPages = (totalItems % limit == 0) ? temp : temp+1;
        int offset = (pageNo-1)*limit;
        List<Curriculum> courses = curriculumService.selectCouOfStu(stu.getStuId(),offset, limit);
        for(Curriculum c :courses){
            System.out.println(c.toString());
        }
        System.out.println("总记录数："+ totalItems);
        System.out.println("总页数："+ totalPages);
        System.out.println("当前页数"+ pageNo);
        mdv.addObject("courses",courses)
                .addObject("totalItems",totalItems)
                .addObject("totalPages",totalPages)
                .addObject("curPageNo",pageNo);
        return mdv;
    }

    //检查登记成绩时间
    @RequestMapping(value = "/checkScoEndByCurName/{curName}", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg checkTopTime(@PathVariable("curName") String curName) throws ParseException {
        String as [ ] = curName.split("_");
        int curYear = Integer.parseInt(as[0]);
        String couId = as[1];
        List<Curriculum> curriculumList = curriculumService.selCurByCouAndCurYear(couId,curYear);
        Curriculum curriculum = curriculumList.get(0);
        System.out.println("curriculum:"+curriculum.toString());
        String scoEnd = curriculum.getScoEnd();
        if(scoEnd == null){
            return  JsonMsg.fail().addInfo("scoEnd_before_err","未设置登记成绩时间，请耐心等待！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date end = sdf.parse(scoEnd);
        //获取当前时间
        Date current = new Date();
        System.out.println(current);

        //现在时间在登记成绩结束时间之前
        if(current.before(end)){
            return  JsonMsg.fail().addInfo("scoEnd_before_err","教师尚未登记成绩，请耐心等待！");
        } else{
            return JsonMsg.success();
        }
    }
}
