package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.FriendlyLinkCriteria;
import com.keymanager.ckadmin.entity.FriendlyLink;
import com.keymanager.ckadmin.service.FriendlyLinkService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/friendlyLinks")
public class FriendlyLinkController {

    private static Logger logger = LoggerFactory.getLogger(FriendlyLinkController.class);

    @Resource(name = "friendlyLinkService2")
    private FriendlyLinkService friendlyLinkService;

    @RequiresPermissions("/internal/friendlyLink/saveFriendlyLink")
    @GetMapping("/toSaveFriendlyLink")
    public ModelAndView toSaveFriendlyLink(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("friendlyLinkList/SaveFriendlyLink");
        return mv;
    }

    @RequiresPermissions("/internal/friendlyLink/searchFriendlyLinks")
    @PostMapping("/searchFriendlyLinkLists")
    public ResultBean searchFriendlyLinkLists(@RequestBody FriendlyLinkCriteria friendlyLinkCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<FriendlyLink> page = new Page<>(friendlyLinkCriteria.getPage(), friendlyLinkCriteria.getLimit());
            page = friendlyLinkService.searchFriendlyLinkList(page, friendlyLinkCriteria);
            List<FriendlyLink> friendlyLinkList = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(friendlyLinkList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/friendlyLink/deleteFriendlyLink")
    @GetMapping("/delFriendlyLink/{uuid}")
    public ResultBean delFriendlyLink(@PathVariable Long uuid){
        ResultBean resultBean = new ResultBean();
        try{
            friendlyLinkService.delFriendlyLink(uuid);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/friendlyLink/deleteFriendlyLinks")
    @PostMapping("/delFriendlyLinks")
    public ResultBean delFriendlyLinks(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try{
            friendlyLinkService.delFriendlyLinks(requestMap);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/friendlyLink/saveFriendlyLink")
    @RequestMapping(value = "/getFriendlyLink/{uuid}", method = RequestMethod.GET)
    public ResultBean getFriendlyLink(@PathVariable Long uuid){
        ResultBean resultBean = new ResultBean();
        try{
            FriendlyLink friendlyLink = friendlyLinkService.getFriendlyLink(uuid);
            resultBean.setData(friendlyLink);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/friendlyLink/pushFriendlyLink")
    @RequestMapping(value = "/pushFriendlyLink", method = RequestMethod.POST)
    public ResultBean pushFriendlyLink(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            friendlyLinkService.pushFriendlyLink(requestMap);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/friendlyLink/saveFriendlyLinks")
    @RequestMapping(value = "/saveFriendlyLink", method = RequestMethod.POST)
    public ResultBean saveFriendlyLink(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
        ResultBean resultBean = new ResultBean();
        try{
            FriendlyLink friendlyLink = friendlyLinkService.initFriendlyLink(request);
            friendlyLinkService.saveFriendlyLink(file, friendlyLink);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/friendlyLink/saveFriendlyLink")
    @RequestMapping(value = "/updateFriendlyLink", method = RequestMethod.POST)
    public ResultBean updateFriendlyLink(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
        ResultBean resultBean = new ResultBean();
        try{
            FriendlyLink friendlyLink = friendlyLinkService.initFriendlyLink(request);
            int originalSortRank = Integer.valueOf(request.getParameter("originalSortRank"));
            friendlyLinkService.updateFriendlyLink(file, friendlyLink, originalSortRank);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

//    @RequiresPermissions("/internal/friendlyLink/saveFriendlyLink")
    @RequestMapping(value = "/searchFriendlyLinkTypeList/{websiteUuid}", method = RequestMethod.GET)
    public ResultBean  searchFriendlyLinkTypeList(@PathVariable Long websiteUuid) {
        ResultBean resultBean = new ResultBean();
        try {
            List<Map> friendlyLinkTypeList = friendlyLinkService.searchFriendlyLinkTypeList(websiteUuid);
            resultBean.setCode(200);
            resultBean.setData(friendlyLinkTypeList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

}
