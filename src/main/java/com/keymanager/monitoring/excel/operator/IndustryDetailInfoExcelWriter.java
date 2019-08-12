package com.keymanager.monitoring.excel.operator;

import com.keymanager.monitoring.excel.definition.IndustryDetailInfoDefinition;
import com.keymanager.monitoring.vo.IndustryDetailVO;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class IndustryDetailInfoExcelWriter {

    private final String fileName = "IndustryDetailInfo.xls";
    protected JXLExcelWriter writer;

    public IndustryDetailInfoExcelWriter() throws BiffException, IOException {
        super();
        writer = new JXLExcelWriter(getTemplateFile());
        writer.setCurrentWorkSheetWithName("industryDetailInfo");
    }

    public void saveAs(String outputFileName) throws Exception{
        writer.saveAs(getWebRootPath() + outputFileName);
    }

    public byte[] getExcelContentBytes() throws Exception {
        return writer.getExcelContentBytes();
    }

    private File getTemplateFile(){
        String SERVLET_CONTEXT_PATH = getWebRootPath();
        String path = SERVLET_CONTEXT_PATH + "/" + fileName;
        path = path.replaceAll("%20", " ");
        return new File(path);
    }

    private String getWebRootPath() {
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

    private void writeRow(int rowIndex, IndustryDetailVO view) throws ExcelWriteException {
        writer.addLabelCell(IndustryDetailInfoDefinition.IndustryName.getColumnIndex(), rowIndex, view.getIndustryName());
        writer.addLabelCell(IndustryDetailInfoDefinition.Url.getColumnIndex(), rowIndex, view.getWebsite());
        writer.addLabelCell(IndustryDetailInfoDefinition.QQ.getColumnIndex(), rowIndex, view.getQq());
        writer.addLabelCell(IndustryDetailInfoDefinition.Telephone.getColumnIndex(), rowIndex, view.getTelephone());
        writer.addLabelCell(IndustryDetailInfoDefinition.Weight.getColumnIndex(), rowIndex, view.getWeight());
        writer.addLabelCell(IndustryDetailInfoDefinition.Remark.getColumnIndex(), rowIndex, view.getRemark());
    }

    public void writeDataToExcel(List<IndustryDetailVO> views) throws ExcelWriteException {
        if(!Utils.isEmpty(views)){
            int rowIndex = 1;
            for(IndustryDetailVO view : views){
                writeRow(rowIndex++, view);
            }
        }
    }
}
