package com.keymanager.monitoring.service;

import com.keymanager.monitoring.common.email.ComplaintsReportMailService;
import com.keymanager.monitoring.entity.TSNegativeKeyword;
import com.keymanager.monitoring.vo.ComplaintMailVO;
import java.util.List;
import javax.mail.MessagingException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintsReportService {

  private static Logger logger = LoggerFactory.getLogger(ComplaintsReportService.class);

  @Autowired
  private ComplaintsReportMailService complaintsReportMailService;

  @Autowired
  private TSNegativeKeywordService tsNegativeKeywordService;

  public void sendComplaintsReports() {
    //判断什么时候发送邮件,以及发送的内容
    List<ComplaintMailVO> PCOver2WeekstSmainKeywordVOS = tsNegativeKeywordService
        .complaintsReportContentPC2weeks();
    List<ComplaintMailVO> phoneOver2WeekstSmainKeywordVOS = tsNegativeKeywordService
        .complaintsReportContentPhone2weeks();
    List<ComplaintMailVO> PCOver3TimestSmainKeywordVOS = tsNegativeKeywordService
        .complaintsReportContentPC3times();
    List<ComplaintMailVO> phoneOver3TimestSmainKeywordVOS = tsNegativeKeywordService
        .complaintsReportContentPhone3times();

    if (CollectionUtils.isNotEmpty(PCOver2WeekstSmainKeywordVOS) || CollectionUtils.isNotEmpty(
        phoneOver2WeekstSmainKeywordVOS)) {
      try {
        complaintsReportMailService.sendComplaintsReportOver2weeks(PCOver2WeekstSmainKeywordVOS, phoneOver2WeekstSmainKeywordVOS);
        if (CollectionUtils.isNotEmpty(PCOver2WeekstSmainKeywordVOS)) {
          TSNegativeKeyword tsNegativeKeyword = new TSNegativeKeyword();
          for (ComplaintMailVO tSmainKeyWordVO : phoneOver2WeekstSmainKeywordVOS) {
            tsNegativeKeyword.setUuid(tSmainKeyWordVO.getTsNegativeKeywordUuid());
            tsNegativeKeyword.setPcEmailSentOver2Weeks(1);
            tsNegativeKeywordService.updateById(tsNegativeKeyword);
          }
        }
        if (CollectionUtils.isNotEmpty(phoneOver2WeekstSmainKeywordVOS)) {
          TSNegativeKeyword tsNegativeKeyword = new TSNegativeKeyword();
          for (ComplaintMailVO tSmainKeyWordVO : phoneOver2WeekstSmainKeywordVOS) {
            tsNegativeKeyword.setUuid(tSmainKeyWordVO.getTsNegativeKeywordUuid());
            tsNegativeKeyword.setPhoneEmailSentOver2Weeks(1);
            tsNegativeKeywordService.updateById(tsNegativeKeyword);
          }
        }
      }catch (MessagingException e){
        logger.error("构造邮件失败", e);
      } catch (Exception e) {
        logger.error("发送邮件失败", e);
      }
      }

    if (CollectionUtils.isNotEmpty(PCOver3TimestSmainKeywordVOS) || CollectionUtils.isNotEmpty( phoneOver3TimestSmainKeywordVOS)) {
      try {
        complaintsReportMailService.sendComplaintsReportOver3Times(PCOver3TimestSmainKeywordVOS,
            phoneOver3TimestSmainKeywordVOS);
//        if (CollectionUtils.isNotEmpty(PCOver3TimestSmainKeywordVOS)) {
//          TSNegativeKeyword tsNegativeKeyword = new TSNegativeKeyword();
//          for (ComplaintMailVO tSmainKeyWordVO : PCOver3TimestSmainKeywordVOS) {
//            tsNegativeKeyword.setUuid(tSmainKeyWordVO.getTsNegativeKeywordUuid());
//            tsNegativeKeyword.setPcEmailSentOver3Times(1);
//            tsNegativeKeywordService.updateById(tsNegativeKeyword);
//          }
//        }
//        if (CollectionUtils.isNotEmpty(phoneOver3TimestSmainKeywordVOS)) {
//          TSNegativeKeyword tsNegativeKeyword = new TSNegativeKeyword();
//          for (ComplaintMailVO tSmainKeyWordVO : phoneOver3TimestSmainKeywordVOS) {
//            tsNegativeKeyword.setUuid(tSmainKeyWordVO.getTsNegativeKeywordUuid());
//            tsNegativeKeyword.setPhoneEmailSentOver3Times(1);
//            tsNegativeKeywordService.updateById(tsNegativeKeyword);
//          }
//        }
      }catch (MessagingException e){
        logger.error("构造邮件失败", e);
      } catch (Exception e) {
        logger.error("发送邮件失败", e);
      }
    }
    }

}

