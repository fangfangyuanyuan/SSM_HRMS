package com.hrms.controller;

import com.hrms.bean.Course;
import com.hrms.bean.Curriculum;
import com.hrms.bean.Group;
import com.hrms.service.CourseService;
import com.hrms.service.CurriculumService;
import com.hrms.service.GroupService;
import com.hrms.util.ExcelUtil;
import com.hrms.util.FileToZip;
import com.hrms.util.JsonMsg;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/hrms/file")
public class ModelController {
    @Autowired
    CurriculumService curriculumService;
    @Autowired
    CourseService courseService;
    @Autowired
    GroupService groupService;

    //管理员下载模板
    @RequestMapping(value = "/getModel/{model}", method = RequestMethod.GET)
    public void getCouModel(@PathVariable(value = "model") String model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = null;
        if(model.equals("cou_model"))
            fileName = "Model//课程信息文件上传模板.xlsx";
        if(model.equals("cou_tea_model"))
            fileName = "Model//课程教师分组文件上传模板.xlsx";
        if(model.equals("tea_stu_model"))
            fileName = "Model//教师学生分组文件上传模板.xlsx";
        System.out.println("model:"+model);
        downLoadFile(fileName,request,response);
    }
    //教师下载成绩表模板
    @RequestMapping(value = "/getTopicModel", method = RequestMethod.GET )
    public void getTopicModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = "Model//课题上传模板.xlsx";
        downLoadFile(fileName,request,response);
    }
    //教师下载课题模板
    @RequestMapping(value = "/getScoModel", method = RequestMethod.GET )
    public void getScoModel(@RequestParam(value = "curId")String curId,HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Group> scores = groupService.getScoListByTea(curId);
        //excel标题
        String[] title = {"学号","姓名","成绩一","成绩二","成绩三"};
        String fileName =  "学生成绩表模板.xlsx";

        String sheetName = scores.get(0).getCurYear()+"学年"+scores.get(0).getCouName()+"("+scores.get(0).getTeaName()+")";
        String[][] content = new String[ scores.size() ][ title.length ];
        for (int i = 0; i < scores.size(); i++) {
            Group obj = scores.get(i);
            content[i][0] = obj.getStuId();
            content[i][1] = obj.getStuName();
//            if(obj.getScore1() == null && obj.getScore2() == null && obj.getScore3() == null){
//                continue;
//            }
//            content[i][2] = Double.valueOf(obj.getScore1()).toString();
//            content[i][3] = Double.valueOf(obj.getScore2()).toString();
//            content[i][4] = Double.valueOf(obj.getScore3()).toString();
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

    //下载报告模板
    @RequestMapping(value = "/getReportMould", method = RequestMethod.GET )
    public void getReportMould(@RequestParam(value = "curId")String curId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Curriculum c = curriculumService.selCurById(curId);

        String fileName = c.getMould();
        System.out.println(fileName);
        String zipFilePath0 = request.getSession().getServletContext().getRealPath(fileName);
        System.out.println("zip:"+zipFilePath0);
        FileToZip fileToZip = new FileToZip();
        fileToZip.downLoad(zipFilePath0,response);
    }


    public void downLoadFile(String fileName, HttpServletRequest request, HttpServletResponse response){
        String sourceFilePath = request.getSession().getServletContext().getRealPath(fileName);
        System.out.println("sourceFilePath:"+sourceFilePath);
        File file = new File(sourceFilePath);
        if (file.exists()) {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

