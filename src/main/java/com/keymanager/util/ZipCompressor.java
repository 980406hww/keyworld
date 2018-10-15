package com.keymanager.util;

import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.service.ConfigService;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompressor {
	public static void zipMultiFile(String filepath, String zippath, boolean noTopDirectory) {
		try {
			File file = new File(filepath);// 要被压缩的文件夹
			File zipFile = new File(zippath);
			InputStream input = null;
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; ++i) {
					input = new FileInputStream(files[i]);
					if(noTopDirectory){
						zipOut.putNextEntry(new ZipEntry(files[i].getName()));
					}else {
						zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + files[i].getName()));
					}
					int temp = 0;
					while ((temp = input.read()) != -1) {
						zipOut.write(temp);
					}
					input.close();
				}
			}
			zipOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createEncryptionZip(String filepath, String zippath, String password) throws Exception {
		ArrayList<File> fileList = new ArrayList<File>();
		File file = new File(filepath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if(f.isFile()) {
					fileList.add(f);
				}
			}

			File tmpZipFile = new File(zippath);
			if(tmpZipFile.exists()){
				tmpZipFile.delete();
			}
			//创建压缩文件
			ZipFile zipFile = new ZipFile(zippath);

			if(CollectionUtils.isNotEmpty(fileList)) {
				//设置压缩文件参数
				ZipParameters parameters = new ZipParameters();
				parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
				parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
				parameters.setEncryptFiles(true);
				parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
				parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
				parameters.setPassword(password);

				//添加文件到压缩文件
				zipFile.addFiles(fileList, parameters);
			}
		}
	}


	public static void main(String[] args){
		ZipCompressor.zipMultiFile("D:/dbx/", "d:/dbx.zip", true);
	}
}
