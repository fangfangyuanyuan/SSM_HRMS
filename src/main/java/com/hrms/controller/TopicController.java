package com.hrms.controller;
import com.hrms.bean.*;
import com.hrms.service.CourseService;
import com.hrms.service.CurriculumService;
import com.hrms.service.GroupService;
import com.hrms.service.TopicService;
import com.hrms.util.JsonMsg;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/hrms/topic")
public class TopicController {

    @Autowired
    TopicService topicService;
    @Autowired
    CurriculumService curriculumService;
    @Autowired
    CourseService courseService;
    @Autowired
    GroupService groupService;
    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";


    //删除课题：查询group表，若存在不能删除，若不存在可以删除
    @RequestMapping(value = "/delTop/{topId}", method = RequestMethod.DELETE)
    @ResponseBody
    JsonMsg delTop(@PathVariable(value = "topId")String topId) throws ParseException {
        Group group;
        int res = 0;
        if(topId.length() > 0){
            Topic t = topicService.selectOneByTopId(topId);
            String startTime = t.getStartTime();
            if(startTime != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date start = sdf.parse(startTime);
                //获取当前时间
                Date current = new Date();
                System.out.println("选题开始时间："+start);
                System.out.println("现在时间"+current);
                if(current.after(start)){
                    return  JsonMsg.fail().addInfo("time_error","学生已经开始选题，不能删除选题！");
                }
            }

            res = topicService.delTopById(topId);
            if(res != 1){
                return JsonMsg.fail().addInfo("top_del_error","课题删除异常");
            }else{
                return JsonMsg.success();
                }
        }
        return JsonMsg.fail().addInfo("top_del_error","课题号异常");
    }

    //教师更新课题
    @RequestMapping(value = "/updateTop/{topId}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonMsg updateTopById(@PathVariable("topId")String topId,Topic topic) throws ParseException {
        int res = 0;

        if(topId.length() > 0){
            Topic t = topicService.selectOneByTopId(topId);
            String startTime = t.getStartTime();
            if(startTime != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date start = sdf.parse(startTime);
                //获取当前时间
                Date current = new Date();
                System.out.println("选题开始时间："+start);
                System.out.println("现在时间"+current);
                if(current.after(start)){
                    return  JsonMsg.fail().addInfo("top_edit_error","学生已经开始选题，不能修改选题！");
                }
            }
            topic.setTopStatus(0);
            System.out.println("UpdateTopic:"+topic.toString());
            res = topicService.updateTopById(topId,topic);
        }
        if(res != 1)
            return JsonMsg.fail().addInfo("top_edit_error","课题更新异常");
        else
            return JsonMsg.success();
    }

    //审核自拟课题
    @RequestMapping(value = "/topReview", method = RequestMethod.PUT)
    @ResponseBody
    public JsonMsg topReview(@RequestParam("topId")String topId,@RequestParam(value = "topStatus") String topStatus,
                             @RequestParam("curId")String curId){
        int res = 0;
        int res1 = 0;
        System.out.println("topId.length():"+topId.length());
        if(topId.length() > 0){
            Topic topic = topicService.selectOneByTopId(topId);
            if(topic.getTopStatus() != 0){
                return JsonMsg.fail().addInfo("review_fail_error","该自拟课题已审核");
            }else{
                int status = Integer.parseInt(topStatus);
                System.out.println("status:"+status);
                topic.setTopStatus(status);
                System.out.println("topicGiver:"+topic.getTopGiver());
                res = topicService.updateTopById(topId,topic);
                if(res != 1)
                    return JsonMsg.fail().addInfo("review_fail_error","自拟课题审核异常,请重新尝试！");
                else if(status == -1){
                    //审核不通过
                    return JsonMsg.success();
                }else{
                    //审核通过
                    Group group = groupService.selGroByCurIdAndStuId(curId,topic.getTopGiver());
                    group.setTopId(topId);
                    res1 = groupService.updateTopOfGroup(group.getGroId(),group);
                    if(res1 != 1)
                        return JsonMsg.fail().addInfo("review_fail_error","自拟课题审核异常,请重新尝试！");
                    else
                        return JsonMsg.success();
                }
            }
        }
        return JsonMsg.fail().addInfo("review_fail_error","自拟课题信息有误！");
    }

    //getTopById
    @RequestMapping(value = "/getTopById/{topId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getTeaById(@PathVariable("topId") String topId){
        System.out.println("topId_get:"+topId);
        Topic topic = null;
        if(topId.length() > 0)
            topic =  topicService.selectOneByTopId(topId);
        System.out.println("topId_get:"+topic.toString());
        if(topic != null)
            return JsonMsg.success().addInfo("topic",topic);
        else
            return  JsonMsg.fail().addInfo("top_get_error","无课题信息");
    }

    //查询选题时间
    @RequestMapping(value = "/checkTopTime/{curId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg checkTopTime(@PathVariable("curId") String curId) throws ParseException {
        System.out.println("curId:"+curId);
        Curriculum curriculum = null;
        if(curId.length() > 0)
            curriculum =  curriculumService.selCurById(curId);
        System.out.println("curriculum:"+curriculum.toString());
        String startTime = curriculum.getStartTime();
        String endTime = curriculum.getEndTime();
        if(startTime == null && endTime == null){
            return  JsonMsg.fail().addInfo("time_error","教师未设置选题时间，请耐心等待！");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(startTime);
        Date end = sdf.parse(endTime);
        //获取当前时间
        Date current = new Date();
        System.out.println(current);

        if(current.before(start)){
            return  JsonMsg.fail().addInfo("time_error","未到选题时间，请耐心等待！");
         }else if(current.after(end)){
            return  JsonMsg.fail().addInfo("time_error","当前时间不再选题时间，如未选题请联系指导教师！");
         } else{
            return JsonMsg.success().addInfo("curId",curId);
         }
    }

    //检查报告时间
    @RequestMapping(value = "/checkReportTime/{curId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg checkReportTime(@PathVariable("curId") String curId) throws ParseException {
        System.out.println("curId:"+curId);
        Curriculum curriculum = null;
        if(curId.length() > 0)
            curriculum =  curriculumService.selCurById(curId);
        System.out.println("curriculum:"+curriculum.toString());
        String startTime = curriculum.getStartTime();
        String endTime = curriculum.getEndTime();
        if(startTime == null && endTime == null){
            return  JsonMsg.fail().addInfo("time_error","教师未设置选题时间，请耐心等待！");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(startTime);
        Date end = sdf.parse(endTime);
        //获取当前时间
        Date current = new Date();
        System.out.println(current);

        if(current.before(start)){
            return  JsonMsg.fail().addInfo("time_error","选题开始后才可以下载报告，请耐心等待！");
        }else{
            return JsonMsg.success().addInfo("curId",curId);
        }
    }


    //获得某课程课题列表
    @RequestMapping(value = "/getTopList", method = RequestMethod.GET)
    public ModelAndView getTeaList(@RequestParam(value = "curId") String curId, HttpSession session){
        ModelAndView mdv;
        if(curId.length() == 0 && curId == null)
            return new ModelAndView("errInfo").addObject("errInfo","没有信息");
        String role = (String)session.getAttribute("role");
        if("T".equals(role)){
            mdv = new ModelAndView("teaTopicPage");
        }else{
            mdv = new ModelAndView("stuTopicPage");
        }

        List<Topic> topics = null;
        if("S".equals(role))
           topics = topicService.getTopsListByCur(curId);
       else
           topics = topicService.getTopsListByCurOfTea(curId);

        Curriculum curriculum = curriculumService.selCurById(curId);
        String startTime = curriculum.getStartTime();
        String endTime = curriculum.getEndTime();
//        System.out.println("选题时间:"+startTime+"-"+endTime);

        if("S".equals(role)){
            Student student = (Student) session.getAttribute("student");
            Group group =  groupService.selGroByCurIdAndStuId(curId,student.getStuId());
            if(group.getTopId() != null){
                System.out.println("group:"+group.toString());
                Topic topic = topicService.selectOneByTopId(group.getTopId());
                if(topic != null){
                    if(topic.getTopId() != null)
                        mdv.addObject("msg","你选择的课题："+ topic.getTopName());
                }
            }else{
                session.removeAttribute("msg");
            }
        }

        mdv.addObject("topics",topics)
                .addObject("curId",curId)
                .addObject("startTime",startTime)
                .addObject("endTime",endTime);
        return mdv;
    }

    //自拟课题
    @RequestMapping(value = "/draftTop/{curId}",method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg AddDraftTop(@PathVariable(value = "curId") String curId,Topic topic, HttpSession session) throws ParseException {
        Curriculum c = curriculumService.selCurById(curId);
        String endTime = c.getEndTime();
        if(endTime != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date end = sdf.parse(endTime);
            //获取当前时间
            Date current = new Date();
            System.out.println("选题结束时间："+end);
            System.out.println("现在时间"+current);
            if(current.after(end)){
                return  JsonMsg.fail().addInfo("time_error","选题时间已经结果，不能自拟选题！");
            }
        }

        Student student = (Student) session.getAttribute("student");
        Group group = groupService.selGroByCurIdAndStuId(curId,student.getStuId());
        if(group.getTopId() != null){
            return JsonMsg.fail().addInfo("top_draft_error","自拟命题失败，你已经选过题目！");
        }

        topic.setTopGiver(student.getStuId());
        topic.setTopStatus(0);
        topic.setCurId(curId);
        System.out.println("AddDraftTop:"+ topic.toString());
        Topic top1 = topicService.selTopByCurAndTopName(topic);
        if(top1 != null){
            return JsonMsg.fail().addInfo("top_draft_error","自拟课题失败，该课题已经存在！");
        }

        int res = 0;
        //自拟课题未审核，多次提交自拟课题：更新
        top1 = topicService.selTopByCurAndTopGiver(curId,student.getStuId());
        if(top1 != null){
            if(topic.getTopIntroduce() == null)
                topic.setTopIntroduce("暂无");
            res = topicService.updateTopById(top1.getTopId(),topic);
            if(res != 1){
                return JsonMsg.fail().addInfo("top_draft_error","自拟课题失败，请重新尝试！");
            }
            return JsonMsg.success();
        }

        //第一次提交自拟课题
        int res1= 0;
        if( curId.length() > 0 && student != null){
            if(topic.getTopIntroduce() == null)
                topic.setTopIntroduce("暂无");
            res1 = topicService.insertTop(topic);
            if(res1 != 1)
                return JsonMsg.fail().addInfo("top_draft_error","自拟课题失败，请重新尝试！");

            return JsonMsg.success();
        }
        else{
            return JsonMsg.fail().addInfo("top_draft_error","自拟信息异常");
        }
    }

    //教师添加课题
    @RequestMapping(value = "/addTop/{curId}",method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg addTop(@PathVariable(value = "curId") String curId,Topic topic, HttpSession session) throws ParseException {

        Curriculum c = curriculumService.selCurById(curId);
        String startTime = c.getStartTime();
        if(startTime != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = sdf.parse(startTime);
            //获取当前时间
            Date current = new Date();
            System.out.println("选题开始时间："+start);
            System.out.println("现在时间"+current);
            if(current.after(start)){
                return  JsonMsg.fail().addInfo("time_error","学生已经开始选题，不能添加选题！");
            }
        }

        System.out.println("AddDraftTop:"+ topic.toString());
        Topic top1 = topicService.selTopByCurAndTopName(topic);
        if(top1 != null){
            return JsonMsg.fail().addInfo("top_draft_error","课题添加失败，该课题已经存在！");
        }
        int res = 0;
        if( curId.length() > 0){
            if(topic.getTopIntroduce() == null)
                topic.setTopIntroduce("暂无");
            topic.setTopGiver("教师");
            topic.setTopStatus(0);
            topic.setCurId(curId);
            res = topicService.insertTop(topic);
            if(res != 1)
                return JsonMsg.fail().addInfo("top_draft_error","课题添加失败，请重新尝试！");

            return JsonMsg.success();
        }
        else{
            return JsonMsg.fail().addInfo("top_draft_error","自拟信息异常");
        }
    }


    //查询自拟课题:文件类型参数放在第一个
    @RequestMapping(value = "/getTopCheckList", method = RequestMethod.GET)
    public ModelAndView getTopCheckList(@RequestParam(value = "curId") String curId){
//        System.out.println("getTopList中curId："+curId);
        ModelAndView mdv = new ModelAndView("teaTopicReview");
        List<Topic> topics = topicService.getTopCheckList(curId);

        mdv.addObject("topics",topics)
                .addObject("curId",curId);
        return mdv;
    }


    //导入课题信息
    @RequestMapping(value = "/importTopic/{curId}", method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg importTopicExcel(@RequestParam(value = "topicFile")MultipartFile topicFile,
                                    @PathVariable(value = "curId") String curId) throws ParseException {

        Curriculum c = curriculumService.selCurById(curId);
        if(c.getStartTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = sdf.parse(c.getStartTime());
            Date current = new Date();
            if (current.after(start)) {
                return JsonMsg.fail().addInfo("topicUpload_err", "上传报告失败：选题时间已经开始，请在选题时间开始前上传课题文件！");
            }
        }

        JsonMsg jsonMsg = checkTopicFile(topicFile);
        if(jsonMsg.getCode() != 100){
            return  jsonMsg;
        }
        String fileName = topicFile.getOriginalFilename();
        System.out.println("导入课题的课程号："+ curId);
        Workbook workbook = null;

        if(fileName.endsWith(XLS)) {
            try {
                workbook = new HSSFWorkbook(topicFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                workbook = new XSSFWorkbook(topicFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int sheetCount = workbook.getNumberOfSheets();// 获得工作表个数

        // 遍历工作表
        for (int i = 0; i < sheetCount; i++)
        {
            int rows = 0;
            Sheet sheet = workbook.getSheetAt(i);
            rows = sheet.getLastRowNum();// 获得行数
            System.out.println("importTopic中行数："+rows);//0

            // 获得列数，先获得一行，在得到列数
            Row tmp = sheet.getRow(0);
            if (tmp == null)
                continue;

            // 读取数据 跳过表头
            for (int row = 1; row < rows + 1 ; row++)
            {
                Row r = sheet.getRow(row);
                String cellValue;
                Topic top = new Topic();
                top.setCurId(curId);

                //处理选题名称
                Cell cell = r.getCell(0);
                cellValue = String.valueOf(cell.getStringCellValue());
                top.setTopName(cellValue);

                //处理选题性质
                cell = r.getCell(1);
                cellValue = String.valueOf(cell.getStringCellValue());
                top.setTopNature(cellValue);

                //处理选题描述:cell为空时，使用cell.getStringCellValue()报空指针
                cell = r.getCell(2);
                if(cell != null){
                    cellValue = String.valueOf(cell.getStringCellValue());
                    top.setTopIntroduce(cellValue);
                }else {
                    top.setTopIntroduce("暂无");
                }

                top.setTopGiver("教师");
                top.setTopStatus(0);

                System.out.println(top.toString());
                //检查课题是否已经导入
                Topic top1 = topicService.selTopByCurAndTopName(top);
//                System.out.println("top1："+top1.toString());
                if(top1 != null){
                    System.out.println("课题名："+ top.getTopName() +"已导入");
                    System.out.println(top.equals(top1));
                    //检查与已导入课题信息是否一致
                    if(!top.equals(top1))
                        return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行课题数据与已导入信息不一致！" );
                    else
                        return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行课题数据已导入！" );
                }else{//课题不存在：插入课题
                    System.out.println("课题名："+ top.getTopName() +"尚未导入");
                    topicService.insertTop(top);
                }
            }
        }
        return JsonMsg.success();
    }


    //检查课题文件
    public JsonMsg checkTopicFile(MultipartFile topicFile){

        String fileName = topicFile.getOriginalFilename();
        Workbook workbook = null;

        //判断文件后缀
        if(fileName.endsWith(XLS)) {
            try {
                workbook = new HSSFWorkbook(topicFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                workbook = new XSSFWorkbook(topicFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int sheetCount = workbook.getNumberOfSheets();// 获得工作表个数

        // 遍历工作表
        for (int i = 0; i < sheetCount; i++)
        {
            int rows = 0;
            Sheet sheet = workbook.getSheetAt(i);
            rows = sheet.getLastRowNum();// 获得行数
            System.out.println("checkTopic中rows:"+ rows);

            // 获得列数，先获得一行，在得到列数
            Row tmp = sheet.getRow(0);
            if (tmp == null)
            {
                continue;
            }

            //检查列数
            System.out.println("列数："+sheet.getRow(0).getPhysicalNumberOfCells());
            if(sheet.getRow(0).getPhysicalNumberOfCells() != 3){
                return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中列数不正确（正确列数：3）" );
            }

            //检查表头
            Row r0 = sheet.getRow(0);
            String cellHead;

            Cell cell0 = r0.getCell(0);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("课题名称")){
                return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ i +"中的第1行第1列列名不正确！正确应为\"课题名称\" " );
            }

            cell0 = r0.getCell(1);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("课题性质")){
                return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第1行第2列列名不正确！正确应为\"课题性质\" " );
            }

            cell0 = r0.getCell(2);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("课题介绍")){
                return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第1行第3列列名不正确！正确应为\"课题介绍\" " );
            }

            // 读取数据 跳过表头
            for (int row = 1; row < rows + 1; row++)
            {
                Row r = sheet.getRow(row);
                String cellValue;

                //处理课题名称
                Cell cell = r.getCell(0);
                System.out.println("课题名称：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第1列课题名称不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第1列课题名称应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());
                    System.out.println("课题名称：" + cellValue);
                    if(cellValue.length() > 50)
                        return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第1列课题名称长度超过限定长度（50） " );

                }

                //处理课题性质
                cell = r.getCell(1);
                System.out.println("课题性质：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第2列课题性质不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第2列课题性质应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());

                    if(cellValue.length() > 20 || cellValue.length() < 1)
                        return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第2列课题性质长度不在规定范围内（1-20） " );
                }

                //处理课题介绍
                cell = r.getCell(2);
                if(cell == null || cell.getCellType() == 3)
                    continue;
                else{
                    System.out.println("课题介绍：" + cell.getCellType());
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第3列课题介绍应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());

                    if(cellValue.length() > 100)
                        return JsonMsg.fail().addInfo("topicUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第3列课题介绍数据长度超过限制长度（100） " );
                }
            }
        }
        return JsonMsg.success();
    }
}
