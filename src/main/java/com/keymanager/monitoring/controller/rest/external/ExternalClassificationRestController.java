package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.Classification;
import com.keymanager.monitoring.entity.ClassificationWebsitInfo;
import com.keymanager.monitoring.service.ClassificationRestService;
import com.keymanager.monitoring.service.ClassificationWebsiteInfoRestService;
import com.keymanager.monitoring.service.VMwareService;
import com.keymanager.monitoring.vo.ClassificationSupportingDataVo;
import com.keymanager.monitoring.vo.ClassificationWebSiteInfoVO;
import com.keymanager.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj22 on 2017/12/4.
 */
@Controller
@RequestMapping(value = "/external/classification")
public class ExternalClassificationRestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ExternalClassificationRestController.class);

    @Autowired
    private ClassificationRestService classificationRestService;

    @Autowired
    private ClassificationWebsiteInfoRestService classificationWebsiteInfoRestService;

    @RequestMapping(value = "/getClassification",method = RequestMethod.POST)
    public ResponseEntity<?> getClassification(@RequestBody Map<String, Object> requestMap, HttpServletRequest request){
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        //String type = (String) requestMap.get("type");
        String group = (String) requestMap.get("group");
        try {
            if(validUser(userName, password)) {
                if(group.equals("All")){
                    Classification classification = classificationRestService.getClassification();
                    return new ResponseEntity<Object>(classification,HttpStatus.OK);
                }else{
                    Classification classification = classificationRestService.getClassificationgroupNotAll(group);
                    return new ResponseEntity<Object>(classification,HttpStatus.OK);
                }
            }
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        } catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getClassificationSupportingData",method = RequestMethod.POST)
    public ResponseEntity<?> getClassificationSupportingData(@RequestBody Map<String, Object> requestMap, HttpServletRequest request){
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        try {
            if(validUser(userName, password)) {
                ClassificationSupportingDataVo classificationSupportingDataVo = new ClassificationSupportingDataVo();
                classificationSupportingDataVo.setClassificationGroups(classificationRestService.getClassificationSupportingData());
                return new ResponseEntity<Object>(classificationSupportingDataVo, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        } catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/updateClassificationInfo",method = RequestMethod.POST)
    public ResponseEntity<?> updateClassificationInfo(@RequestBody Map<String,Object> requestMap, HttpServletRequest request){
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        int uuids = (Integer) requestMap.get("uuid");
        Long uuid = Long.valueOf(uuids);
        List<LinkedHashMap> classificationWebsitInfoLists = (List<LinkedHashMap>) requestMap.get("searchResults");
        List<ClassificationWebsitInfo> classificationWebsitInfoList = new ArrayList<ClassificationWebsitInfo>();
        for(int i = 0;i<classificationWebsitInfoLists.size();i++){
            ClassificationWebsitInfo classificationWebsitInfo = new ClassificationWebsitInfo();
            classificationWebsitInfo.setClassificationUuid(uuid);
            classificationWebsitInfo.setUrl((String) classificationWebsitInfoLists.get(i).get("url"));
            classificationWebsitInfo.setTitle((String)classificationWebsitInfoLists.get(i).get("title"));
            classificationWebsitInfo.setEmailAddress((String) classificationWebsitInfoLists.get(i).get("emailAddress"));
            classificationWebsitInfo.setTelPhone((String) classificationWebsitInfoLists.get(i).get("telPhone"));
            classificationWebsitInfo.setHref((String) classificationWebsitInfoLists.get(i).get("href"));
            classificationWebsitInfo.setHasOfficialWebsiteIndicator((Boolean) classificationWebsitInfoLists.get(i).get("hasOfficialWebsiteIndicator"));
            classificationWebsitInfo.setDesc((String) classificationWebsitInfoLists.get(i).get("desc"));
            //String order = (String) classificationWebsitInfoLists.get(i).get("order");
            //classificationWebsitInfo.setOrder(Integer.valueOf(order));
            classificationWebsitInfo.setOrder((Integer) classificationWebsitInfoLists.get(i).get("order"));
            classificationWebsitInfoList.add(classificationWebsitInfo);
        }
        try {
            if(validUser(userName,password)) {
                if(!Utils.isEmpty(classificationWebsitInfoList)){
                    classificationRestService.updateClassificationInfo(classificationWebsitInfoList,uuid);
                    return new ResponseEntity<Object>(true, HttpStatus.OK);
                }
            }
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        } catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getfetchKeywordClassificationEmail",method = RequestMethod.POST)
    public ResponseEntity<?> getfetchKeywordClassificationEmail(@RequestBody Map<String, Object> requestMap, HttpServletRequest request){
        String userName = (String) requestMap.get("userName");
        String password = (String) requestMap.get("password");
        String group = (String) requestMap.get("group");
        try {
            if(validUser(userName, password)) {
                Classification classification = classificationRestService.getClassificationgroupUuid(group);
                ClassificationWebSiteInfoVO classificationWebSiteInfoVO = classificationWebsiteInfoRestService.getfetchKeywordClassificationEmail(classification.getUuid());
                if(classificationWebSiteInfoVO != null){
                    return new ResponseEntity<Object>(classificationWebSiteInfoVO,HttpStatus.OK);
                }else {
                    return new ResponseEntity<Object>(null,HttpStatus.OK);
                }
            }
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        } catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/updateClassificationEmail" , method = RequestMethod.POST)
    public ResponseEntity<?> updateClassificationEmail(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            Integer uuid = (Integer) requestMap.get("uuid");
            String emailAddress = (String) requestMap.get("emailAddress");
            if(validUser(userName, password)) {
                classificationWebsiteInfoRestService.updateEmailByUuid(uuid, emailAddress);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }
    }
}
