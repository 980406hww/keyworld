package com.keymanager.monitoring.service;

import com.keymanager.monitoring.common.email.ComplaintsReportMailService;
import com.keymanager.monitoring.common.email.MimeMailService;
import com.keymanager.monitoring.entity.TSMainKeyword;
import com.keymanager.monitoring.vo.TSmainKeyWordVO;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintsReportService {

    @Autowired
    private ComplaintsReportMailService complaintsReportMailService;

    @Autowired
    private TSMainKeywordService tsMainKeywordService;

    public void sendComplaintsReports() {
//        ResourceBundle resour = ResourceBundle.getBundle(
//            "D:\\githome\\keyword-management\\keyword-management-new\\src\\main\\resources\\application");
//        String toemail = resour.getString("complaintsreport.emailFrom");
        //判断什么时候发送邮件,以及发送的内容
        List<TSmainKeyWordVO> tSmainKeyWordVOS = tsMainKeywordService.complaintsReportContent();
        if (CollectionUtils.isNotEmpty(tSmainKeyWordVOS)) {
            complaintsReportMailService.sendComplaintsReport("刘捷", "929339036@qq.com", tSmainKeyWordVOS);
        }
    }
}

