package com.hrms.controller;

import com.hrms.bean.Course;
import com.hrms.bean.Curriculum;
import com.hrms.bean.Teacher;
import com.hrms.service.CourseService;
import com.hrms.service.CurriculumService;
import com.hrms.service.TeacherService;
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

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping(value = "/hrms/tea")
public class TeacherController {

    @Autowired
    TeacherService teacherService;
    @Autowired
    CourseService courseService;
    @Autowired
    CurriculumService curriculumService;

    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    //获得专业列表
    @RequestMapping(value = "/getTeaMajorList", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getTeaMajorList(){
        List<Teacher> majors = null;
        majors =   teacherService.getTeaMajorList();
        if(majors ==  null)
            return JsonMsg.fail().addInfo("major_get_error","专业为空！");

        List<Teacher> teachers = null;
        teachers = teacherService.getTeasByMajor(majors.get(0).getTeaMajor());
        if(teachers == null)
            return JsonMsg.fail().addInfo("major_get_error","该专业没有教师！");

        for(Teacher t:teachers)
            System.out.println("teacher:"+t.toString());
        return JsonMsg.success().addInfo("majors",majors)
                                .addInfo("teachers",teachers);
    }

    //获得某专业教师列表
    @RequestMapping(value = "/getTeasByMajor/{teaMajor}", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getTeasByMajor(@PathVariable(value = "teaMajor")String teaMajor){
        List<Teacher> teachers = null;
        teachers = teacherService.getTeasByMajor(teaMajor);
        if(teachers == null)
            return JsonMsg.fail().addInfo("tea_get_error","该专业没有教师！");
        return JsonMsg.success().addInfo("teachers",teachers);
    }


    //删除教师
    @RequestMapping(value = "/delTea/{teaId}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonMsg delTeaById(@PathVariable("teaId")String teaId){
        System.out.println("delTeaId:"+teaId);
        int res = 0;
        if(teaId.length() > 0){
            int c = curriculumService.countCousByTea(teaId);
            if(c != 0)
                return JsonMsg.fail().addInfo("tea_del_error","删除失败：该教师已经分配课程！");
            res = teacherService.delTeaById(teaId);
        }
        if(res != 1)
            return JsonMsg.fail().addInfo("tea_del_error","教师删除异常");
        else
            return JsonMsg.success();
    }

    //添加教师
    @RequestMapping(value = "/addTea", method = RequestMethod.PUT)
    @ResponseBody
    public JsonMsg insertStu(Teacher teacher){

        //查看教师是否添加过
        Teacher t = teacherService.selectOneByTeaId(teacher.getTeaId());
        if(t != null){
            return JsonMsg.fail().addInfo("tea_add_error","添加失败，该教师已经添加过！");
        }
        teacher.setTeaPass("111111");
        int res = teacherService.insertTea(teacher);
        if(res != 1)
            return JsonMsg.fail().addInfo("tea_add_error","添加教师异常");
        return JsonMsg.success();
    }

    //通过教工号查找教师
    @RequestMapping(value = "/getTeaById/{teaId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getTeaById(@PathVariable("teaId") String teaId){
        System.out.println("teaId_get:"+teaId);
        Teacher teacher = null;
        if(teaId.length() > 0)
            teacher =  teacherService.selectOneByTeaId(teaId);
        System.out.println("teaId_get:"+teacher.toString());
        if(teacher != null)
            return JsonMsg.success().addInfo("teacher",teacher);
        else
            return  JsonMsg.fail().addInfo("tea_get_error","无教师信息");
    }

    //更新教师
    @RequestMapping(value = "/updateTea/{teaId}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonMsg updateTeaById(@PathVariable("teaId")String teaId,Teacher teacher){
        int res = 0;
        if(teaId.length() > 0)
            res = teacherService.updateTeaById(teaId,teacher);
        if(res != 1)
            return JsonMsg.fail().addInfo("tea_edit_error","密码更新异常");
        else
            return JsonMsg.success();
    }

    //更新教师密码
    @RequestMapping(value = "/updateTeaPass/{pass1}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonMsg updateTeaPass(@PathVariable(value = "pass1")String pass1, HttpSession session){
        Teacher teacher = (Teacher)session.getAttribute("teacher");
        teacher.setTeaPass(pass1);
        int res = 0;
        res = teacherService.updateTeaById(teacher.getTeaId(),teacher);
        if(res != 1)
            return JsonMsg.fail().addInfo("pass_edit_error","教师信息更新异常");
        else
            return JsonMsg.success();
    }



    //获得总页数
    @RequestMapping(value = "/getTotalPages", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getTotalPages(){
        //每页显示的记录行数
        int limit = 5;
        //总记录数
        int totalItems = teacherService.getTeaCount();
        int temp = totalItems / limit;
        int totalPages = (totalItems % limit== 0) ? temp : temp+1;

        return JsonMsg.success().addInfo("totalPages", totalPages);
    }

    //获得教师列表
    @RequestMapping(value = "/getTeaList", method = RequestMethod.GET)
    public ModelAndView getTeaList(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo){
        ModelAndView mdv = new ModelAndView("adminTeacherPage");
        int limit = 5;
        int totalItems = teacherService.getTeaCount();
        int tmp = totalItems / limit;
        int totalPages = (totalItems % limit == 0) ? tmp : tmp+1;
        int offset = (pageNo - 1) * limit;
        List<Teacher> teachers = teacherService.getTeasList(offset, limit);

        mdv.addObject("teachers",teachers)
                .addObject("totalItems",totalItems)
                .addObject("totalPages",totalPages)
                .addObject("curPageNo",pageNo);
        return mdv;
    }

    //导入课程-教师表
    @RequestMapping(value = "/importTea", method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg importTea(@RequestParam(value = "myFile", required = false) MultipartFile myFile){

        JsonMsg jsonMsg = checkTeacherFile(myFile);

        if(jsonMsg.getCode() != 100){
            return  jsonMsg;
        }

        String fileName = myFile.getOriginalFilename();
        Workbook workbook = null;

        if(fileName.endsWith(XLS)) {
            try {
                workbook = new HSSFWorkbook(myFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                workbook = new XSSFWorkbook(myFile.getInputStream());
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
            String sheetName = sheet.getSheetName();
            String sheetNameSplit []= sheetName.split("_");
            int curYear = Integer.parseInt(sheetNameSplit[0]);
            String curName = sheetNameSplit[1];
            Course course = courseService.selectCouByCouName(curName);
            rows = sheet.getLastRowNum();// 获得行数
            System.out.println("importTea中总行数："+rows);

            // 获得列数，先获得一行，在得到列数
            Row tmp = sheet.getRow(0);
            if (tmp == null)
                continue;

            // 读取数据 跳过表头
            for (int row = 1; row < rows + 1; row++)
            {
                Row r = sheet.getRow(row);
                String cellValue;
                Teacher tea = new Teacher();

                //处理教工号
                Cell cell = r.getCell(0);
                cellValue = String.valueOf(cell.getStringCellValue());
                String  teaId = cellValue;
                tea.setTeaId(cellValue);

                //处理姓名
                cell = r.getCell(1);
                cellValue = String.valueOf(cell.getStringCellValue());
                tea.setTeaName(cellValue);

                //处理专业
                cell = r.getCell(2);
                cellValue = String.valueOf(cell.getStringCellValue());
                tea.setTeaMajor(cellValue);

                //处理院系
                cell = r.getCell(3);
                cellValue = cell.getStringCellValue();
                tea.setTeaDept(cellValue);


                //检查教师是否已经导入
                Teacher tea1 = teacherService.selectOneByTeaId(teaId);
                if(tea1 != null){
                    System.out.println("教工号："+tea.getTeaId()+"已导入");
                    System.out.println(tea.equals(tea1));
                    //检查与已导入教师信息是否一致
                    if(!tea.equals(tea1))
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行教师数据与已导入信息不一致！" );

                    //判断教师与课程关联关系是否存在
                    Curriculum cur = curriculumService.selCurByCouAndTeaAndYear(course.getCouId(),tea.getTeaId(),curYear);

                    if(cur != null)  // 关联关系存在
                    {
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行该课程中已经存在该任课教师！" );
//                        continue;
                    }
                    else //关联关系不存在，创建关联关系
                        curriculumService.insertCur(course.getCouId(),tea.getTeaId(),curYear);
                }else{//教师不存在：插入教师，创建关联关系
                    System.out.println("教工号："+cellValue+"尚未导入");
                    teacherService.insertTea(tea);
                    curriculumService.insertCur(course.getCouId(),tea.getTeaId(),curYear);
                }

            }

        }
        return jsonMsg.success().addInfo("fileUpload_suc","导入成功");
    }


    //检查课程文件
    public JsonMsg checkTeacherFile(MultipartFile teacherFile){

        String fileName = teacherFile.getOriginalFilename();
        Workbook workbook = null;

        if(fileName.endsWith(XLS)) {
            try {
                workbook = new HSSFWorkbook(teacherFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                workbook = new XSSFWorkbook(teacherFile.getInputStream());
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
            String sheetName = sheet.getSheetName();
            String sheetNameSplit []= sheetName.split("_");
            if(sheetNameSplit.length != 2){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"文件名不正确！" );
            }
            int curYear = Integer.parseInt(sheetNameSplit[0]);

            //检查课程年份
            String curName = sheetNameSplit[1];
            Calendar date = Calendar.getInstance();
            int year = Integer.parseInt(String.valueOf(date.get(Calendar.YEAR)));
            if(curYear < year){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"文件名中的年份"+ curYear +"不正确！年份应该大于等于当前年份（ "+ year+")" );
            }

            //检查课程存在
            Course course = courseService.selectCouByCouName(curName);
            if(course == null){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"文件名中的课程名"+curName +"不存在！请添加该课程 " );
            }

            System.out.println("导入课程教师分组的课程："+year+"_"+course.getCouName());
            rows = sheet.getLastRowNum();// 获得行数
            System.out.println("列数："+rows);
            // 获得列数，先获得一行，在得到列数
            Row tmp = sheet.getRow(0);
            if (tmp == null)
            {
                continue;
            }

            //检查列数
            System.out.println("列数："+sheet.getRow(0).getPhysicalNumberOfCells());
            if(sheet.getRow(0).getPhysicalNumberOfCells() != 4){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+ sheetName +"列数不正确（正确列数：4）" );
            }

            //检查表头
            Row r0 = sheet.getRow(0);
            String cellHead;

            Cell cell0 = r0.getCell(0);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("教工号")){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中sheet"+ i +"中的第1行第1列列名不正确！正确应为\"教工号\" " );
            }

            cell0 = r0.getCell(1);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("姓名")){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第1行第2列列名不正确！正确应为\"姓名\" " );
            }

            cell0 = r0.getCell(2);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("专业")){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第1行第3列列名不正确！正确应为\"专业\" " );
            }

            cell0 = r0.getCell(3);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("院系")){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第1行第4列列名不正确！正确应为\"院系\" " );
            }

            // 读取数据 跳过表头
            for (int row = 1; row < rows + 1; row++)
            {
                Row r = sheet.getRow(row);
                String cellValue;

                //处理教工号
                Cell cell = r.getCell(0);
                System.out.println("教工号：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行第1列教工号不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行第1列教工号应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());

                    if(cellValue.length() != 8)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行第1列教工号长度应为8 " );

                }

                //处理姓名
                cell = r.getCell(1);
                System.out.println("姓名：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行第2列姓名不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行第2列姓名应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());

                    if(cellValue.length() > 20 || cellValue.length() < 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行第2列姓名长度不在规定范围内（1-20） " );
                }

                //处理专业
                cell = r.getCell(2);
                System.out.println("专业：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行第3列专业不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行第3列专业应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());

                    if(cellValue.length() > 10)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行第3列专业数据长度超过限制长度（10） " );
                }

                //处理院系
                cell = r.getCell(3);
                System.out.println("院系：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行第4列院系不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行第4列院系应为字符串类型 " );

                    cellValue = cell.getStringCellValue();

                    if(cellValue.length() > 10)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"中的第"+ (row+1) +"行第4列院系长度超过限定长度（10字） " );
                }
            }
        }
        return JsonMsg.success();
    }
}
