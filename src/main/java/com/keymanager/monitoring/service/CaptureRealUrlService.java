package com.keymanager.monitoring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.util.Utils;
import com.keymanager.monitoring.vo.BaiduUrl;
import com.keymanager.monitoring.vo.BaiduUrlElement;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CaptureRealUrlService {

    public BaiduUrl fetch(BaiduUrl baiduUrl) throws Exception {
        try{
            if(baiduUrl == null || baiduUrl.getBaiduUrlElementList() == null){
                return null;
            }

            for(BaiduUrlElement e : baiduUrl.getBaiduUrlElementList()) {
                String realUrl = fetchRealUrl(e.getBaiduUrl(), 0);
                e.setRealUrl(realUrl);
            }
            return baiduUrl;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

//    private String fetchRealUrl(String sourceUrl) throws IOException {
//        DefaultHttpClient client = new DefaultHttpClient();
//        HttpParams params = client.getParams();
//        params.setParameter(AllClientPNames.HANDLE_REDIRECTS, false);
//        HttpGet httpget = new HttpGet(sourceUrl);
//        HttpResponse response = client.execute(httpget);
//        int statusCode = response.getStatusLine().getStatusCode();
//        if (statusCode == 301 || statusCode == 302) {
//            Header[] hs = response.getHeaders("Location");
//            for (Header h : hs) {
//                return h.getValue();
//            }
//        }
//        return "";
//    }

    public String fetchRealUrl(String sourceUrl, int retryCount) throws IOException {
        System.out.println(sourceUrl);
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            params.setParameter(AllClientPNames.HANDLE_REDIRECTS, false);
            HttpGet httpget = new HttpGet(sourceUrl);
            HttpResponse response = client.execute(httpget);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 301 || statusCode == 302) {
                Header hs = response.getFirstHeader("Location");
                String tmpUrl = hs.getValue();
                if (tmpUrl.indexOf("http") == -1) {
                    return sourceUrl;
                }
                return (retryCount < 2) ? fetchRealUrl(tmpUrl, ++retryCount) : sourceUrl;
            } else {
                return sourceUrl;
            }
        }catch (Exception ex){
            return sourceUrl;
        }
    }

    public String fetchSingleRealUrl(String sourceUrl) throws IOException{
        String realUrl = fetchRealUrl(sourceUrl, 0);
        return realUrl;
    }

    public static void main(String[] args){
        CaptureRealUrlService captureRealUrlService = new CaptureRealUrlService();
        try {
//            String realUrl = captureRealUrl.fetch("{\"baiduUrlElementList\":[{\"id\":\"1\",\"baiduUrl\":\"http://www.baidu.com/link?url=HUPn1y5JDgrWUa1ug77bAJRup3AfJzi-HYDIyDScozLebF1F2LIlfv5vtDPRApwJbj34NnnuWwaNWUD04naJ_QNKRdbmGMdj2Efdh-qRq-S\"}]}");
            String realUrl = captureRealUrlService.fetchRealUrl("http://www.aihuhua.com/index.php?app=wap&mod=Index&act=index", 0);
            System.out.println(realUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        InputStream in = null;
//        OutputStream out = null;
//
//        try {
//            URL url = new URL("http://m.baidu.com/sf?pd=xinghe_recommend&actname=act_figure&word=%E4%BA%91%E8%AE%A1%E7%AE%97%E7%9B%B8%E5%85%B3%E6%8E%A8%E8%8D%90&title=%E4%BA%91%E8%AE%A1%E7%AE%97%E6%9B%B4%E5%A4%9A%E6%8E%A8%E8%8D%90&resource_id=28339&ext={%22key%22:%22\\u4e91\\u8ba1\\u7b97%22,%22resourceid%22:%2228339%22,%22pagenum%22:0}&lid=9134311420746565267&ms=1&frsrcid=28339&frorder=8&ssid=0&from=0&uid=0&pu=usm@5,sz@224_220,ta@iphone___3_537&bd_page_type=1&baiduid=0AD4613C442EF64ABD65634BCD27DA7D&tj=7tX_8_0_10_l1");
//            in = url.openStream();
//            out = System.out;
//
//            byte[] buffer = new byte[4096];
//            if(out==System.out){new String();}
//            int bytes_read;
//            while((bytes_read = in.read(buffer)) != -1){
//                out.write(buffer, 0, bytes_read);}
//        }catch (Exception e) {
//            System.err.println(e);
//            System.err.println("Usage: java GetURL <URL> [<filename>]");
//        }finally {
//            try { in.close(); out.close(); } catch (Exception e) {}
//        }
//        String []baiduUrls = {"http://m.baidu.com/from=0/bd_page_type=1/ssid=0/uid=0/pu=usm%405%2Csz%40224_220%2Cta%40iphone___3_537/baiduid=0AD4613C442EF64ABD65634BCD27DA7D/w=0_10_/t=iphone/l=1/tc?ref=www_iphone&lid=6441523816528548381&order=1&waplogo=1&fm=albk&tj=Xv_1_0_10_title&w_qd=IlPT2AEptyoA_yk66f1p-OGvATG&sec=18918&di=6301fdac61958b9b&bdenc=1&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IJBOMLikK1De8mVjte4viZQRAWyPqLTrIBYCbcYPKxBsIkkWu0WEf7AV2qBdgxmcaakjwdfm2ggCAG_1Kag2lCbOHGnV6pamgaaAjcttyQ2MoBlsxi_38ogoiax3IwAq",
//                "http://m.baidu.com/from=0/bd_page_type=1/ssid=0/uid=0/pu=usm%405%2Csz%40224_220%2Cta%40iphone___3_537/baiduid" +
//                        "=0AD4613C442EF64ABD65634BCD27DA7D/w=0_10_/t=iphone/l=1/tc?ref=www_iphone&lid=6441523816528548381&order=5&fm=alwz&tj=sam_wz_micro_video3_5_0_10_title&w_qd=IlPT2AEptyoA_yk66f1p-OGvATG&sec=18918&di=4c0e9f17171bebaf&bdenc=1&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IEBuZMC1K08SwnESzbbrgHtkfEFXfLHaCIlX5gTCcccpUgiPz3mNUj1w4buN6sF6xlDWGsqWrtbbBWMN0qRZmPMHBXTAb6_mha3pyuhY-M2Q8QjQdna38oc-wvv0Yf21cbvqYg8SqqOa0ZTTvV67Jr7r81Fl7CzKvCNfGh17jnE9uGp8-BK",
//        "http://m.baidu.com/from=0/bd_page_type=1/ssid=0/uid=0/pu=usm%405%2Csz%40224_220%2Cta%40iphone___3_537/baiduid=0AD4613C442EF64ABD65634BCD27DA7D/w=0_10_/t=iphone/l=1/tc?ref=www_iphone&lid=6441523816528548381&order=6&waplogo=1&waput=7&fm=wnor&dict=-1&tj=www_zhidao_normal_6_0_10_title&w_qd=IlPT2AEptyoA_yk66f1p-OGvATG&sec=18918&di=193592ae556d1a88&bdenc=1&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IDBqMMS6LLDivpEmixP4kHREsRC0aNWiCGkb8gTCcsxwGxX7g27Qp6so4g43",
//        "http://m.baidu.com/from=0/bd_page_type=1/ssid=0/uid=0/pu=usm%405%2Csz%40224_220%2Cta%40iphone___3_537/baiduid=0AD4613C442EF64ABD65634BCD27DA7D/w=0_10_/t=iphone/l=1/tc?ref=www_iphone&lid=6441523816528548381&order=8&fm=alop&tj=7tX_8_0_10_l1&w_qd=IlPT2AEptyoA_yk66f1p-OGvATG&sec=18918&di=9a75a2aefc61a847&bdenc=1&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IGtiTNCVUB8SxokDyqRLwJhEtRC4r2SiyC5z8w7a0jdtYcyf7QjFsjQV2qvs8dzNulijwdsids2HlSst_uc16LgCYXysq6Kmha0xPcM1DF1EsBH9O5a4Jos94ut_pbcZQa2a1hmTGruaE1pSJWkb-m5ra25l-CTL1Cff2-2bXtW-OII89Vc_6HJC_rjQQVXwvfAiLztJIL345inJnPuabIvAeVG7EQp9KShuLXNThoKDWU-5zj-Il0kFB5Ejoduj6FkpOTRLt7MowF1_4ExSuFYC4OxpVfN4NUvtpSuOhgr-iP6llak6iTjwDQ_LuByGUOIBGRfX-uv06AVhimX4yIqScpy2cObKyDdMW_qhsT82UeGbcme_Ss576KK3PuxUAMhhhOWiryZY1jZtaAoKuh58X9WILdSHQBljRO7GVQQWISiwdiBOSA1HJQM_TZmB4m5vUxpbXqZeDDxDYIPZzxIt8kkEJ46HxdbvgHzPiINXzyRIQl2n1mvrGiyDjnF7m5V7BXLDF4Rt5052KswEwPYv7spufnY5eCfTU",
//        "http://m.baidu.com/from=0/bd_page_type=1/ssid=0/uid=0/pu=usm%405%2Csz%40224_220%2Cta%40iphone___3_537/baiduid=0AD4613C442EF64ABD65634BCD27DA7D/w=0_10_/t=iphone/l=1/tc?ref=www_iphone&lid=6441523816528548381&order=9&fm=alop&tj=we_wz_zhaopinexactnew_9_0_10_title&w_qd=IlPT2AEptyoA_yk66f1p-OGvATG&sec=18918&di=ac9a43e79d4d8716&bdenc=1&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IDBqUKj-R0HSwnESzbbrgHtkfEFXcKWiTZpLPtXO0ctYYw5CuWmIzaqFOt_t6pmEqakjw-P8h6h_LL_11hgR6OQTFUTY6vfvbirwd9M1IQsFDAn9Nyq4Fss-8iN_iaa",
//        "http://m.baidu.com/from=0/bd_page_type=1/ssid=0/uid=0/pu=usm%405%2Csz%40224_220%2Cta%40iphone___3_537/baiduid" +
//                "=0AD4613C442EF64ABD65634BCD27DA7D/w=10_10_/t=iphone/l=1/tc?ref=www_iphone&lid=8980819928547455009&order=2&fm=alop&tj=we_image_2_10_10_title&w_qd=IlPT2AEptyoA_yk66f1p-OGvATG&sec=18918&di=b1dfc174638eabde&bdenc=1&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IHxeUMikK1De8mVjte4viZQRAUTLuQniOZpPPs7eRh1-RcWGcWSJszB1vafMnhjpzimjbcaWhdhLqW2B0hxZmPMXBWiZhogir8axPcM2zQ3RE1Gde5rOlt290wd0Sb3JMy17I5HCmqfbwZ-T-Z7r-i6nn_6cSCCXjWLeZsRbobiR-SUSpOA0gCYLwqGNHF8grqceqm_QP2wDR6j1z4cakQc23Y6bX2T9O_vuyGRaz6sDJZJAF6WE53EJ25HfmwKXYYDkITR8sze97Jr4HExuuZ6zOBMs2",
//        "http://m.baidu.com/from=0/bd_page_type=1/ssid=0/uid=0/pu=usm%405%2Csz%40224_220%2Cta%40iphone___3_537/baiduid=0AD4613C442EF64ABD65634BCD27DA7D/w=10_10_/t=iphone/l=1/tc?ref=www_iphone&lid=8980819928547455009&order=3&fm=altb&tj=Wc_3_10_10_title&w_qd=IlPT2AEptyoA_yk66f1p-OGvATG&sec=18918&di=4d179873bf4a4761&bdenc=1&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IEhuYNy5K1De8mVjte4viZQRAZHKgVnCFZpLRgTDLpRkYwnHM_XEo8_Asu_-loGsq7XiK-enqhaCBCBYLqM26O0LDVCFsl0qck4Ztg2UJBwFi",
//        "http://m.baidu.com/from=0/bd_page_type=1/ssid=0/uid=0/pu=usm%405%2Csz%40224_220%2Cta%40iphone___3_537/baiduid=0AD4613C442EF64ABD65634BCD27DA7D/w=10_10_/t=iphone/l=1/tc?ref=www_iphone&lid=8980819928547455009&order=5&fm=alop&waplogo=1&tj=www_normal_5_10_10_title&vit=osres&waput=7&cltj=normal_title&asres=1&title=%E4%BA%91%E8%AE%A1%E7%AE%97_%E5%9B%BE%E6%96%87_%E7%99%BE%E5%BA%A6%E6%96%87%E5%BA%93&dict=-1&w_qd=IlPT2AEptyoA_yk66f1p-OGvATG&sec=18918&di=cfd170a9b62ee350&bdenc=1&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IER3PLjkK1De8mVjte4viZQRAVDbqRzrIBZKacTfMqMdUwHSh_TAj7h23fqRpsWoa7G36s_Go",
//        };
//
//        try {
//            for(String baiduUrl : baiduUrls){
//                String content = captureRealUrl.getPageContent(baiduUrl);
//                String realUrl = captureRealUrl.extractRealUrlFromPageContent(content);
//                System.out.println(realUrl);
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
    }

    public  String extractRealUrlFromPageContent(String pageContent){
        if(!Utils.isNullOrEmpty(pageContent)){
            String realUrl = "";
            String[] contentParts = pageContent.split("noscript");
            if(contentParts.length == 3){
                contentParts[1] = contentParts[1].replace(">\n<meta http-equiv=\"refresh\" content=\"0; url=", "");
                contentParts[1] = contentParts[1].replace("\" />\n</", "");
                realUrl = contentParts[1];
                if((realUrl.indexOf("http://zhidao.baidu.com/") > -1) || (realUrl.indexOf("wenku.baidu.com/") > -1)){
                    String [] tmpRealUrls = realUrl.split(".html?");
                    if(tmpRealUrls.length == 2){
                        realUrl = tmpRealUrls[0] + ".html";
                    }
                }
                return realUrl;
            }
        }
        return "";
    }

    public  String getPageContent(String url) throws Exception {
        String HOST = "www.baidu.com";
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 设置GET请求参数，URL一定要以"http://"开头
            HttpGet getReq = new HttpGet(url);
            // 设置请求报头，模拟Chrome浏览器
            getReq.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
            getReq.addHeader("Accept-Encoding", "gzip,deflate,sdch");
            getReq.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
            getReq.addHeader("Content-Type", "text/html; charset=UTF-8");
            getReq.addHeader("Host", HOST);
            getReq.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36");
            // 发送GET请求
            CloseableHttpResponse rep = httpClient.execute(getReq);
            // 从HTTP响应中取出页面内容
            HttpEntity repEntity = rep.getEntity();
            String content = EntityUtils.toString(repEntity);

            // 关闭连接
            rep.close();
            httpClient.close();
            return content;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new Exception(ex);
        }
    }
}