package com.hrms.service;

import com.hrms.bean.Curriculum;
import com.hrms.bean.Group;
import com.hrms.bean.Student;
import com.hrms.bean.Topic;
import com.hrms.mapper.GroupMapper;
import com.hrms.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class GroupService {
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    TopicMapper topicMapper;

    public int countScosByStu(String stuId) {return groupMapper.countScosByStu(stuId); }

    public List<Group> getScoListByStu(String stuId, int offset, int limit) { return  groupMapper.getScoListByStu(stuId,offset,limit);}
    public Group selGroByCurIdAndStuId(String curId,String stuId){return groupMapper.selGroByCurIdAndStuId(curId,stuId);}

    public int insertGroup(String curId,String stuId){ return groupMapper.insertGroup(curId,stuId);}

    public int updateTopOfGroup(String groId,Group group) { return groupMapper.updateTopOfGroup(groId,group); }

    public int getOptTopCounts(String curId) {return groupMapper.getOptTopCounts(curId);}

    public Group selectByTopId(String topId) {return groupMapper.selectByTopId(topId);}

    public List<Group> getOptTopListByTea(String curId) { return groupMapper.getOptTopListByTea(curId);}

    public int cancelTop(String groId) { return groupMapper.cancelTop(groId);}

    public int countOptsByStu(String stuId) {
        int count0 = topicMapper.countOptUnReview(stuId);
        int count1 = topicMapper.countOptFail(stuId);
        int count2 =  groupMapper.countOptSucByStu(stuId);
        int total = count0 +  count1+ count2 ;
        return total;
    }

    public List<Group> getOptListByStu(String stuId) {
        List<Group> groups = new ArrayList<>();
        for(Group g0:topicMapper.getTopUnReview(stuId)){
            g0.setOptResult("正在审核状态");
            groups.add(g0);
        }
        for(Group g1:topicMapper.getTopFail(stuId)){
            g1.setOptResult("审核不通过");
            groups.add(g1);
        }
        for(Group g2:groupMapper.getOptSucList(stuId)){
            g2.setOptResult("审核通过");
            groups.add(g2);
        }
        return groups;
    }

    public Group getGroByStuAndCur(String curId, String stuId) { return groupMapper.getGroByStuAndCur(curId,stuId);}

    public Group selectByGroId(String groId) { return groupMapper.selectByGroId(groId);}

    public int countScoByTea(String curId) { return groupMapper.countScoByTea(curId);}

    public List<Group> getScoListByTea(String curId) { return groupMapper.getScoListByTea(curId); }

    public int countStuGro(String curId) { return groupMapper.countStuGro(curId);}

    public List<Group> getStuGroupByAdmin(String curId, int offset, int limit) { return groupMapper.getStuGroupByAdmin(curId,offset,limit); }

    public int delGro(String groId) { return groupMapper.delGro(groId);}

    public Group selectGroById(String groId) { return groupMapper.selectGroById(groId);}

    public List<Student> getStuMajorByCur(String curId) { return groupMapper.getStuMajorByCur(curId);}

    public Group getAvgScoByTea(String curId){return groupMapper.getAvgScoByTea(curId);}

    public List<Group> getScoReport(String curId) { return groupMapper.getScoReport(curId);}

    public List<Group> getOptTopListByCur(String curId) { return groupMapper.getOptTopListByCur(curId);}

    public List<Group> getScoListExportByTea(String curId) { return groupMapper.getScoListExportByTea(curId);}

    public Group getScoValuesByCouId(String couId, int curYear) { return groupMapper.getScoValuesByCouId(couId,curYear);}

    public int countGroByStu(String stuId) { return groupMapper.countGroByStu(stuId);}

    public Group selGroByCouIdAndStuIdAndCurYear(String couId, String stuId, int curYear) {
    return groupMapper.selGroByCouIdAndStuIdAndCurYear(couId,stuId,curYear);}

    public Group getScoValuesByCurId(String curId) { return groupMapper.getScoValuesByCurId(curId);}
}
