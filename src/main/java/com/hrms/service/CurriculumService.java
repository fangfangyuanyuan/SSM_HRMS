package com.hrms.service;

import com.hrms.bean.Curriculum;
import com.hrms.mapper.CurriculumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CurriculumService {

    @Autowired
    CurriculumMapper curriculumMapper;

    /**
     * 插入
     */
    public int insertCur(String couId,String teaId,int curYear){return curriculumMapper.insertCur(couId,teaId,curYear);}

    public Curriculum selCurByCouAndTeaAndYear(String couId,String teaId,int curYear){return curriculumMapper.selCurByCouAndTeaAndYear(couId, teaId, curYear);}

    public int UpdateCur(String curId,Curriculum c) { return curriculumMapper.UpdateCur(curId,c);}

    public Curriculum selCurById(String curId) { return curriculumMapper.selCurById(curId);}

    public int countTeaGro(String couId, int curYear) { return curriculumMapper.countTeaGro(couId,curYear);}

    public List<Curriculum> getTeaGroListByAdmin(String couId, int curYear, int offset, int limit) { return curriculumMapper.getTeaGroListByAdmin(couId, curYear, offset, limit); }

    public List<Curriculum> selectCurNameByAdmin() { return curriculumMapper.selectCurNameByAdmin(); }

    public List<Curriculum> getTeaListByCouAndYear(int curYear, String couId) { return curriculumMapper.getTeaListByCouAndYear(curYear,couId);}

    public int delCurById(String curId) { return curriculumMapper.delCurById(curId);}

    public List<Curriculum> selCurByCouAndCurYear(String couId, int curYear) { return curriculumMapper.selCurByCouAndCurYear(couId, curYear);}

    public  List<Curriculum>selectCouNameByTea(String teaId){ return curriculumMapper.selectCouNameByTea(teaId); }
    public int countCousByTea(String teaId) {return curriculumMapper.countCousByTea(teaId);}
    public List<Curriculum> selectCouByTeaIdAndCurYear(String teaId,Integer offset,Integer limit){return curriculumMapper.selectCouByTeaIdAndCurYear(teaId, offset, limit); }
    public List<Curriculum> selectCouNameByStu(String stuId) {return curriculumMapper.selectCouNameByStu(stuId);}
    public int countCousByStu(String stuId) { return curriculumMapper.countCousByStu(stuId);}
    public List<Curriculum> selectCouOfStu(String stuId, int offset, int limit) { return curriculumMapper.selectCouOfStu(stuId,offset,limit); }

    public int countByCouId(String couId) { return curriculumMapper.countByCouId(couId);}

    public int UpdateScoTime(String couId, int curYear, String scoStart, String scoEnd) { return curriculumMapper.UpdateScoTime(couId,curYear,scoStart,scoEnd);}

    public int insertCurWithScoTime(String couId, String teaId, int curYear, String scoStart, String scoEnd) {
    return curriculumMapper.insertCurWithScoTime(couId,teaId,curYear,scoStart,scoEnd);}

    public int checkScoIsEmptyByCur(String curId) { return curriculumMapper.checkScoIsEmptyByCur(curId);}
}
