package com.hrms.controller;

import com.hrms.bean.Course;
import com.hrms.bean.Curriculum;
import com.hrms.bean.Student;
import com.hrms.bean.Teacher;
import com.hrms.mapper.CurriculumMapper;
import com.hrms.service.CourseService;
import com.hrms.service.CurriculumService;
import com.hrms.util.JsonMsg;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/hrms/course")
public class CourseController {

    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    @Autowired
    CourseService courseService;
    @Autowired
    CurriculumService curriculumService;

    @RequestMapping(value = "/delCou/{couId}", method = RequestMethod.DELETE)
    @ResponseBody
    JsonMsg delCou(@PathVariable("couId")String couId){
        int res = 0;
        if(couId.length() > 0){
          int count = curriculumService.countByCouId(couId);
          if(count != 0)
              return JsonMsg.fail().addInfo("cou_del_error","刪除失敗：该课程已经分配指导教师！");

            res = courseService.deleteCouByCouId(couId);
        }
        if(res != 1){
            return JsonMsg.fail().addInfo("cou_del_error","课程删除异常");
        }
        return JsonMsg.success();
    }

    @RequestMapping(value = "/addCou", method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg addCou(Course course){
        System.out.println("addCou");
        if(course.getCouIntroduce() == null)
            course.setCouIntroduce("暂无");

        //查看科目是否添加过
        Course c = courseService.getCouByCouId(course.getCouId());
        if(c!= null)
            return JsonMsg.fail().addInfo("cou_add_error","添加失败，该科目已经添加过！");
         float credit = (float) (Math.round(c.getCouCredit()*100)/100.0);
        int res = courseService.addCou(course);
        if(res != 1)
            return JsonMsg.fail().addInfo("cou_add_error","课程添加异常");
        else
            return JsonMsg.success();
    }

    @RequestMapping(value = "/getCouByCouId/{couId}",method = RequestMethod.GET)
    @ResponseBody
    JsonMsg getCouByCouId(@PathVariable("couId") String couId){
        Course cou = null;
        if(couId.length() > 0) {
            cou = courseService.getCouByCouId(couId);
            System.out.println(cou.toString());
        }
        if(cou != null)
            return JsonMsg.success().addInfo("course",cou);
        else
             return JsonMsg.success().addInfo("cou_get_error","无课程信息");
    }

    //更新课程
    @RequestMapping(value = "/updateCou/{couId}", method = RequestMethod.PUT)
    @ResponseBody
    JsonMsg updateCouByCouId(@PathVariable("couId") String couId, Course course) {
        int res = 0;
        if(couId.length() > 0)
            res = courseService.updateCouByCouId(couId, course);
        if(res != 1)
            return JsonMsg.fail().addInfo("cou_update_error","课程更新失败");

        return JsonMsg.success();
    }

    @RequestMapping(value = "/getTotalPages", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getTotalPage(HttpSession session){
        String role = (String)session.getAttribute("role");
        int totalItems = 0;

        if(role.equals("A"))
            totalItems = courseService.countCous();
        else if(role.equals("T")){
            Teacher teacher = (Teacher)session.getAttribute("role");
            totalItems = curriculumService.countCousByTea(teacher.getTeaId());
        }else{
            Student student = (Student)session.getAttribute("student");
            totalItems = curriculumService.countCousByStu(student.getStuId());
        }
        //获取总的页数
        int temp = totalItems / 5;
        int totalPages = (totalItems % 5 == 0) ? temp : temp+1;
        return JsonMsg.success().addInfo("totalPages", totalPages);
    }

    @RequestMapping(value = "/getCouList", method = RequestMethod.GET)
    public ModelAndView getCouList(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo){
         ModelAndView mdv = new ModelAndView("adminCoursePage");
         //每页页数
         int limit = 5;
         //总记录数
         int totalItems = courseService.countCous();
         int temp = totalItems / limit ;
         //页数
         int totalPages = (totalItems % limit == 0) ? temp : temp+1;
         //每页第一行
         int offset = (pageNo-1)*limit;
         List<Course> courses = courseService.getCouList(offset, limit);
         mdv.addObject("courses",courses)
                 .addObject("totalItems",totalItems)
                 .addObject("totalPages",totalPages)
                .addObject("curPageNo",pageNo);
         return mdv;
     }

    //管理员获得课程名字列表
    @RequestMapping(value = "/getCouNameByAdmin", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getCouNameByAdmin(){
        List<Course> courseList = courseService.selectCouNameByAdmin();
        if (courseList != null){
            for(Course c:courseList){
                System.out.println(c.getCouName());
            }
            return JsonMsg.success().addInfo("courseList", courseList);
        }
        return JsonMsg.fail().addInfo("courseList_get_err", "没有课程,请添加课程!");
    }


    //导入课程信息表
    @RequestMapping(value = "/importCourse", method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg impotCourse(@RequestParam(value = "myFile") MultipartFile myFile) throws IOException {

        JsonMsg jsonMsg = checkCourseFile(myFile);

        if(jsonMsg.getCode() != 100){
            return  jsonMsg;
        }

        String fileName = myFile.getOriginalFilename();
        Workbook workbook = null;

        if(fileName.endsWith(XLS))
            workbook = new HSSFWorkbook(myFile.getInputStream());
        else
            workbook = new XSSFWorkbook(myFile.getInputStream());

        int sheetCount = workbook.getNumberOfSheets();// 获得工作表个数
        int rows = 0;

        // 遍历工作表
        for (int i = 0; i < sheetCount; i++)
        {
            Sheet sheet = workbook.getSheetAt(i);
            rows = sheet.getLastRowNum();// 获得行数
            System.out.println("importCourse行数rows："+rows);
            // 获得列数，先获得一行，在得到列数
            Row tmp = sheet.getRow(0);
            if (tmp == null)
                continue;

            // 读取数据 跳过表头
            for (int row = 1; row < rows + 1; row++)
            {
                int res = 0;
                Row r = sheet.getRow(row);
                String cellValue;
                Course cou = new Course();

                //处理课程号
                Cell cell = r.getCell(0);
                cellValue = String.valueOf(cell.getStringCellValue());
                cou.setCouId(cellValue);

                //处理课程名
                cell = r.getCell(1);
                cellValue = String.valueOf(cell.getStringCellValue());
                cou.setCouName(cellValue);

                //处理性质
                cell = r.getCell(2);
                cellValue = String.valueOf(cell.getStringCellValue());
                cou.setCouNature(cellValue);

                //处理学分
                cell = r.getCell(3);
                float couCredit = (float) (Math.round(cell.getNumericCellValue()*100)/100.0);
                cou.setCouCredit(couCredit);

                //处理课程简介
                cell = r.getCell(4);
                if(cell == null || cell.getCellType() == 3){
                    cou.setCouIntroduce("暂无");
                }else{
                    cellValue = String.valueOf(cell.getStringCellValue());
                    cou.setCouIntroduce(cellValue);
                }

                //处理开设院系
                cell = r.getCell(5);
                cellValue = cell.getStringCellValue();
                cou.setCouDept(cellValue);

//                cellValue = cell.getStringCellValue();

               res = courseService.addCou(cou);
               if(res != 1)
                   return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ i +"中的第" + (row + 1) + "行未插入：不正确" );

            }

        }
        return jsonMsg.success().addInfo("fileUpload_suc","导入成功");
    }


    //检查课程文件
    public JsonMsg checkCourseFile(MultipartFile courseFile){

        String fileName = courseFile.getOriginalFilename();
        Workbook workbook = null;

        if(fileName.endsWith(XLS)) {
            try {
                workbook = new HSSFWorkbook(courseFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                workbook = new XSSFWorkbook(courseFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        int sheetCount = workbook.getNumberOfSheets();// 获得工作表个数
        int rows = 0;

        // 遍历工作表
        for (int i = 0; i < sheetCount; i++)
        {
            Sheet sheet = workbook.getSheetAt(i);
            rows = sheet.getLastRowNum();// 获得行数
            System.out.println("importCourse行数rows："+rows);

            // 获得列数，先获得一行，在得到列数
            Row tmp = sheet.getRow(0);
            if (tmp == null)
            {
                continue;
            }

            //检查列数
            System.out.println("列数："+sheet.getRow(0).getPhysicalNumberOfCells());
            if(sheet.getRow(0).getPhysicalNumberOfCells() != 6){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中列数不正确（正确列数：6）" );
            }

            //检查表头
                Row r0 = sheet.getRow(0);
                String cellHead;

                Cell cell0 = r0.getCell(0);
                cellHead = String.valueOf(cell0.getStringCellValue());
                System.out.println(cellHead);
                if(!cellHead.equals("课程编号")){
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ i +"中的第1行第1列列名不正确！正确应为\"课程编号\" " );
                }

                cell0 = r0.getCell(1);
                cellHead = String.valueOf(cell0.getStringCellValue());
                System.out.println(cellHead);
                if(!cellHead.equals("课程名称")){
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第1行第2列列名不正确！正确应为\"课程名称\" " );
                }

                cell0 = r0.getCell(2);
                cellHead = String.valueOf(cell0.getStringCellValue());
                System.out.println(cellHead);
                if(!cellHead.equals("课程性质")){
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第1行第3列列名不正确！正确应为\"课程性质\" " );
                }

                cell0 = r0.getCell(3);
                cellHead = String.valueOf(cell0.getStringCellValue());
                System.out.println(cellHead);
                if(!cellHead.equals("学分")){
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第1行第4列列名不正确！正确应为\"学分\" " );
                }

                cell0 = r0.getCell(4);
                cellHead = String.valueOf(cell0.getStringCellValue());
                System.out.println(cellHead);
                if(!cellHead.equals("课程介绍")){
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第1行第5列列名不正确！正确应为\"课程介绍\" " );
                }

                cell0 = r0.getCell(5);
                cellHead = String.valueOf(cell0.getStringCellValue());
                System.out.println(cellHead);
                if(!cellHead.equals("开设院系")){
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第1行第6列列名不正确！正确应为\"开设院系\" " );
                }


            // 读取数据 跳过表头
            for (int row = 1; row < rows+1 ; row++)
            {
                Row r = sheet.getRow(row);
                String cellValue;
                Course course = new Course();

                //处理课程号
                Cell cell = r.getCell(0);
                System.out.println("课程号：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第1列课程号不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第1列课程号应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());

                    if(cellValue.length() != 10)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第1列课程号长度应为10 " );

                    Course cou = courseService.getCouByCouId(cellValue);
                    if(cou != null) {
                        System.out.println("导入失败的课程名："+ cou.getCouName());
                        return JsonMsg.fail().addInfo("fileUpload_err", "导入失败：" + fileName + "中sheet" + (i + 1) + "中的第" + (row + 1) + "行课程已经存在");
                    }
                }

                //处理课程名
                cell = r.getCell(1);
                System.out.println("课程名：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第2列课程名不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第2列课程名应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());
                    Course course1 = courseService.selectCouByCouName(cellValue);
                    if(course1 != null) {
                        System.out.println("导入失败的课程名："+ course1.getCouName());
                        return JsonMsg.fail().addInfo("fileUpload_err", "导入失败：" + fileName + "中sheet" + (i + 1) + "中的第" + (row + 1) + "行第2列课程与已存在的课程号不一致");
                    }
                    System.out.println("课程名长度"+cellValue.length());
                    if(cellValue.length() > 40 || cellValue.length() < 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第2列课程名长度不在规定范围内（1-20） " );
                }

                //处理性质
                cell = r.getCell(2);
                System.out.println("课程性质：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第3列课程性质不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第3列课程性质应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());

                    if(cellValue.length() != 3)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第3列课程性质数据长度应为3 " );
                }

                //处理学分
                cell = r.getCell(3);
                System.out.println("学分：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第4列学分不能为空 " );
                else{
                    if(cell.getCellType() != 0)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第4列学分应为数字类型 " );

                    float couCredit = (float)cell.getNumericCellValue();

                    if(couCredit > 5.0)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第4列学分最大值不超过5.0 " );
                }

                //处理课程简介
                cell = r.getCell(4);
//                System.out.println("简介：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    continue;
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第5列课程简介应为字符串类型 " );

                    cellValue = cell.getStringCellValue();

                    if(cellValue.length() > 100)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第5列课程简介长度超过限定长度（100）" );
                }

                //处理开设院系
                cell = r.getCell(5);
                System.out.println("开设院系：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    continue;
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第6列开设院系应为字符串类型 " );

                    cellValue = cell.getStringCellValue();
                    if(cellValue.length() > 10)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ (i+1) +"中的第"+ (row+1) +"行第6列开设院系长度超过限定长度（10字）" );
                }



            }
        }
        return JsonMsg.success();
    }

}
