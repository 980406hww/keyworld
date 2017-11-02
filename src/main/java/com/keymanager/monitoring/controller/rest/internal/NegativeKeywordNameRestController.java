package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.NegativeKeywordNameCriteria;
import com.keymanager.monitoring.entity.NegativeKeywordName;
import com.keymanager.monitoring.entity.NegativeKeywordNamePositionInfo;
import com.keymanager.monitoring.excel.operator.NegativeKeywordNameExcelWriter;
import com.keymanager.monitoring.service.NegativeKeywordNamePositionInfoService;
import com.keymanager.monitoring.service.NegativeKeywordNameService;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by shunshikj08 on 2017/10/24.
 */
@RestController
@RequestMapping(value = "/internal/negativeKeywordName")
public class NegativeKeywordNameRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(NegativeKeywordNameRestController.class);

    @Autowired
    private NegativeKeywordNameService negativeKeywordNameService;

    @Autowired
    private NegativeKeywordNamePositionInfoService negativeKeywordNamePositionInfoService;

    @RequiresPermissions("/internal/negativeKeywordName/searchNegativeKeywordNames")
    @RequestMapping(value = "/searchNegativeKeywordNames", method = RequestMethod.GET)
    public ModelAndView searchNegativeKeywordNames(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructNegativeKeywordNameModelAndView(new NegativeKeywordNameCriteria(), currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/negativeKeywordName/searchNegativeKeywordNames")
    @RequestMapping(value = "/searchNegativeKeywordNames", method = RequestMethod.POST)
    public ModelAndView searchNegativeKeywordNamesPost(HttpServletRequest request, NegativeKeywordNameCriteria negativeKeywordNameCriteria) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == pageSize) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructNegativeKeywordNameModelAndView(negativeKeywordNameCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
    }

    private ModelAndView constructNegativeKeywordNameModelAndView(NegativeKeywordNameCriteria negativeKeywordNameCriteria, int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/negativeKeyword/list");
        Page<NegativeKeywordName> page = negativeKeywordNameService.searchNegativeKeywordNames(new Page<NegativeKeywordName>(currentPageNumber,pageSize), negativeKeywordNameCriteria);
        modelAndView.addObject("negativeKeywordNameCriteria", negativeKeywordNameCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequiresPermissions("/internal/negativeKeywordName/uploadTxtFile")
    @RequestMapping(value = "/uploadTxtFile", method = RequestMethod.POST)
    public boolean uploadTxtFile(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "group") String group, HttpServletRequest request) {
        try {
            String path = Utils.getWebRootPath() + "txtTemp";
            File targetFile = new File(path, "company.txt");
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);
            negativeKeywordNameService.insertBatchByTxtFile(targetFile, group);
            FileUtil.delFolder(path);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    @RequiresPermissions("/internal/negativeKeywordName/downloadNegativeKeywordInfo")
    @RequestMapping(value = "/downloadNegativeKeywordInfo", method = RequestMethod.POST)
    public ResponseEntity<?> downloadCustomerKeywordInfo(HttpServletRequest request,
                                                         HttpServletResponse response, NegativeKeywordNameCriteria negativeKeywordNameCriteria) {
        try {
            List<NegativeKeywordName> negativeKeywordNames = negativeKeywordNameService.findAllNegativeKeywordName(negativeKeywordNameCriteria);
            if (!Utils.isEmpty(negativeKeywordNames)) {
                // 需要将模板文件放在最外层项目文件夹下
                NegativeKeywordNameExcelWriter excelWriter = new NegativeKeywordNameExcelWriter();
                excelWriter.writeDataToExcel(negativeKeywordNames);
                String fileName = negativeKeywordNameCriteria.getGroup() + ".xls";
                fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
                byte[] buffer = excelWriter.getExcelContentBytes();
                downExcelFile(response, fileName, buffer);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(true,HttpStatus.OK);
    }

    @RequiresPermissions("/internal/negativeKeywordName/getNegativePositionInfo")
    @RequestMapping(value = "/getNegativePositionInfo/{uuid}", method = RequestMethod.GET)
    public List<NegativeKeywordNamePositionInfo> searchNegativeKeywordNames(@PathVariable("uuid") Long uuid) {
        List<NegativeKeywordNamePositionInfo> negativeKeywordNamePositionInfos = negativeKeywordNamePositionInfoService.findPositionInfos(uuid);
        return negativeKeywordNamePositionInfos;
    }

}
