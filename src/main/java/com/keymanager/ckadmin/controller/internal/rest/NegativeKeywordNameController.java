package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.NegativeKeywordNameCriteria;
import com.keymanager.ckadmin.entity.NegativeKeywordName;
import com.keymanager.ckadmin.entity.NegativeKeywordNamePositionInfo;
import com.keymanager.ckadmin.excel.operator.NegativeKeywordNameExcelWriter;
import com.keymanager.ckadmin.service.NegativeKeywordNamePositionInfoService;
import com.keymanager.ckadmin.service.NegativeKeywordNameService;
import com.keymanager.ckadmin.util.FileUtil;
import com.keymanager.ckadmin.util.Utils;
import java.io.File;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/negativeKeywords")
public class NegativeKeywordNameController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(NegativeKeywordNameController.class);

    @Resource(name = "negativeKeywordNameService2")
    private NegativeKeywordNameService negativeKeywordNameService;

    @Resource(name = "negativeKeywordNamePositionInfoService2")
    private NegativeKeywordNamePositionInfoService negativeKeywordNamePositionInfoService;

    @RequiresPermissions("/internal/negativeKeywords/toNegativeKeywords")
    @RequestMapping(value = "/toNegativeKeywords", method = RequestMethod.GET)
    public ModelAndView toSearchPositiveLists() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("negativesKeywords/negativeKeywords");
        return mv;
    }

    @RequiresPermissions("/internal/negativeKeywordName/searchNegativeKeywordNames")
    @RequestMapping(value = "/getNegativeKeywords", method = RequestMethod.POST)
    public ResultBean searchNegativeKeywordNamesPost(@RequestBody NegativeKeywordNameCriteria criteria) {
        ResultBean resultBean = new ResultBean(0, "success");
        if ("init".equals(criteria.getInit())) {
            return resultBean;
        }
        try {
            Page<NegativeKeywordName> page = negativeKeywordNameService.searchNegativeKeywordNames(criteria);
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/negativeKeywordName/searchNegativeKeywordNames")
    @RequestMapping(value = "/getNegativeGroup", method = RequestMethod.GET)
    public ResultBean getGroups() {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        try {
            resultBean.setData(negativeKeywordNameService.getGroups());
            resultBean.setMsg("success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/negativeKeywordName/searchNegativeKeywordNames")
    @RequestMapping(value = "/getNegativePositionInfo/{uuid}", method = RequestMethod.GET)
    public ResultBean getNegativePositionInfo(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            List<NegativeKeywordNamePositionInfo> list = negativeKeywordNamePositionInfoService.findPositionInfos(uuid);
            if (!list.isEmpty()) {
                resultBean.setData(list);
                resultBean.setMsg("success");
            } else {
                resultBean.setCode(300);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/negativeKeywordName/downloadNegativeKeywordInfo")
    @RequestMapping(value = "/downloadNegativeKeywordInfo", method = RequestMethod.POST)
    public ResultBean downloadCustomerKeywordInfo(HttpServletResponse response, NegativeKeywordNameCriteria negativeKeywordNameCriteria) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            List<NegativeKeywordName> negativeKeywordNames = negativeKeywordNameService.findAllNegativeKeywordName(negativeKeywordNameCriteria);
            if (!Utils.isEmpty(negativeKeywordNames)) {
                NegativeKeywordNameExcelWriter excelWriter = new NegativeKeywordNameExcelWriter();
                excelWriter.writeDataToExcel(negativeKeywordNames);
                String fileName = negativeKeywordNameCriteria.getGroup() + ".xls";
                fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
                byte[] buffer = excelWriter.getExcelContentBytes();
                downExcelFile(response, fileName, buffer);
            } else {
                resultBean.setCode(300);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/negativeKeywordName/searchNegativeKeywordNames")
    @RequestMapping(value = "/postNegativeExcel", method = RequestMethod.POST)
    public ResultBean postNegativeExcel(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "group") String group) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            String path = Utils.getWebRootPath() + "txtTemp";
            File targetFile = new File(path, "company.txt");
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);
            negativeKeywordNameService.insertBatchByTxtFile(targetFile, group);
            FileUtil.delFolder(path);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }
}
