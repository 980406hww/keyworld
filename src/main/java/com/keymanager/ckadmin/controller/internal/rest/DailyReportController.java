package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.KeywordCriteria;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.entity.DailyReport;
import com.keymanager.ckadmin.excel.operator.CustomerKeywordDailyReportExcelWriter;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.DailyReportService;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.util.TerminalTypeMapping;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DailyReportController
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/25 11:38
 * @Version 1.0
 */
@RequestMapping("/internal/dailyReport")
@RestController
public class DailyReportController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(DailyReportController.class);

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Resource(name = "dailyReportService2")
    private DailyReportService dailyReportService;

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @RequestMapping(value = "/downloadSingleCustomerReport2/{customerUuid}/{terminalType}", method = RequestMethod.GET)
    public ResultBean downloadSingleCustomerReport(@PathVariable("customerUuid") Long customerUuid, @PathVariable("terminalType") String terminalType,
        HttpServletResponse response) throws Exception {
        ResultBean resultBean = new ResultBean(200, "success");
        int dayOfMonth = Utils.getDayOfMonth();

        KeywordCriteria keywordCriteria = new KeywordCriteria();
        keywordCriteria.setTerminalType(terminalType);
        keywordCriteria.setCustomerUuid(customerUuid);
        List<CustomerKeyword> customerKeywords = customerKeywordService.searchCustomerKeywordsForDailyReport(keywordCriteria);
        if (!Utils.isEmpty(customerKeywords)) {
            if (dayOfMonth == 1) {
                String uuids = "" + customerUuid;
                dailyReportService.resetDailyReportExcel(terminalType, uuids);
            }

            CustomerKeywordDailyReportExcelWriter excelWriter = new CustomerKeywordDailyReportExcelWriter(terminalType, customerUuid + "", 0);
            Customer customer = customerService.selectById(customerUuid);
            excelWriter.writeDataToExcel(customerKeywords, customer.getExternalAccount(), customer.getContactPerson(), terminalType);

            try {
                String fileName = customer.getContactPerson() + Utils.formatDatetime(Utils.getCurrentTimestamp(), "yyyy.MM.dd") + ".xls";
                fileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
                // 以流的形式下载文件。
                byte[] buffer = excelWriter.getExcelContentBytes();
                // 清空response
                response.reset();
                // 设置response的Header
                //new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
                response.addHeader("Content-Length", "" + buffer.length);
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream;charset=utf-8");
                response.setCharacterEncoding("UTF-8");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
                resultBean.setCode(400);
                resultBean.setMsg(e.getMessage());
                return resultBean;
            }
        }
        return resultBean;
    }

    @RequestMapping(value = "/searchCurrentDateCompletedReports2", method = RequestMethod.GET)
    public ResultBean searchCurrentDateCompletedReports(HttpServletRequest request) throws Exception{
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            String userName = null;
            Set<String> roles = getCurrentUser().getRoles();
            if(!roles.contains("QTSpecial")) {
                userName = (String) request.getSession().getAttribute("username");
            }
            List<DailyReport> dailyReports = dailyReportService.searchCurrentDateCompletedReports(userName);
            resultBean.setData(dailyReports);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }
}
