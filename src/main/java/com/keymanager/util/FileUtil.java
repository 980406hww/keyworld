package com.keymanager.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {// 判断目录是否存在
            //System.out.println("创建目录失败，目标目录已存在！");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
            destDirName = destDirName + File.separator;
        }
        if (dir.mkdirs()) {// 创建目标目录
            //System.out.println("创建目录成功！" + destDirName);
            return true;
        } else {
            //System.out.println("创建目录失败！");
            return false;
        }
    }

    public static List<String> readTxtFile(File file, String encoding){
        try {
            List<String> contents = new ArrayList<String>();
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    if(lineTxt.contains("\uFEFF")) {
                        lineTxt = lineTxt.substring(1);
                    }
                    contents.add(lineTxt.trim());
                }
                read.close();
                return contents;
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return null;
    }


    public static String readTxtFile(String fileName, String encoding) throws Exception {
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), encoding));
            try {
                String read = "";
                while ((read = bufferedReader.readLine()) != null) {
                    if(StringUtils.isNotBlank(read)) {
                        result.append(read);
                        result.append("\r\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return result.toString();
    }

    public static boolean writeTxtFile(String content, String encoding, String fileName) throws Exception {
        RandomAccessFile mm = null;
        boolean flag = false;
        FileOutputStream o = null;
        try {
            File file = new File(fileName);
            o = new FileOutputStream(file, true);
            o.write(content.getBytes(encoding));
            o.close();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mm != null) {
                mm.close();
            }
        }
        return flag;
    }

    public static void copyDir(String src, String des) {
        File file1 = new File(src);
        File[] fs = file1.listFiles();
        File file2 = new File(des);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        for (File f : fs) {
            if (f.isFile()) {
                fileCopy(f.getPath(), des + File.separator + f.getName()); //调用文件拷贝的方法
            } else if (f.isDirectory()) {
                copyDir(f.getPath(), des + File.separator + f.getName());
            }
        }
    }

    /**
     * 文件拷贝的方法
     */
    public static void fileCopy(String src, String des) {
        try {
            File afile = new File(src);
            afile.renameTo(new File(des));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean copyFile(String srcFileName, String destFileName, boolean overlay) {
        File srcFile = new File(srcFileName);
        // 判断目标文件是否存在
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePathAndName
     *            String 文件路径及名称 如c:/fqf.txt
     * @return boolean
     */
    public static void delFile(String filePathAndName) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myDelFile = new File(filePath);
            myDelFile.delete();

        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 删除文件夹
     *
     * @param folderPath
     *            String 文件夹路径及名称 如c:/fqf
     * @return boolean
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹

        } catch (Exception e) {
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path
     *            String 文件夹路径 如 c:/fqf
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
            }
        }
    }

    public static void closeOutputStream(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeWriter(Writer w) {
        try {
            if (w != null) {
                w.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  下载文件
     *
     * @param response
     * @param path
     */
    public static void download(HttpServletResponse response, String path) {
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

    /**
     * 下载压缩包文件
     * @param response
     * @param customerName
     * @param terminalType
     */
    public static void downloadZip(HttpServletResponse response, String path, String customerName, String terminalType) {
        File file = new File(path);
        //定义写出流
        ZipOutputStream zos = null;
        BufferedOutputStream bos = null;
        try {
            response.setContentType("application/zip");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(((terminalType == null ? "" : terminalType + "_")
                    + (customerName == null ? "" :customerName + "_") + Utils.formatDatetime(Utils.getCurrentTimestamp(), "yyyy.MM.dd")
                    + ".zip").getBytes("GBK"), "iso8859-1") + "\"");
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
            closeOutputStream(zos);
            closeOutputStream(bos);
        }
    }

    public static void main(String[] args) {
        try {
            //readTxtFile("D:\\workingFolder\\website\\com.vps04\\data\\common.inc.php", "UTF-8");
            copyDir("D:\\workingFolder\\template\\keywordTable", "D:\\workingFolder\\template");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getFilePath(){
        String path = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.substring(1,path.length());
        int endIndex = path.lastIndexOf("/");
        path = path.substring(0, endIndex);
        try {
            return path = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}