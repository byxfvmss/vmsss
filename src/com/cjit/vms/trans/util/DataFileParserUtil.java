package com.cjit.vms.trans.util;

import java.io.File;

/**
 * <p>解析类工具</p>
 *
 * @author Albert Li
 *         date 2011-6-13
 * @version 1.0
 *          <p>修改记录</p>
 *          Albert Li    新建类    2011-6-13
 */
public class DataFileParserUtil {
    public static DataFileParser createDataFileParser(File file) {
        String fName = file.getName().toLowerCase();
        if(fName.endsWith(".xls")) {
            return new DataFileParserExcelImpl(file);
        } 
//        else if(fName.endsWith(".xlsx")) {
//            return new DataFileParserXlsxImpl(file);
//        } 
//        else if(fName.endsWith(".csv")) {
//            return new DataFileParserCsvImpl(file);
//        } 
        else {
            return null;
        }
    }
}
