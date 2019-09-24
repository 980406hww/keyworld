package com.keymanager.ckadmin.util.excel.mapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExcelMappingSchemaBase implements Serializable {

    private static final long serialVersionUID = -6359387526579535938L;

    private List<ExcelMappingElement> mappingElements = new ArrayList<ExcelMappingElement>();

    protected void register(String fieldName, String columnIndex, int rowIndex) {
        this.mappingElements.add(new ExcelMappingElement(fieldName, columnIndex, rowIndex));
    }

    public List<ExcelMappingElement> getMappingElements() {
        return mappingElements;
    }

}
