package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.UpdateKeywordBearsPawNumberDao;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.util.common.StringUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Decription 补充和更新熊掌号
 * @Author: rwxian
 * @Date 2019/9/27 15:10
 */
@Service
public class UpdateKeywordBearsPawNumberService extends ServiceImpl<UpdateKeywordBearsPawNumberDao, CustomerKeyword> {
    @Autowired
    private UpdateKeywordBearsPawNumberDao updateKeywordBearsPawNumberDao;

    /**
     * 获取所有去重后的url和对应的关键字
     * @return
     */
    public ConcurrentHashMap getDistinctUrl() {
        List<CustomerKeyword> distinctUrl = updateKeywordBearsPawNumberDao.getCustomerKeywordDistinctUrl();
        ConcurrentHashMap<String, String> urlMap = new ConcurrentHashMap<>();
        for (CustomerKeyword ck : distinctUrl) {
            try {
                String dUrl = ck.getUrl();
                if (StringUtil.isNotNullNorEmpty(dUrl)) {       // url为空，不处理
                    if (stringIsUrl(dUrl)) {                    // 判断是否为正常的url
                        if (!dUrl.contains("http")) {
                            dUrl = "http://" + dUrl;            // 容错URL对象
                        }
                        URL url = new URL(dUrl);
                        String hostName = url.getHost();
                        if (hostName.contains("www.")) {        // 获取二级域名
                            int beginIndex = "www.".length();
                            hostName = hostName.substring(beginIndex);
                        }
                        urlMap.put(hostName, ck.getKeyword());         // 利用map集合二次去重
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return urlMap;
    }

    /**
     * 判断字符串是否为域名
     * @param str
     * @return
     */
    private boolean stringIsUrl(String str) {
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                // + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";

        Pattern pat = Pattern.compile(regex.trim());//比对
        Matcher mat = pat.matcher(str.trim());
        return mat.matches();
    }

    public boolean updateBearPaw(String url, String bearPaw) {
        int res = updateKeywordBearsPawNumberDao.updateBearPawByUrl(url, bearPaw);
        return res > 0;
    }
}
