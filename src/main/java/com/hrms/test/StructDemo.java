package com.hrms.test;

import com.hrms.bean.Group;
import com.hrms.service.CurriculumService;
import com.hrms.service.GroupService;
import com.hrms.util.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/hrms/test")
public class StructDemo {
    public String[] groId;
    public String[] score1;
    public String[] score2;
    public String[] score3;
    @Autowired
    GroupService groupService;

    public String[] getGroId() {
        return groId;
    }

    public void setGroId(String[] groId) {
        this.groId = groId;
    }

    public String[] getScore1() {
        return score1;
    }

    public void setScore1(String[] score1) {
        this.score1 = score1;
    }

    public String[] getScore2() {
        return score2;
    }

    public void setScore2(String[] score2) {
        this.score2 = score2;
    }

    public String[] getScore3() {
        return score3;
    }

    public void setScore3(String[] score3) {
        this.score3 = score3;
    }

    //教师修改成绩
    @RequestMapping(value ="/updateScoreList", method = RequestMethod.POST)
    @ResponseBody
    public JsonMsg UpdateScoreList(@RequestBody Map<String,String> a) throws ParseException {

        System.out.println(a.get("a"));
//        Group g = groupService.selectGroById(groId);
//        String scoEnd = g.getScoEnd();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date end = sdf.parse(scoEnd);
//        Date current = new Date();

//         if(current.after(end))
//             return JsonMsg.fail().addInfo("sco_update_error", "修改失败：当前时间不在登记成绩时间范围！");

//        String[] groId = a.getGroId();
//        String[] score1 = a.getScore1();
//        String[] score2 = a.getScore2();
//        String[] score3 = a.getScore3();
//        System.out.println("groId:"+groId[0]);
//        System.out.println("score1:"+score1[0]);
//        System.out.println("score2:"+score2[0]);
//        for(Group g:groups){
//            int res = 0;
//            Group group = groupService.selectByGroId(g.getGroId());
//            double score1 = Math.round(g.getScore1()*100)/100;
//            double score2 = Math.round(g.getScore2()*100)/100;
//            double score3 = Math.round(g.getScore3()*100)/100;
//
//            double total = score1 *  g.getSco1Count() +score2  * g.getSco2Count()+ score3 *g.getSco3Count();
//            double total1 = Math.round(total*100)/100;
//            group.setScore1(score1);
//            group.setScore2(score2);
//            group.setScore3(score3);
//            group.setTotal(total1);
//            res = groupService.updateTopOfGroup(group.getGroId(), group);
//
//            if (res != 1){
//                return JsonMsg.fail().addInfo("sco_update_error", "更改异常");
//            }
//        }
        return JsonMsg.success();
    }

}
