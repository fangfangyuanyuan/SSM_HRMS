package com.hrms.mapper;

import com.hrms.bean.Group;
import com.hrms.bean.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

public interface GroupMapper {

    String TABLE_NAME = " `group` ";
    String INSERT_CURRICULUM_FIELDS = "curId, stuId";

    @Insert({"INSERT INTO", TABLE_NAME, "(",  INSERT_CURRICULUM_FIELDS, ")",
            "VALUES(#{curId},#{stuId})"})
    int insertGroup(@Param("curId") String curId,@Param("stuId") String stuId);

    int updateTopOfGroup(@Param("groId") String groId,@Param("group") Group group);

    Group selGroByCurIdAndStuId(@Param("curId") String curId,@Param("stuId") String stuId);

    @Select({"SELECT",INSERT_CURRICULUM_FIELDS,"FROM",TABLE_NAME,"WHERE topId=#{topId}"})
    Group selectByTopId(@Param("topId") String topId);

    int countScosByStu(@Param("stuId") String stuId);

    List<Group> getScoListByStu(@Param("stuId") String stuId, @Param("offset") int offset, @Param("limit") int limit);

    int getOptTopCounts(@Param("curId") String curId);

    List<Group> getOptTopListByTea(@Param("curId")String curId);

    int cancelTop(@Param("groId") String groId);

    @Select({"SELECT COUNT(*) FROM",TABLE_NAME,"WHERE stuId=#{stuId} and topId IS NOT NULL"})
    int countOptSucByStu(@Param("stuId")String stuId);

    List<Group> getOptSucList(@Param("stuId")String stuId);

    Group getGroByStuAndCur(@Param("curId")String curId,@Param("stuId") String stuId);

    Group selectByGroId(@Param("groId")String groId);

    @Select({"SELECT COUNT(*) FROM",TABLE_NAME,"WHERE curId=#{curId}"})
    int countScoByTea(@Param("curId")String curId);

    List<Group> getScoListByTea(@Param("curId")String curId);

    @Select({"SELECT COUNT(*) FROM",TABLE_NAME,"WHERE curId=#{curId}"})
    int countStuGro(@Param("curId")String curId);

    List<Group> getStuGroupByAdmin(@Param("curId")String curId, @Param("offset") int offset, @Param("limit") int limit);

    @Delete({"DELETE FROM",TABLE_NAME,"WHERE groId=#{groId}"})
    int delGro(@Param("groId")String groId);

    Group selectGroById(@Param("groId") String groId);

    List<Student> getStuMajorByCur(@Param("curId")String curId);

    @Select({"SELECT curId,AVG(score1) avg1,AVG(score2) avg2,AVG(score3) avg3,AVG(total) avgTotal FROM",TABLE_NAME,"WHERE curId = #{curId}"})
    Group getAvgScoByTea(@Param("curId")String curId);

    List<Group> getScoReport(@Param("curId")String curId);

    List<Group> getOptTopListByCur(@Param("curId")String curId);

    List<Group> getScoListExportByTea(@Param("curId")String curId);


    Group getScoValuesByCouId(@Param("couId")String couId,@Param("curYear") int curYear);

    @Select({"SELECT COUNT(*) FROM",TABLE_NAME,"WHERE stuId= #{stuId}"})
    int countGroByStu(@Param("stuId") String stuId);

    Group selGroByCouIdAndStuIdAndCurYear(@Param("couId")String couId,@Param("stuId") String stuId,@Param("curYear")  int curYear);

    Group getScoValuesByCurId(@Param("curId")String curId);
}
