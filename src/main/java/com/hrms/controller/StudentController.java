package com.hrms.controller;

import com.hrms.bean.*;
import com.hrms.service.*;
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
@RequestMapping(value = "/hrms/student")
public class StudentController {

    @Autowired
    StudentService studentService;
    @Autowired
    CourseService courseService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    CurriculumService curriculumService;
    @Autowired
    GroupService groupService;
    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    //删除学生
    @RequestMapping(value = "/delStu/{stuId}" ,method = RequestMethod.DELETE)
    @ResponseBody
    public JsonMsg delStu(@PathVariable("stuId") String stuId){

        int res = 0;

        if(stuId.length() > 0){
            int count = groupService.countGroByStu(stuId);
            if(count != 0)
                return JsonMsg.fail().addInfo("stu_del_error","删除失败，该学生已经分配课程！");

            res = studentService.delStuById(stuId);
            if(res != 1)
                return JsonMsg.fail().addInfo("stu_del_error","学生删除异常");
            else
                return JsonMsg.success();
        }else{
            return JsonMsg.fail().addInfo("stu_del_error","学号异常");
        }
    }

    //通过学号查找学生
    @RequestMapping(value = "/getStuById/{stuId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getStuById(@PathVariable("stuId") String stuId){

        Student student = null;

        if(stuId.length() > 0)
            student = studentService.selectOneByStuId(stuId);
        if(student != null)
            return JsonMsg.success().addInfo("student", student);
        else
            return JsonMsg.fail().addInfo("stu_get_error","无学生信息");
    }

    //更新学生
    @RequestMapping(value = "/updateStu/{stuId}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonMsg updateStu(@PathVariable("stuId")String stuId,Student student){

        int res = 0;

        if(stuId.length() > 0)
            res = studentService.updateStu(stuId,student);
        if(res !=  1)
            return JsonMsg.fail().addInfo("stu_edit_error","学生更新异常");
        return JsonMsg.success();
    }

    //新增学生
    @RequestMapping(value = "/addStu",method = RequestMethod.PUT)
    @ResponseBody
    public JsonMsg addStu(Student student){

        //查看学生是否添加过
        Student s = studentService.selectOneByStuId(student.getStuId());
        if(s != null)
            return JsonMsg.fail().addInfo("stu_add_error","添加失败，该学生已经添加过！");
        int res = studentService.insertStu(student);

        if(res != 1)
            return JsonMsg.fail().addInfo("stu_add_error","学生插入异常");
        return JsonMsg.success();
    }

    //获得学生总页数
    @RequestMapping(value = "/getTotalPages", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getTotalPages(){
        //每页显示的记录行数
        int limit = 5;
        //总记录数
        int totalItems = studentService.countStu();
        int temp = totalItems / limit;
        int totalPages = (totalItems % limit== 0) ? temp : temp+1;

        return JsonMsg.success().addInfo("totalPages", totalPages);
    }

    //获得学生列表
    @RequestMapping(value = "/getStuList",method = RequestMethod.GET)
    public ModelAndView getStuList(@RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo){
        ModelAndView mdv = new ModelAndView("adminStudentPage");
        int limit = 5;
        int totalItems = studentService.countStu();
        int tmp = totalItems / limit;
        int totalPages = (totalItems % limit == 0) ? tmp : tmp+1;
        int offset = (pageNo - 1) * limit;

        List<Student> students = studentService.getStuList(offset,limit);
        mdv.addObject("students",students)
                .addObject("totalItems",totalItems)
                .addObject("totalPages",totalPages)
                .addObject("curPageNo",pageNo);
        return mdv;
    }

    //获得某课程教师所带的学生列表
    @RequestMapping(value = "/getStuListVaTea",method = RequestMethod.GET)
    public ModelAndView getStuListVaTea(@RequestParam(value = "curId") String curId){
        System.out.println("getStuListVaTea:"+curId);
        ModelAndView mdv = new ModelAndView("teaStudentPage");
        List<Student> students = studentService.getStuListVaTea(curId);
        mdv.addObject("students",students)
                .addObject("curId",curId);
        return mdv;
    }

    //更新学生密码
    @RequestMapping(value = "/updateStuPass/{pass1}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonMsg updateTeaPass(@PathVariable(value = "pass1")String pass1, HttpSession session){
        Student student = (Student)session.getAttribute("student");
        student.setStuPass(pass1);
        int res = 0;
        res = studentService.updateStu(student.getStuId(),student);
        if(res != 1)
            return JsonMsg.fail().addInfo("pass_edit_error","密码更新异常");
        else
            return JsonMsg.success();
    }

    //获得学生专业
    @RequestMapping(value = "/getStuMajorList", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getStuMajorList(){
        List<Student> majors = null;
        majors =   studentService.getStuMajorList();
        if(majors ==  null)
            return JsonMsg.fail().addInfo("major_get_error","专业列表为空！");

        List<Student> classes = null;
        classes = studentService.getClassByMajor(majors.get(0).getStuMajor());
        if(classes == null)
            return JsonMsg.fail().addInfo("major_get_error","该专业没有班级！");

        List<Student> stus = null;
        stus = studentService.selStuByMajorAndClass(majors.get(0).getStuMajor(),classes.get(0).getStuClass());
        if(stus == null)
            return JsonMsg.fail().addInfo("major_get_error","该专业没有学生！");

        for(Student t:majors)
            System.out.println("majors:"+t.getStuMajor());
        for(Student t:classes)
            System.out.println("classes:"+t.getClass());
        for(Student t:stus)
            System.out.println("stus:"+t.toString());

        return JsonMsg.success().addInfo("majors",majors)
                                 .addInfo("classes",classes)
                                 .addInfo("stus",stus);
    }

    //通过专业获得班级列表
    @RequestMapping(value = "/getClassesByMajor/{stuMajor}", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getTeasByMajor(@PathVariable(value = "stuMajor")String stuMajor){
        List<Student> classes = null;
        classes = studentService.getClassByMajor(stuMajor);
        if(classes == null)
            return JsonMsg.fail().addInfo("class_get_error","该专业没有班级！");

        List<Student> stus = null;
        stus = studentService.selStuByMajorAndClass(stuMajor,classes.get(0).getStuClass());
        if(stus == null)
            return JsonMsg.fail().addInfo("class_get_error","该专业没有学生！");

        return JsonMsg.success().addInfo("classes",classes)
                                 .addInfo("stus",stus);
    }

    //通过专业和班级获得学生列表
    @RequestMapping(value = "/getStusByMajorAndClass", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getStusByMajorAndClass(@RequestParam(value = "stuMajor")String stuMajor,
                                          @RequestParam(value="stuClass")int stuClass){
        List<Student> stus = null;
        System.out.println("stuMajor+stuClass:"+stuMajor+stuClass);
        stus = studentService.selStuByMajorAndClass(stuMajor,stuClass);
        if(stus == null)
            return JsonMsg.fail().addInfo("stu_get_error","该班级没有学生！");

        for(Student t:stus)
            System.out.println("stus:"+t.toString());
        return JsonMsg.success().addInfo("stus",stus);
    }


    //导入学生信息
    @RequestMapping(value = "/importStudent", method = RequestMethod.POST)
    @ResponseBody
    public  JsonMsg importStuExcel(@RequestParam(value = "myFile")MultipartFile myFile){
        JsonMsg jsonMsg = checkStudentFile(myFile);

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


        // 遍历工作表
        for (int i = 0; i < sheetCount; i++)
        {
            int rows = 0;
            Sheet sheet = workbook.getSheetAt(i);
            //获取sheet表格名字
            String sheetName = sheet.getSheetName();
            System.out.println("sheetName:"+sheetName);
            String sheetNameSplit [] = sheetName.split("[ _ , .]");
            int curYear = Integer.parseInt(sheetNameSplit[0]);
            String curName = sheetNameSplit[1];
            String teaName = sheetNameSplit[2];

            Course course = courseService.selectCouByCouName(curName);
            Teacher tea = teacherService.selectOneByTeaName(teaName);

            String curId = curriculumService.selCurByCouAndTeaAndYear(course.getCouId(),tea.getTeaId(),curYear).getCurId();
            Group group = new Group();
            group.setCurId(curId);

            rows = sheet.getLastRowNum();// 获得行数
            System.out.println("importStudent总行数rows："+rows);

            // 获得列数，先获得一行，在得到列数
            Row tmp = sheet.getRow(0);
            if (tmp == null)
                continue;

            // 读取数据 跳过表头
            for (int row = 1; row < rows + 1; row++)
            {
                Row r = sheet.getRow(row);
                String cellValue;
                Student stu = new Student();

                //处理学号
                Cell cell = r.getCell(0);
                cellValue = String.valueOf(cell.getStringCellValue());
                String  stuId = cellValue;
                stu.setStuId(cellValue);
                group.setStuId(stu.getStuId());

                //处理姓名
                cell = r.getCell(1);
                cellValue = String.valueOf(cell.getStringCellValue());
                stu.setStuName(cellValue);

                //处理班级
                cell = r.getCell(2);
                int stuClass = (int)cell.getNumericCellValue();
                stu.setStuClass(stuClass);

                //处理专业
                cell = r.getCell(3);
                cellValue = String.valueOf(cell.getStringCellValue());
                stu.setStuMajor(cellValue);

                //处理院系
                cell = r.getCell(4);
                cellValue = cell.getStringCellValue();
                stu.setStuDept(cellValue);


                //检查学生是否已经导入
                Student stu1 = studentService.selectOneByStuId(stu.getStuId());
                if(stu1 != null){
                    System.out.println("学号："+ stu.getStuId() +"已导入");
                    System.out.println(stu.equals(stu1));
                    //检查与已导入学生信息是否一致
                    if(!stu.equals(stu1))
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行学生数据与已导入信息不一致！" );

                    //判断学生与curriculum关联关系是否存在
                    Curriculum c = curriculumService.selCurById(curId);
                    Group g1 = groupService.selGroByCouIdAndStuIdAndCurYear(c.getCouId(),stuId,c.getCurYear());
                    if(g1 != null){
                        if(g1.getTeaId() != c.getTeaId()){
                            return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行该课程学生已经分配给其他教师！");
                        }else{
                            return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行该课程学生已经分配给该教师！");
                        }
                    } else {//关联关系不存在，创建关联关系
                        System.out.println("Group:"+ group.toString());
                        groupService.insertGroup(curId,stu.getStuId());
                    }
                }else{//教师不存在：插入教师，创建关联关系
                    System.out.println("学号："+ stu.getStuId() +"尚未导入");
                    studentService.insertStu(stu);
                    System.out.println("Group:"+ group.toString());
                    groupService.insertGroup(curId,stu.getStuId());
                }

            }
        }
        return jsonMsg.success().addInfo("fileUpload_suc","导入成功");
    }


    //检查课程文件
    public JsonMsg checkStudentFile(MultipartFile stuFile){

        String fileName = stuFile.getOriginalFilename();
        Workbook workbook = null;

        //判断文件后缀
        if(fileName.endsWith(XLS)) {
            try {
                workbook = new HSSFWorkbook(stuFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                workbook = new XSSFWorkbook(stuFile.getInputStream());
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
            //获得sheet表名
            String sheetName = sheet.getSheetName();
            String sheetNameSplit [] = sheetName.split("[ _ ]");
            if(sheetNameSplit.length != 3){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"文件名不正确！" );
            }

            //判断年份
            int curYear = Integer.parseInt(sheetNameSplit[0]);
//        System.out.println("年份："+ curYear);
            Calendar date = Calendar.getInstance();
            int year = Integer.parseInt(String.valueOf(date.get(Calendar.YEAR)));
            if(curYear < year){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"文件名的年份"+ curYear +"不正确！正确年份应大于等于当年年份（ "+ year+")" );
            }

            //判断课程存在
            String curName = sheetNameSplit[1];
//        System.out.println("课程名："+ curName);
            Course course = courseService.selectCouByCouName(curName);
            if(course == null){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"文件名的课程名"+ curName +"不存在！请添加该课程 ！" );
            }

            //判断教师存在
            String teaName = sheetNameSplit[2];
//        System.out.println("教师名："+ teaName);
            Teacher tea =  teacherService.selectOneByTeaName(teaName);
            if(tea == null){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName+"文件名的教师名"+ teaName +"不存在！请添加该教师！ " );
            }

            //判断课程教师关联关系是否存在，不存在则添加
            Curriculum cur =  curriculumService.selCurByCouAndTeaAndYear(course.getCouId(),tea.getTeaId(),curYear);
            if(cur == null)  // 关联关系不存在，创建关联关系
                curriculumService.insertCur(course.getCouId(),tea.getTeaId(),curYear);


            rows += sheet.getLastRowNum();// 获得行数
            System.out.println("rows:"+ rows);

            // 获得列数，先获得一行，在得到列数
            Row tmp = sheet.getRow(0);
            if (tmp == null)
            {
                continue;
            }

            //检查列数
            System.out.println("列数："+sheet.getRow(0).getPhysicalNumberOfCells());
            if(sheet.getRow(0).getPhysicalNumberOfCells() != 5){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName+"中"+sheetName +"列数不正确（正确列数：5）" );
            }

            //检查表头
            Row r0 = sheet.getRow(0);
            String cellHead;

            Cell cell0 = r0.getCell(0);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("学号")){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第1行第1列列名不正确！正确应为\"学号\" " );
            }

            cell0 = r0.getCell(1);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("姓名")){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第1行第2列列名不正确！正确应为\"姓名\" " );
            }

            cell0 = r0.getCell(2);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("班级")){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"中的第1行第3列列名不正确！正确应为\"班级\" " );
            }

            cell0 = r0.getCell(3);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("专业")){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第1行第4列列名不正确！正确应为\"专业\" " );
            }

            cell0 = r0.getCell(4);
            cellHead = String.valueOf(cell0.getStringCellValue());
            System.out.println(cellHead);
            if(!cellHead.equals("院系")){
                return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第1行第5列列名不正确！正确应为\"院系\" " );
            }

            // 读取数据 跳过表头
            for (int row = 1; row < rows; row++)
            {
                Row r = sheet.getRow(row);
                String cellValue;

                //处理学号
                Cell cell = r.getCell(0);
                System.out.println("学号：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第1列学号不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第1列学号应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());
                    System.out.println("学号：" + cellValue);
                    if(cellValue.length() != 12)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第1列学号长度应为12 " );

                }

                //处理姓名
                cell = r.getCell(1);
                System.out.println("姓名：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第2列姓名不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第2列姓名应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());

                    if(cellValue.length() > 20 || cellValue.length() < 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第2列姓名长度不在规定范围内（1-20） " );
                }

                //处理班级
                cell = r.getCell(2);
                System.out.println("班级：" + cell.getCellType());

                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第3列班级不能为空 " );
                else{
                    if(cell.getCellType() != 0)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"中的第"+ (row+1) +"行第3列班级应为数字类型 " );

                    cellValue = String.valueOf(cell.getNumericCellValue());
                    System.out.println("班级长度cellValue.length()："+cellValue.length());
                    if(cellValue.length() != 6)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第3列班级数据长度超过限制长度（4） " );
                }

                //处理专业
                cell = r.getCell(3);
                System.out.println("专业：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第3列专业不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第3列专业应为字符串类型 " );

                    cellValue = String.valueOf(cell.getStringCellValue());

                    if(cellValue.length() > 10)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第3列专业数据长度超过限制长度（10） " );
                }

                //处理院系
                cell = r.getCell(4);
                System.out.println("院系：" + cell.getCellType());
                if(cell == null || cell.getCellType() == 3)
                    return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第4列院系不能为空 " );
                else{
                    if(cell.getCellType() != 1)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第4列院系应为字符串类型 " );

                    cellValue = cell.getStringCellValue();

                    if(cellValue.length() > 10)
                        return JsonMsg.fail().addInfo("fileUpload_err","导入失败："+ fileName +"中"+sheetName +"的第"+ (row+1) +"行第4列院系长度超过限定长度（10字） " );
                }
            }
        }
        return JsonMsg.success();
    }
}
