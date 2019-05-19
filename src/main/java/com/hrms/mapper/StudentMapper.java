package com.hrms.mapper;

import com.hrms.bean.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {

    String TABLE_NAME = "student";
    String INSERT_FIELDS = "stuId, stuName, stuClass, stuMajor, stuDept";
//    String SELECT_FIELDS = "emp_id, " + INSERT_FIELDS;

    /**
     * 删除
     */
    @Delete({"DELETE FROM", TABLE_NAME, "WHERE stuId=#{stuId}"})
    int delStuById(@Param("stuId") String stuId);

    /**
     * 更新
     */
    int updateStuById(@Param("stuId")String stuId,@Param("student")Student student);

    /**
     * 插入
     */
    @Insert({"INSERT INTO", TABLE_NAME, "(",INSERT_FIELDS,") " +
            "VALUES(#{stuId}, " +
            "#{stuName}, " +
            "#{stuClass}, " +
            "#{stuMajor}, "+
            "#{stuDept}) " })
    int insertStu(Student student);

    /**
     *查询
     */
    Student selectOneByStuId(@Param("stuId") String stuId);

    /**
     * 分页查询
     * @param offset
     * @param limit
     * @return
     */
    List<Student> selectStuByOffsetAndLimit(@Param("offset")Integer offset,
                                            @Param("limit")Integer limit);
    List<Student> getStuListVaTea(@Param("curId") String curId);
    /**
     * 总记录数
     * @return
     */
    @Select({"SELECT COUNT(*) FROM",TABLE_NAME })
    int couStus();

    int countStuVaCur(@Param("curId") String curId);

    List<Student> getStuMajorList();

    List<Student> getClassByMajor(@Param("stuMajor")String stuMajor);

    List<Student> selStuByMajorAndClass(@Param("stuMajor")String stuMajor,@Param("stuClass") Integer stuClass);
}
