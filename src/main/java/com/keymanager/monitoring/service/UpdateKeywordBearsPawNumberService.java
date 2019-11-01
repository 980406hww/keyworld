package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.UpdateKeywordBearsPawNumberDao;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.util.common.StringUtil;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
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

    private static final ConcurrentHashMap<String, String> CUSTOMER_KEYWORD_DOMAIN_MAP = new ConcurrentHashMap<>();

    /**
     * 缓存去重后的关键词域名，用于更新百度关键词的熊掌号
     * @throws Exception
     */
    public void cacheCustomerKeywordDomainMap() throws Exception {
        CUSTOMER_KEYWORD_DOMAIN_MAP.clear();
        List<CustomerKeyword> distinctUrl = updateKeywordBearsPawNumberDao.getCustomerKeywordDistinctUrl();
        for (CustomerKeyword ck : distinctUrl) {
            String dUrl = ck.getUrl();
            // url为空的不处理
            if (StringUtil.isNotNullNorEmpty(dUrl)) {
                // 判断是否为正常的url
                if (stringIsUrl(dUrl)) {
                    if (!dUrl.contains("http")) {
                        dUrl = "http://" + dUrl;
                    }
                    URL url = new URL(dUrl);
                    String hostName = url.getHost();
                    // 获取二级域名
                    if (hostName.contains("www.")) {
                        int beginIndex = "www.".length();
                        hostName = hostName.substring(beginIndex);
                    }
                    // 利用map集合二次去重
                    CUSTOMER_KEYWORD_DOMAIN_MAP.put(hostName, ck.getKeyword());
                }
            }
        }
    }

    /**
     * 获取所有去重后的url和对应的关键字
     * @return
     */
    public ConcurrentHashMap getCustomerKeywordDomains() {
        ConcurrentHashMap<String, String> customerKeywordMap = null;
        if (!CUSTOMER_KEYWORD_DOMAIN_MAP.isEmpty()) {
            customerKeywordMap = new ConcurrentHashMap<>(20);
            do {
                Entry<String, String> next = CUSTOMER_KEYWORD_DOMAIN_MAP.entrySet().iterator().next();
                customerKeywordMap.put(next.getKey(), next.getValue());
                CUSTOMER_KEYWORD_DOMAIN_MAP.remove(next.getKey());
            } while (customerKeywordMap.size() < 20);
        }
        return customerKeywordMap;
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
        // 比对
        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(str.trim());
        return mat.matches();
    }

    public void updateBearPaw(String url, String bearPaw) {
        updateKeywordBearsPawNumberDao.updateBearPawByUrl(url, bearPaw);
    }
}
