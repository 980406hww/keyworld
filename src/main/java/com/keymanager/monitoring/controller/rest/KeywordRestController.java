package com.keymanager.monitoring.controller.rest;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.Keyword;
import com.keymanager.monitoring.service.KeywordService;
import com.keymanager.monitoring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = "/keyword")
public class KeywordRestController extends SpringMVCBaseController {

	@Autowired
	private KeywordService keywordService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public ResponseEntity<?> changeStatus(@RequestBody Map<String, Object> requestMap){
		Integer id = (Integer) requestMap.get("id");
		Keyword keyword = keywordService.selectById(id);
		keyword.setStatus("上线");
		keyword.setUpdateTime(new Date());
		keywordService.insertOrUpdate(keyword);
		return new ResponseEntity<Object>(keyword, HttpStatus.OK);
	}
	
	@RequestMapping(value ="/{id}", method = RequestMethod.DELETE)
	public void deleteKeyword(@PathVariable("id") Long keywordId){
		keywordService.deleteById(keywordId);
	}
}
