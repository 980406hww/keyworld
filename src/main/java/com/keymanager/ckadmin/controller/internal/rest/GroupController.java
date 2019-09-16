package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.internal.SpringMVCBaseController;
import com.keymanager.ckadmin.service.GroupService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/internal/group")
public class GroupController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Resource(name = "groupService2")
    private GroupService groupService;

    /**
     * 修改全站优化组关联的操作组合
     * @param requestMap
     * @return
     */
    @RequiresPermissions("/internal/group/updateGroupOperationCombineUuid")
    @PostMapping("/updateGroupOperationCombineUuid")
    public ResultBean updateGroupOperationCombineUuid(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            Long operationCombineUuid = null;
            if (null != requestMap.get("operationCombineUuid")) {
                operationCombineUuid = Long
                    .valueOf((String) requestMap.get("operationCombineUuid"));
            }
            List<String> groupNames = (List<String>) requestMap.get("groupNames");
            String loginName = getCurrentUser().getLoginName();
            groupService
                .updateGroupOperationCombineUuid(operationCombineUuid, groupNames, loginName);
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
