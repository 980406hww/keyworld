package com.keymanager.ckadmin.excel.operator;


import com.keymanager.ckadmin.util.FileUtil;
import com.keymanager.ckadmin.util.Utils;
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
public class IndustryDetailInfoCsvExportWriter {

    private final static String path = getTemplatePath();

    private static String getTemplatePath() {
        String SERVLET_CONTEXT_PATH = Utils.getWebRootPath();
        String path = SERVLET_CONTEXT_PATH + "/" + "industry_detail_info.csv";
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
            bw.append("行业名称").append(",")
                .append("url").append(",")
                .append("QQ").append(",")
                .append("电话").append(",")
                .append("权重").append(",")
                .append("客户标注").append(",")
                .append("销售备注").append(",")
                .append("\r");
            // 数据
            if (dataList != null && !dataList.isEmpty()) {
                for (Map data : dataList) {
                    bw.append(String.valueOf(data.get("industryName"))).append(",")
                        .append(String.valueOf(data.get("website"))).append(",")
                        .append(data.get("qq") == null ? "" : String.valueOf(data.get("qq")).replaceAll(",", ";")).append(",")
                        .append(data.get("telephone") == null ? "" : String.valueOf(data.get("telephone")).replaceAll(",", ";")).append(",")
                        .append(String.valueOf(data.get("weight"))).append(",")
                        .append(data.get("identifyCustomer") == null ? "" : String.valueOf(data.get("identifyCustomer"))).append(",")
                        .append(data.get("remark") == null ? "" : String.valueOf(data.get("remark")).replaceAll(",", ";")).append(",")
                        .append("\r");
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

    public static void downloadZip(HttpServletResponse response) {
        FileUtil.downloadZip(response, path, null, null);
    }
}
