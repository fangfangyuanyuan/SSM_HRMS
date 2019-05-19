package com.hrms.service;

import com.hrms.bean.Course;
import com.hrms.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CourseService {


    @Autowired
    CourseMapper courseMapper;



    public int deleteCouByCouId(String couId){return courseMapper.deleteByCouId(couId);}

    public int updateCouByCouId(String couId, Course course){ return courseMapper.updateCouByCouId(couId, course);}

    public int addCou(Course course){ return courseMapper.insertCou(course); }

    public Course getCouByCouId(String couId){ return courseMapper.selectCouByCouId(couId); }
    public Course selectCouByCouName(String couName){return courseMapper.selectCouByCouName(couName);}
    public List<Course> getCouList(Integer offset,Integer limit){ return courseMapper.selectCouByLimitAndOffset(offset, limit); }
    public List<Course> selectCouNameByAdmin() { return courseMapper.selectCouNameByAdmin();}
    public int countCous(){return courseMapper.countCous();}
}
