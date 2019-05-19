package com.hrms.mapper;

import com.hrms.bean.Group;
import com.hrms.bean.Teacher;
import com.hrms.bean.Topic;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TopicMapper {

    String TABLE_NAME = "topic";
    String INSERT_FIELDS = "curId, topName, topNature, topIntroduce, topStatus, topGiver ";
    String SELECT_FIELDS = "topId, " + INSERT_FIELDS;

    @Delete({"DELETE FROM", TABLE_NAME, "WHERE topId =#{topId}"})
    int delTopById(@Param("topId") String topId);

    int updateTopById(@Param("topId") String topId, @Param("topic") Topic topic);

    @Insert({"INSERT INTO",TABLE_NAME,"(",INSERT_FIELDS,") " +
            "VALUES(#{topic.curId}," +
            "#{topic.topName}," +
            "#{topic.topNature}," +
            "#{topic.topIntroduce}," +
            "#{topic.topStatus}," +
            "#{topic.topGiver})"})
    int insertTop(@Param("topic") Topic topic);

    Topic selectOneByTopId(@Param("topId")String topId);
    Topic selTopByCurAndTopName(@Param("topic")Topic topic);
    int getTopCountByCur(@Param("curId") String curId);
    List<Topic> getTopsListByCur(@Param("curId")String curId);

    //有问题
    @Select({"SELECT COUNT(*) FROM",TABLE_NAME,"WHERE curId = #{curId} and topStatus = '-1' and topGiver != '教师'"})
    int getTopCheckCount(@Param("curId")String curId);

    List<Topic> getTopCheckList(@Param("curId")String curId);

    @Select({"SELECT COUNT(*) FROM",TABLE_NAME,"WHERE topGiver =#{stuId} AND topStatus = 0"})
    int countOptUnReview(@Param("stuId")String stuId);

    @Select({"SELECT COUNT(*) FROM",TABLE_NAME,"WHERE topGiver =#{stuId} AND topStatus = -1"})
    int countOptFail(@Param("stuId")String stuId);

    List<Group> getTopUnReview(@Param("stuId") String stuId);

    List<Group> getTopFail(@Param("stuId") String stuId);

    List<Topic> selTopByCurId(@Param("curId") String curId);

    List<Topic> getTopsList1ByCurOfTea(@Param("curId")String curId);

    List<Topic>  getTopsList2ByCurOfTea(@Param("curId")String curId);
    @Select({"SELECT COUNT(*) FROM",TABLE_NAME,"WHERE curId=#{curId} AND topStatus=2"})
    int getTopCountByCurOfTea(@Param("curId")String curId);

    @Select({"SELECT "+SELECT_FIELDS+ " FROM",TABLE_NAME,"WHERE curId=#{curId} AND topGiver=#{stuId} AND topStatus=0"})
    Topic selTopByCurAndTopGiver(@Param("curId")String curId,@Param("stuId") String stuId);
}
