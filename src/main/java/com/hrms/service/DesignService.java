package com.hrms.service;

import com.hrms.bean.Design;
import com.hrms.mapper.DesignMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignService {

    @Autowired
    DesignMapper designMapper;

    public List<Design> getDesiList(Integer offset,Integer limit){ return designMapper.selectDesiByLimitAndOffset(offset,limit);}
    public int getDesiCount(){ return designMapper.countDesi();}
    public int addDesi(Design design){return designMapper.insertDesi(design);}
    public int deleteDesiById(Integer id){return designMapper.deleteDesiById(id);}
    public Design  getDesiById(Integer id){return designMapper.selectOneById(id);}
    public int updateDesiById(Integer id,Design design) {return designMapper.updateDesiById(id,design);}
}
