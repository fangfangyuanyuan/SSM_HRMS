package com.hrms.service;

import com.hrms.bean.Curriculum;
import com.hrms.bean.Topic;
import com.hrms.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    TopicMapper topicMapper;

    public int delTopById(String topId) { return topicMapper.delTopById(topId);}

    public int updateTopById(String topId, Topic topic) { return topicMapper.updateTopById(topId,topic);}

    public int insertTop(Topic topic){return topicMapper.insertTop(topic);}

    public Topic selTopByCurAndTopName(Topic topic){return topicMapper.selTopByCurAndTopName(topic);}
    public int getTopCountByCur(String curId) {return topicMapper.getTopCountByCur(curId);}
    public List<Topic> getTopsListByCur(String curId) { return topicMapper.getTopsListByCur(curId);}
    public Topic selectOneByTopId(String topId) {return topicMapper.selectOneByTopId(topId); }

    public int getTopCheckCount(String curId) { return topicMapper.getTopCheckCount(curId);}

    public List<Topic> getTopCheckList(String curId) { return topicMapper.getTopCheckList(curId);}

    public  List<Topic> selTopByCurId(String curId) { return topicMapper.selTopByCurId(curId);}

    //有问题
    public List<Topic> getTopsListByCurOfTea(String curId) {
        List<Topic> topicList = topicMapper.getTopsList1ByCurOfTea(curId);
        List<Topic> topicList1 = topicMapper.getTopsList2ByCurOfTea(curId);
        for(Topic topic:topicList1){
            topicList.add(topic);
        }
        return topicList;
    }

    public int getTopCountByCurOfTea(String curId) { return topicMapper.getTopCountByCurOfTea(curId);}

    public Topic selTopByCurAndTopGiver(String curId, String stuId) { return topicMapper.selTopByCurAndTopGiver(curId,stuId);}
}
