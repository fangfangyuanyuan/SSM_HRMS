package com.hrms.mapper;

import com.hrms.bean.Design;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DesignMapper {

    String TABLE_NAME = "tbl_cdesign";
    String INSERT_FIELDS = " stuyear, term, c_id, t_id, s_id, d_score, a_score, r_score, total";
    String SELECT_FIELDS = "id , stuyear, term, c_id, t_id, s_id, d_score, a_score, r_score, total";

    /**
     * =================================删除============================================
     */
    @Delete({"DELETE FROM",TABLE_NAME,"WHERE id=#{id}"})
    int deleteDesiById(@Param("id") Integer id);

    /**
     * =================================更改============================================
     */
    int updateDesiById(@Param("id") Integer id,
                       @Param("design") Design design);

    /**
     * =================================新增============================================
     */
    @Insert({"INSERT INTO",TABLE_NAME, "(", INSERT_FIELDS ,") " +
            "VALUES(#{design.stuyear}, #{design.term}, #{design.c_id}, #{design.t_id}, #{design.s_id}, #{design.d_score}, #{design.a_score}, #{design.r_score}, #{design.total})" })
    int insertDesi(@Param("design") Design design);

    /**
     * =================================查询============================================
     */
    @Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME, "WHERE id=#{desiId}" })
    Design selectOneById(@Param("desiId") Integer desiId);
//    @Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME, "WHERE dept_leader=#{deptLeader}" })
//    Department selectOneByLeader(@Param("deptLeader") String deptLeader);
//    @Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME, "WHERE dept_name=#{deptName}" })
//    Department selectOneByName(@Param("deptName") String deptName);
//    @Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME})
//    List<Department> selectDeptList();

    List<Design> selectDesiByLimitAndOffset(@Param("offset") Integer offset,
                                             @Param("limit") Integer limit);

//    @Select({"SELECT COUNT(dept_id) FROM", TABLE_NAME,
//            "WHERE deptLeader = #{deptLeader} OR deptName = #{deptName}"})
//    int checkDeptsExistsByNameAndleader(@Param("deptLeader") String deptLeader,
//                                        @Param("deptName") String deptName);

    @Select({"SELECT COUNT(*) FROM",TABLE_NAME})
    int countDesi();
}
