package com.hrms.mapper;

import com.hrms.bean.Teacher;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherMapper {

    String TABLE_NAME = "teacher";
    String INSERT_FIELDS = "teaId, teaName, teaMajor, teaDept";


    /**
     * 删除
     * @param teaId
     * @return
     */
    @Delete({"DELETE FROM",TABLE_NAME,"WHERE teaId = #{teaId}"})
    int delTeaById(@Param("teaId")String teaId);

    /**
     * 更新
     * @param teaId
     * @param teacher
     * @return
     */
    int updateTeaById(@Param("teaId")String teaId,@Param("teacher")Teacher teacher);

    /**
     * 插入
     * @param teacher
     * @return
     */

    @Insert({"INSERT INTO",TABLE_NAME,"(",INSERT_FIELDS,") " +
            "VALUES(#{teacher.teaId}," +
            "#{teacher.teaName}," +
            "#{teacher.teaMajor}," +
            "#{teacher.teaDept})"})
    int insertTea(@Param("teacher") Teacher teacher);

    /**
     * 查询
     * @param teaId
     * @return
     */
    Teacher selectOneByTeaId(@Param("teaId")String teaId);
    Teacher selectOneByTeaName(@Param("teaName")String teaName);
    /**
     * 分页查询
     * @param offset
     * @param limit
     * @return
     */
    List<Teacher> selectTeasByOffsetAndLimit(@Param("offset")Integer offset,
                                             @Param("limit")Integer  limit);

    /**
     * 总记录数
     * @return
     */
    @Select({"SELECT COUNT(*) FROM",TABLE_NAME})
    int countTeas();

    List<Teacher> getTeaMajorList();

    List<Teacher> getTeasByMajor(@Param("teaMajor")String teaMajor);
}
