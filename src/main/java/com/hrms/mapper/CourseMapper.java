package com.hrms.mapper;

import com.hrms.bean.Course;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseMapper {

    String TABLE_NAME = "course";
    String INSERT_COURSE_FIELDS = "couId, couName, couNature, couCredit, couIntroduce, couDept";

    @Delete({"DELETE FROM", TABLE_NAME, "WHERE couId=#{couId}"})
    int deleteByCouId(@Param("couId")String couId);

   /*
   更新课程
   */
    int updateCouByCouId(@Param("couId")String couId,@Param("course") Course course);

    /*
    插入课程
    */
    @Insert({ "INSERT INTO", TABLE_NAME, "(", INSERT_COURSE_FIELDS,")",
            "VALUES(#{couId}," +
                    "#{couName}," +
                    "#{couNature}," +
                    "#{couCredit}," +
                    "#{couIntroduce}," +
                    "#{couDept})"})
    int insertCou(Course course);


    /**
    * 通过课程号查找课程
    */
    Course selectCouByCouId(@Param("couId")String couId);
    Course selectCouByCouName(@Param("couName")String couName);
    List<Course> selectCouByLimitAndOffset(@Param("offset")Integer offset, @Param("limit")Integer limit);
    List<Course> selectCouNameByAdmin();
    @Select({"SELECT COUNT(*) FROM", TABLE_NAME})
    int countCous();
}
