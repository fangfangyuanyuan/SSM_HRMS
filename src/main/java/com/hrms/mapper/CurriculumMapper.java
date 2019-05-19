package com.hrms.mapper;

import com.hrms.bean.Curriculum;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface CurriculumMapper {

    String TABLE_NAME = "curriculum ";
    String INSERT_CURRICULUM_FIELDS = "couId, teaId, curYear";
    String INSERT_CURRICULUMWithScoTime_FIELDS=INSERT_CURRICULUM_FIELDS + ",scoStart, scoEnd";
    @Insert({ "INSERT INTO", TABLE_NAME, "(", INSERT_CURRICULUM_FIELDS,")",
            "VALUES(#{couId}," +
                    "#{teaId}," +
                    "#{curYear})"})
    int insertCur(@Param("couId") String couId,@Param("teaId") String teaId,@Param("curYear") int curYear);

    int UpdateCur(@Param("curId")String curId,@Param("curriculum") Curriculum curriculum);


    @Delete({"DELETE FROM",TABLE_NAME,"WHERE curId=#{curId}"})
    int delCurById(@Param("curId") String curId);

    Curriculum selCurByCouAndTeaAndYear(@Param("couId") String couId,@Param("teaId") String teaId,@Param("curYear") int curYear);
    Curriculum selCurById(@Param("curId")String curId);

    @Select({"SELECT COUNT(*) FROM",TABLE_NAME,"WHERE couId=#{couId} AND curYear=#{curYear}"})
    int countTeaGro(@Param("couId")String couId, @Param("curYear")int curYear);
    int countCousByStu(@Param("stuId") String stuId);
    int countCousByTea(@Param("teaId") String teaId);
    List<Curriculum> getTeaGroListByAdmin(@Param("couId")String couId,@Param("curYear") int curYear,@Param("offset") int offset,@Param("limit") int limit);
    List<Curriculum> selectCurNameByAdmin();
    List<Curriculum> getTeaListByCouAndYear(@Param("curYear") int curYear,@Param("couId")String couId);
    List<Curriculum> selCurByCouAndCurYear(@Param("couId")String couId,@Param("curYear") int curYear);
    List<Curriculum> selectCouNameByTea(@Param("teaId") String teaId);
    List<Curriculum> selectCouByTeaIdAndCurYear(@Param("teaId") String teaId, @Param("offset")Integer offset, @Param("limit")Integer limit);
    List<Curriculum> selectCouNameByStu(@Param("stuId") String stuId);
    List<Curriculum> selectCouOfStu(@Param("stuId") String stuId, @Param("offset")Integer offset, @Param("limit")Integer limit);

    @Select({"SELECT COUNT(*) FROM",TABLE_NAME,"WHERE couId=#{couId}"})
    int countByCouId(@Param("couId") String couId);

    int UpdateScoTime(@Param("couId")String couId,@Param("curYear") int curYear, @Param("scoStart")String scoStart,@Param("scoEnd") String scoEnd);

    @Insert({ "INSERT INTO", TABLE_NAME, "(", INSERT_CURRICULUMWithScoTime_FIELDS,")",
            "VALUES(#{couId}," +
                    "#{teaId}," +
                    "#{curYear}," +
                    "#{scoStart}," +
                    "#{scoEnd})"})
    int insertCurWithScoTime(@Param("couId")String couId,@Param("teaId")String teaId, @Param("curYear")int curYear,@Param("scoStart") String scoStart,@Param("scoEnd") String scoEnd);

    @Select({"SELECT COUNT(*) FROM",TABLE_NAME,"WHERE scoStatus=1 AND curId=#{curId}"})
    int checkScoIsEmptyByCur(@Param("curId")String curId);
}
