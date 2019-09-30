package com.keymanager.ckadmin.excel.operator;

import com.keymanager.ckadmin.util.Utils;
import com.keymanager.ckadmin.util.FileUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wjianwu 2019/6/28 10:35
 */
public class CustomerKeywordAndUrlCvsExportWriter {

    private final static String path = getTemplatePath();

    private static String getTemplatePath() {
        String SERVLET_CONTEXT_PATH = Utils.getWebRootPath();
        String path = SERVLET_CONTEXT_PATH + "/" + "keyword_url.csv";
        path = path.replaceAll("%20", " ");
        return path;
    }

    public static boolean exportCsv(List<Map> dataList) {
        File file = new File(path);
        boolean isSuccess;

        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            bw = new BufferedWriter(osw);
            bw.write(0xFEFF);
            // 标题
            bw.append("关键字").append(",").append("url").append("\r");
            // 数据
            if (dataList != null && !dataList.isEmpty()) {
                for (Map data : dataList) {
                    bw.append(String.valueOf(data.get("keyword"))).append(",").append(String.valueOf(data.get("url"))).append("\r");
                }
            }
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
        } finally {
            FileUtil.closeWriter(bw);
            FileUtil.closeWriter(osw);
            FileUtil.closeOutputStream(out);
        }
        return isSuccess;
    }

    public static void download(HttpServletResponse response) {
        FileUtil.download(response, path);
    }

    public static void downloadZip(HttpServletResponse response, String customerName, String terminalType) {
        FileUtil.downloadZip(response, path, customerName, terminalType);
    }
}
