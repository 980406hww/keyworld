package com.keymanager.monitoring.controller.rest;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shunshikj01 on 2017/7/10.
 */
@RequestMapping(value = "/test")
public class TestController extends SpringMVCBaseController {

    TestController(){
        System.out.print("=====test===");
    }

    @RequestMapping(value = "/test1")
    public String test1(String name, Integer age, Double income, Boolean isMarried, String[] interests)
	    {
              System.out.println("简单数据类型绑定=========");
              System.out.println("名字:" + name);
               System.out.println("年龄:" + age);
         System.out.println("收入:" + income);
             System.out.println("已结婚:" + isMarried);
             System.out.println("兴趣:");
          for (String interest : interests)
                  {
                     System.out.println(interest);
                   }
               System.out.println("====================");
          return "Test";
          }


}
