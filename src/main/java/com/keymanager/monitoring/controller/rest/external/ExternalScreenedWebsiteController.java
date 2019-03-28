package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.rest.internal.ScreenedWebsiteListCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/external/screenedWebsite")
public class ExternalScreenedWebsiteController {

    private static Logger logger = LoggerFactory.getLogger(ExternalScreenedWebsiteController.class);

    @Autowired
    private ScreenedWebsiteListCacheService screenedWebsiteListCacheService;

    @RequestMapping(value = "/evictScreenedWebsiteCache", method = RequestMethod.POST)
    public ResponseEntity<?> evictScreenedWebsiteCache(@RequestBody String optimizeGroupName) {
        try {
            screenedWebsiteListCacheService.screenedWebsiteListCacheEvict(optimizeGroupName);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/evictAllScreenedWebsiteListCache", method = RequestMethod.POST)
    public ResponseEntity<?> evictAllScreenedWebsiteListCache() {
        try {
            screenedWebsiteListCacheService.evictAllScreenedWebsiteListCache();
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
