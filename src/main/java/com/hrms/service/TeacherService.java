package com.hrms.service;

import com.hrms.bean.Teacher;
import com.hrms.bean.Teacher;
import com.hrms.mapper.TeacherMapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TeacherService {

        @Autowired
        private TeacherMapper teacherMapper ;

        private final static String XLS = "xls";
        private final static String XLSX = "xlsx";

        public int insertTea(Teacher teacher){return teacherMapper.insertTea(teacher);}

        public int delTeaById(String teaId){return teacherMapper.delTeaById(teaId);}

        public int updateTeaById(String teaId,Teacher teacher){ return teacherMapper.updateTeaById(teaId,teacher); }

        public Teacher selectOneByTeaId(String teaId){return teacherMapper.selectOneByTeaId(teaId);}
        public Teacher selectOneByTeaName(String teaName){return teacherMapper.selectOneByTeaName(teaName);}
        public List<Teacher> getTeasList(Integer offset,Integer limit){return teacherMapper.selectTeasByOffsetAndLimit(offset,limit);}
        public int getTeaCount(){return teacherMapper.countTeas();}

        public List<Teacher> getTeaMajorList() { return teacherMapper.getTeaMajorList(); }

        public List<Teacher> getTeasByMajor(String teaMajor) { return teacherMapper.getTeasByMajor(teaMajor); }

}
