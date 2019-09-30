package com.keymanager.ckadmin.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.TemplateException;
import java.io.IOException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * @ClassName ShiroTagFreeMarkerConfigurer
 * @Description 继承FreeMarkerConfigurer类, 重写afterPropertiesSet()方法；集成shiroTags标签
 * @Author lhc
 * @Date 2019/9/2 11:42
 * @Version 1.0
 */
public class ShiroTagFreeMarkerConfigurer extends FreeMarkerConfigurer {
    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        this.setTemplateLoaderPath("/WEB-INF/views/");
        this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
    }
}
