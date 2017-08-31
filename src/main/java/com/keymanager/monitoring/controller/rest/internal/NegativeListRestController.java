package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.NegativeListCriteria;
import com.keymanager.monitoring.entity.NegativeList;
import com.keymanager.monitoring.service.NegativeListService;
import com.keymanager.util.PortTerminalTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/negativelist")
public class NegativeListRestController {

    private static Logger logger = LoggerFactory.getLogger(NegativeListRestController.class);

    @Autowired
    private NegativeListService negativeListService;

    @RequestMapping(value = "/searchNegativeLists", method = RequestMethod.GET)
    public ModelAndView searchNegativeLists(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "50") int displaysRecords, HttpServletRequest request) {
        return constructNegativeListModelAndView(request, new NegativeListCriteria(), currentPage + "", displaysRecords + "");
    }

    @RequestMapping(value = "/searchNegativeLists", method = RequestMethod.POST)
    public ModelAndView searchNegativeListsPost(HttpServletRequest request, NegativeListCriteria negativeListCriteria) {
        String currentPage = request.getParameter("currentPageHidden");
        String displaysRecords = request.getParameter("displayRerondsHidden");
        if (null == currentPage && null == currentPage) {
            currentPage = "1";
            displaysRecords = "50";
        }
        return constructNegativeListModelAndView(request, negativeListCriteria, currentPage, displaysRecords);
    }

    private ModelAndView constructNegativeListModelAndView(HttpServletRequest request, NegativeListCriteria negativeListCriteria, String currentPage, String displaysRecords) {
        ModelAndView modelAndView = new ModelAndView("/negativelist/list");
        Page<NegativeList> page = negativeListService.searchNegativeLists(new Page<NegativeList>(Integer.parseInt(currentPage), Integer.parseInt
                (displaysRecords)), negativeListCriteria);
        String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
        modelAndView.addObject("terminalType", terminalType);
        modelAndView.addObject("negativeListCriteria", negativeListCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequestMapping(value = "/addNegativeList", method = RequestMethod.POST)
    public ResponseEntity<?> addNegativeList(@RequestBody NegativeList negativeList) {
        if(negativeListService.addNegativeList(negativeList)){
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }
        return new ResponseEntity<Object>(false, HttpStatus.OK);
    }

    @RequestMapping(value = "/getNegativeList/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> getCustomer(@PathVariable("uuid")Long uuid) {
        return new ResponseEntity<Object>(negativeListService.getNegativeList(uuid), HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteNegativeList/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> delNegativeList(@PathVariable("uuid")Long uuid) {
        return new ResponseEntity<Object>(negativeListService.deleteNegativeList(uuid), HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteNegativeLists", method = RequestMethod.POST)
    public ResponseEntity<?> deleteNegativeLists(@RequestBody Map<String, Object> requestMap) {
        List<String> uuids = (List<String>) requestMap.get("uuids");
        return new ResponseEntity<Object>(negativeListService.deleteAll(uuids) , HttpStatus.OK);
    }
    

    
}
