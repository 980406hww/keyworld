package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.GroupService;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/internal/group")
public class GroupController {

    private static Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Resource(name = "groupService2")
    private GroupService groupService;

    @PostMapping("/updateGroupOperationCombineUuid")
    public ResultBean updateGroupOperationCombineUuid(
        @RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            Long operationCombineUuid = null;
            if (null != requestMap.get("operationCombineUuid")) {
                operationCombineUuid = Long
                    .valueOf((String) requestMap.get("operationCombineUuid"));
            }
            String groupName = (String) requestMap.get("groupName");
            groupService.updateGroupOperationCombineUuid(operationCombineUuid, groupName);
            resultBean.setCode(200);
            resultBean.setMsg("success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("服务端错误");
            return resultBean;
        }
        return resultBean;
    }

}