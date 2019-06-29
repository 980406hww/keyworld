package com.keymanager.monitoring.excel.operator;

import com.keymanager.util.Utils;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author wjianwu 2019/6/28 10:35
 */
public class CustomerKeywordAndUrlCvsExportWriter {

    private final static String path = getTemplatePath();

    private static String getTemplatePath() {
        String SERVLET_CONTEXT_PATH = getWebRootPath();
        String path = SERVLET_CONTEXT_PATH + "/" + "keyword_url.csv";
        path = path.replaceAll("%20", " ");
        return path;
    }

    private static String getWebRootPath() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        java.net.URL url = classLoader.getResource("");
        String ROOT_CLASS_PATH = url.getPath() + "/";
        File rootFile = new File(ROOT_CLASS_PATH);
        String WEB_INFO_DIRECTORY_PATH = rootFile.getParent() + "/";
        File webInfoDir = new File(WEB_INFO_DIRECTORY_PATH);
        String SERVLET_CONTEXT_PATH = webInfoDir.getParent() + "/";
        return SERVLET_CONTEXT_PATH;
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
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    public static void download(HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = Utils.formatDatetime(Utils.getCurrentTimestamp(), "yyyy.MM.dd-hh.mm.ss") + "_" + file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void downloadZip(HttpServletResponse response, String customerName) {
        File file = new File(path);
        //定义写出流
        ZipOutputStream zos = null;
        BufferedOutputStream bos = null;
        try {
            response.setContentType("application/zip");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + new String((customerName + "_" + Utils.formatDatetime(Utils.getCurrentTimestamp(), "yyyy.MM.dd") + ".zip").getBytes("GBK"), "iso8859-1") + "\"");
            OutputStream stream = response.getOutputStream();
            bos = new BufferedOutputStream(stream, 64 * 1024);
            zos = new ZipOutputStream(bos);
            zos.setLevel(1);
            //为要添加的文件设置文件名称
            zos.putNextEntry(new ZipEntry(file.getName()));
            Writer w = new OutputStreamWriter(zos, StandardCharsets.UTF_8);
            //这个地方可以单条数据进行写入，也可以把读取到的文件byte[]明确的转为字符串直接写入
            w.write(FileUtils.readFileToString(file, "UTF-8"));
            w.flush();
            w.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
