package com.hrms.controller;

import com.hrms.bean.Student;
import com.hrms.bean.Teacher;
import com.hrms.service.StudentService;
import com.hrms.service.TeacherService;
import com.hrms.util.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping(value = "/hrms")
public class LoginController {

    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;

    /**
     * 登录：跳转到登录页面
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(){
        return "userLogin";
    }

    /**
     * 对登录页面输入的用户名和密码做简单的判断
     * @param request
     * @return
     */
    @RequestMapping(value = "/dologin", method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg dologin(HttpServletRequest request, HttpSession session){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        System.out.println(username + password + role);
        session.setAttribute("role", role);
        if(username.isEmpty() || password.isEmpty()){
            return JsonMsg.fail().addInfo("login_error","账号密码不能为空");
        }

       if("A".equals(role)){
           if (!"admin1234".equals(username + password)){
               return JsonMsg.fail().addInfo("login_error", "输入账号用户名与密码不匹配，请重新输入！");
           }
       }else if ("T".equals(role)){
            Teacher teacher = teacherService.selectOneByTeaId(username);
            System.out.println("login_teacher:"+teacher);
            if(teacher == null){
                return JsonMsg.fail().addInfo("login_error", "账号不正确，请重新输入！");
            }
            if(!password.equals(teacher.getTeaPass()))
                return JsonMsg.fail().addInfo("login_error", "密码不正确，请重新输入！");
           session.setAttribute("teacher",teacher);
        } else if ("S".equals(role)){
            Student student =  studentService.selectOneByStuId(username);
            //mybatis的select查找不到对象返回空值
            if(student == null){
                return JsonMsg.fail().addInfo("login_error", "账号不正确，请重新输入！");
            }
            System.out.println("学生：" + student.toString());
            if(!password.equals(student.getStuPass()))
                return JsonMsg.fail().addInfo("login_error", "密码不正确，请重新输入！");
           session.setAttribute("student",student);
        }else {
           return JsonMsg.fail().addInfo("login_error", "用户尚未注册！");
       }
        return JsonMsg.success();
    }

    /**
     * 跳转到主页面
     * @return
     */
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main(@RequestParam(value = "role") String role){
        System.out.println("role:" + role);
        if("A".equals(role)){
            return "adminMain";
        } else if("T".equals(role)){
            return "teaMain";
        } else{
            return "stuMain";
        }
    }



    /**
     * 退出登录：从主页面跳转到登录页面
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session){
        String role = (String) session.getAttribute("role");
        session.removeAttribute("role");

        if ("T".equals(role))
            session.removeAttribute("teacher");
        else
            session.removeAttribute("student");

        return "userLogin";
    }

}
