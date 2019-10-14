package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.GroupSettingCriteria;
import com.keymanager.ckadmin.criteria.UpdateGroupSettingCriteria;
import com.keymanager.ckadmin.entity.GroupSetting;
import com.keymanager.ckadmin.entity.OperationCombine;
import com.keymanager.ckadmin.service.GroupSettingService;
import com.keymanager.ckadmin.service.OperationCombineService;
import com.keymanager.ckadmin.service.OperationTypeService;
import com.keymanager.ckadmin.util.SQLFilterUtils;
import java.util.List;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName GroupSettingController
 * @Description 分组信息设置
 * @Author lhc
 * @Date 2019/10/10 14:20
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/internal/groupsetting")
public class GroupSettingController {

    private static Logger logger = LoggerFactory.getLogger(GroupSettingController.class);

    @Resource(name = "groupSettingService2")
    private GroupSettingService groupSettingService;

    @Resource(name = "operationCombineService2")
    private OperationCombineService operationCombineService;

    @Resource(name = "operationTypeService2")
    private OperationTypeService operationTypeService;

    @RequiresPermissions("/internal/groupsetting/searchGroupSettings")
    @GetMapping("/toGroupSettings")
    public ModelAndView toGroupSettings() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("groupSettings/groupSetting");
        return mv;
    }

    @RequiresPermissions("/internal/groupsetting/searchGroupSettings")
    @PostMapping(value = "/getGroupSettings")
    public ResultBean getGroupSettings(@RequestBody GroupSettingCriteria groupSettingCriteria) {
        ResultBean resultBean = new ResultBean();
        if (SQLFilterUtils.sqlInject(groupSettingCriteria.toString())) {
            resultBean.setCode(400);
            resultBean.setMsg("查询参数错误或包含非法字符，请检查后重试！");
            return resultBean;
        }
        try {
            Page<OperationCombine> page = new Page<>(groupSettingCriteria.getPage(), groupSettingCriteria.getLimit());
            page = groupSettingService.searchGroupSettings(page, groupSettingCriteria);
            List<OperationCombine> operationCombines = page.getRecords();
            resultBean.setCode(200);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("");
            resultBean.setData(operationCombines);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/groupsetting/searchGroupSettings")
    @PostMapping("/getOperationTypes/{terminalType}")
    public ResultBean getOperationTypes(@PathVariable(name = "terminalType") String terminalType){
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            List<String> operationTypeValues = operationTypeService.getOperationTypeValuesByRole(terminalType);
            resultBean.setData(operationTypeValues);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("服务端错误");
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/groupsetting/saveGroupSetting")
    @GetMapping("/toGroupSettingAdd")
    public ModelAndView toGroupSettingAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("groupSettings/groupSettingAdd");
        return mv;
    }

    @RequiresPermissions("/internal/groupsetting/saveGroupSetting")
    @PostMapping("/saveGroupSetting2")
    public ResultBean saveGroupSetting(@RequestBody GroupSetting groupSetting) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            groupSettingService.saveGroupSetting(groupSetting);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("服务端错误");
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/groupsetting/saveGroupSetting")
    @GetMapping("/toGroupSettingUpdate")
    public ModelAndView toGroupSettingUpdate() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("groupSettings/groupSettingUpdate");
        return mv;
    }

    @RequiresPermissions("/internal/groupsetting/searchGroupSettings")
    @PostMapping(value = "/getGroupSettingByUuid/{uuid}")
    public ResultBean getGroupSettingByUuid(@PathVariable(name = "uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            GroupSetting groupSetting = groupSettingService.getGroupSettingByUuid(uuid);
            resultBean.setCode(200);
            resultBean.setMsg("");
            resultBean.setData(groupSetting);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/groupsetting/updateGroupSetting")
    @PostMapping("/updateGroupSetting2")
    public ResultBean updateGroupSetting(@RequestBody UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            groupSettingService.updateGroupSetting(updateGroupSettingCriteria);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("服务端错误");
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/groupsetting/findGroupSetting")
    @PostMapping("/getGroupSettingCount2/{operationCombineUuid}")
    public ResultBean getGroupSettingCount(@PathVariable("operationCombineUuid") long operationCombineUuid) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            int groupSettingCount = groupSettingService.getGroupSettingUuids(operationCombineUuid).size();
            resultBean.setData(groupSettingCount);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("服务端错误");
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/groupsetting/delGroupSetting")
    @PostMapping("/delGroupSetting2/{uuid}")
    public ResultBean delGroupSetting(@PathVariable("uuid") long uuid) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            groupSettingService.deleteGroupSetting(uuid);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("服务端错误");
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/groupsetting/searchGroupSettings")
    @GetMapping("/toGroupDetail")
    public ModelAndView toGroupDetail() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("groupSettings/groupDetail");
        return mv;
    }
}
