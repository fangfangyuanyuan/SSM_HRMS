package com.hrms.controller;

import com.hrms.bean.Design;
import com.hrms.service.DesignService;
import com.hrms.util.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.Contended;

import java.util.List;

@Controller
@RequestMapping(value = "/hrms/desi")
public class DesignController {

    @Autowired
    DesignService designService;

    @RequestMapping(value = "/getDesiList", method = RequestMethod.GET)
    public ModelAndView getDeptList(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo){
        ModelAndView mv = new ModelAndView("designPage");
        //每页显示的记录行数
        int limit = 5;
        //总记录数
        int totalItems = designService.getDesiCount();
        int temp = totalItems / limit;
        int totalPages = (totalItems % limit== 0) ? temp : temp+1;
        //每页的起始行(offset+1)数据，如第一页(offset=0，从第1(offset+1)行数据开始)
        int offset = (pageNo - 1)*limit;
        List<Design> designs = designService.getDesiList(offset, limit);

        mv.addObject("designs", designs)
                .addObject("totalItems", totalItems)
                .addObject("totalPages", totalPages)
                .addObject("curPageNo", pageNo);
        return mv;
    }

    @RequestMapping(value = "/addDesi",method =  RequestMethod.PUT)
    @ResponseBody
    public JsonMsg addDesi(Design design){
        int res = designService.addDesi(design);
        if(res != 1)
        {
           return JsonMsg.fail().addInfo("add_desi_error","添加异常");
        }
        return JsonMsg.success();
    }

    @RequestMapping(value = "/getTotalPages", method = RequestMethod.GET)
    @ResponseBody
    public JsonMsg getTotalPages(){

        //每页显示的记录行数
        int limit = 5;
        //总记录数
        int totalItems = designService.getDesiCount();
        int temp = totalItems / limit;
        int totalPages = (totalItems % limit== 0) ? temp : temp+1;

        return JsonMsg.success().addInfo("totalPages", totalPages);
    }

   @RequestMapping(value = "/delDesi/{id}", method = RequestMethod.DELETE)
   @ResponseBody
   public JsonMsg deleteDept(@PathVariable("id") Integer id){
       int res = 0;
       if (id > 0){
           res = designService.deleteDesiById(id);
       }
       if (res != 1){
           return JsonMsg.fail().addInfo("del_desi_error", "删除异常");
       }
       return JsonMsg.success();
   }


   @RequestMapping(value = "/getDesiById/{id}", method = RequestMethod.GET)
   @ResponseBody
   public JsonMsg getDesiById(@PathVariable("id") Integer id){
        Design design = null;
        if(id > 0){
            design = designService.getDesiById(id);
        }
        if(design != null){
            System.out.println(design.toString());
            return JsonMsg.success().addInfo( "design", design);
        }else{
            return JsonMsg.fail().addInfo("get_desi_error", "无课程设计信息" );
        }
   }

  @RequestMapping(value = "/updateDesiById/{id}", method = RequestMethod.PUT)
  @ResponseBody
  public JsonMsg updateDesiById(@PathVariable("id") Integer id,Design design){
        int res = 0;
        if(id > 0){
            res = designService.updateDesiById(id,design);
        }
        if(res != 1){
            return JsonMsg.fail().addInfo("update_desi_error","课程设计更新失败");
        }else{
            return JsonMsg.success();
        }
  }

}
