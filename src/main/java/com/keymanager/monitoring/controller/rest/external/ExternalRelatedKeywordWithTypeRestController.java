package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.RelatedKeywordWithTypeCriteria;
import com.keymanager.monitoring.entity.RelatedKeyWordWithType;
import com.keymanager.monitoring.service.RelatedKeywordWithTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/external/RelatedKeywordWithType")
public class ExternalRelatedKeywordWithTypeRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalRelatedKeywordWithTypeRestController.class);

    @Autowired
    private RelatedKeywordWithTypeService relatedKeywordWithTypeService;

    @RequestMapping(value = "/findRelatedKeywordWithType", method = RequestMethod.POST)
    public ResponseEntity<?> findRelatedKeywordWithType(@RequestBody RelatedKeywordWithTypeCriteria relatedKeywordWithTypeCriteria){
        try {
            if (validUser(relatedKeywordWithTypeCriteria.getUserName(), relatedKeywordWithTypeCriteria.getPassword())){
                List<RelatedKeyWordWithType> relatedKeywords = relatedKeywordWithTypeService.findRelatedKeywordWithType(relatedKeywordWithTypeCriteria.getMainKeyword());
                return new ResponseEntity<Object>(relatedKeywords, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/saveRelatedKeywordWithType", method = RequestMethod.POST)
    public ResponseEntity<?> saveRelatedKeywordWithType(@RequestBody RelatedKeywordWithTypeCriteria relatedKeywordWithTypeCriteria){
        try {
            if(validUser(relatedKeywordWithTypeCriteria.getUserName(), relatedKeywordWithTypeCriteria.getPassword())){
                relatedKeywordWithTypeService.saveRelatedKeywordWithType(relatedKeywordWithTypeCriteria);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/deleteRelatedKeywordWithType", method = RequestMethod.POST)
    public ResponseEntity<?> deleteRelatedKeywordWithType(@RequestBody RelatedKeywordWithTypeCriteria relatedKeywordWithTypeCriteria){
        try {
            if (validUser(relatedKeywordWithTypeCriteria.getUserName(), relatedKeywordWithTypeCriteria.getPassword())){
                relatedKeywordWithTypeService.deleteRelatedKeywordWithType(relatedKeywordWithTypeCriteria.getMainKeyword(),relatedKeywordWithTypeCriteria.getRelatedKeyword());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
