package com.cjit.vms.metlife.action;

import cjit.crms.util.ExcelIOUtil;
import com.cjit.common.util.JXLTool;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.vms.metlife.model.TransDataInfo;
import com.cjit.vms.metlife.service.ImpDataMetlifeService;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.service.CustomerService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.VmsTransInfo;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.service.config.TransTypeService;
import com.cjit.vms.trans.util.DataUtil;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Abel on 2016/4/7.
 */
public class ImpDataMetlifeAction extends DataDealAction {

    private ImpDataMetlifeService impDataMetlifeService;
    private TransTypeService transTypeService;
    private UserInterfaceConfigService userInterfaceConfigService;
    private OrganizationService organizationService;
    private TransDataInfo dataInfo;
    private File attachment;
    private String attachmentFileName;
    private String batchId;
    private List<TransDataInfo> batchIdList = new ArrayList<TransDataInfo>();
    private String status;
    private String mStatus;
    private CustomerService customerService;

    public String findTransDataInfoList() {
        try {
            if (dataInfo == null) {
                dataInfo = new TransDataInfo();
            }
            if (status == null || "".equals(status) || !DataUtil.BATCH_STATUS_3.equals(status)) {
                dataInfo.setStatus("<> " + DataUtil.BATCH_STATUS_3);
            } else {
                dataInfo.setStatus("= " + DataUtil.BATCH_STATUS_3);
            }
            impDataMetlifeService.findTransDataInfoList(dataInfo, paginationList);
            dataInfo.setStatus(dataInfo.getmStatus());
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String findTransDataInfoListByBatchId() {
        try {
            if (dataInfo == null) {
                dataInfo = new TransDataInfo();
            }
            dataInfo.setBatchId(batchId);
            dataInfo.setStatus(status);
            impDataMetlifeService.findTransDataInfoListByBatchId(dataInfo, paginationList);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            this.setRESULT_MESSAGE("数据异常,请稍后重试");
            return ERROR;
        }

    }

    public String checkMetLifeData() {
        try {
            String[] a = batchId.split(",");
            List msgList = new ArrayList();
            for (int i = 0; i < a.length; i++) {
                String batchId = a[i];
                TransDataInfo info = new TransDataInfo();
                info.setBatchId(batchId);
                List impDataList = impDataMetlifeService.findTransDataInfoListByBatchId(info, null);
                //获取明细
                for (int j = 0; j < impDataList.size(); j++) {
                    StringBuilder str = new StringBuilder();
                    TransDataInfo resultInfo = (TransDataInfo) impDataList.get(j);
                    resultInfo.setdStatus("");
                    String dataId = resultInfo.getDataId();
                    String transId = resultInfo.getTransId();
                    //transId重复性校验
                    //表内重复性校验
                    TransDataInfo transDataInfo = new TransDataInfo();
                    //获取交易信息
                    transDataInfo.setTransId(transId);

                    //相关性及非空校验
                    transDataInfo.setDataId(dataId);
                    List resultList = impDataMetlifeService.findTransDataInfoListByBatchId(transDataInfo, null);
                    TransDataInfo checkResult = (TransDataInfo) resultList.get(0);
                    //客户号
                    String customerId = checkResult.getCowNnum();
                    if (checkNull(customerId)) {
                        str.append("客户号为空,");
                        resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                    } else {
                        Customer c = customerService.findCustomer(customerId);
                        if (c == null) {
                            str.append("未找到客户号相关客户,");
                            resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                        } else {
                            resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                        }
                    }

                    if (checkNull(resultInfo.getProDuctCode())) {
                        str.append("产品代码为空,");
                        resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                    } else {
                        resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                    }

                    //交易日期
                    String transDate = checkResult.getTrdt();
                    if (checkNull(transDate)) {
                        str.append("交易日期为空,");
                        resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                    } else {
                        String[] dataSplit = transDate.split("-");
                        if (null != transDate && !"".equals(transDate) && dataSplit.length == 3 &&
                                dataSplit[1].length() == dataSplit[2].length() && transDate.length() == 10) {
                            resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                        } else {
                            str.append("交易日期格式不正确,");
                            resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                        }
                    }
                    //交易金额
                    String transAmt = "";
                    if(checkResult.getAccTamt() !=null){
                        transAmt = checkResult.getAccTamt().toString();
                    }
                    if (checkNull(transAmt)) {
                        str.append("交易金额为空,");
                        resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                    } else {
                        resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                    }
                    //税率
                    String taxRate = "";
                    if(checkResult.getTaxRate() != null){
                        taxRate = checkResult.getTaxRate();
                    }
                    if (checkNull(taxRate)) {
                        str.append("税率为空,");
                        resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                    } else {
                        resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                    }
                    //收入会计科目
                    if (checkNull(checkResult.getAlTref())) {
                        str.append("收入会计科目为空,");
                        resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                    } else {
                        resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                    }
                    resultInfo.setdStatus(resultInfo.getdStatus() + "0,");

                    //T2
                    if (checkNull(checkResult.getT2())) {
                        str.append("T2为空,");
                        resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                    } else {
                        resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                    }
                    resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                    resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                    //t5
                    if (checkNull(checkResult.getT5())) {
                        str.append("T5为空,");
                        resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                    } else {
                        resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                    }
                    //t6
                    if (checkNull(checkResult.getT6())) {
                        str.append("T6为空,");
                        resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                    } else {
                        resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                    }
                    //t7
                    if (checkNull(checkResult.getT7())) {
                        str.append("T7为空,");
                        resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                    } else {
                        resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                    }
                    resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                    resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                    //交易机构
                    if (checkNull(checkResult.getT10())) {
                        str.append("T10为空,");
                        resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                    } else {
                        List instList = impDataMetlifeService.findBaseInst(resultInfo.getT10());
                        if (instList.size() != 1) {
                            str.append("未找到T10所属交易机构,");
                            resultInfo.setdStatus(resultInfo.getdStatus() + "1,");
                        } else {
                            resultInfo.setdStatus(resultInfo.getdStatus() + "0,");
                        }
                    }
                    TransDataInfo vi = new TransDataInfo();
                    String sTr = str.toString();
                    vi.setMessage(sTr != null && !"".equals(sTr) ? sTr.substring(0, sTr.length() - 1) : "");
                    String statuStr = resultInfo.getdStatus().substring(0, resultInfo.getdStatus().length() - 1);
                    vi.setDataId(resultInfo.getDataId());
                    vi.setdStatus(statuStr);
                    msgList.add(vi);
                }


                impDataMetlifeService.updateTransDataInfoMessage(msgList);
                impDataMetlifeService.updateTransDataInfoList(impDataList);

                long length = 0;
                //更新校验状态
                for (int j = 0; j < impDataList.size(); j++) {
                    TransDataInfo resultInfo = (TransDataInfo) impDataList.get(j);
                    String checkStatus = resultInfo.getdStatus();
                    dataInfo = new TransDataInfo();
                    dataInfo.setBatchId(resultInfo.getBatchId());
                    dataInfo.setDataId(resultInfo.getDataId());
                    length += Long.parseLong(checkStatus.replace(",", ""));
                    if (Long.parseLong(checkStatus.replace(",", "")) != 0) {
                        dataInfo.setStatus(DataUtil.DATA_STATUS_1);
                        impDataMetlifeService.updateTransDataInfo(dataInfo);
                    } else {
                        dataInfo.setStatus(DataUtil.DATA_STATUS_2);
                        impDataMetlifeService.updateTransDataInfo(dataInfo);
                    }
                }
                if (length == 0) {
                    dataInfo.setStatus(DataUtil.DATA_STATUS_2);
                } else {
                    dataInfo.setStatus(DataUtil.DATA_STATUS_1);
                }
                impDataMetlifeService.updateTransBatchInfo(dataInfo);
            }
            this.setRESULT_MESSAGE("校验成功");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            this.setRESULT_MESSAGE("数据异常,请稍后重试");
            return ERROR;
        }
    }

    public String deleteTransBatchDataInfo() {
        try {
            String[] BatchId = batchId.split(",");
            impDataMetlifeService.deleteTransBatchInfoByBatchId(BatchId);
            impDataMetlifeService.deleteTransDataInfoByBatchId(BatchId,null);
            this.setRESULT_MESSAGE("删除成功");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            this.setRESULT_MESSAGE("删除失败,数据异常,请稍后重试");
            return ERROR;
        }
    }

    //提交审核
    public String upTransBatchInfo() {
        try {
            if (dataInfo == null) {
                dataInfo = new TransDataInfo();
            }
            String[] batchIdStr = batchId.split(",");
            for (int i = 0; i < batchIdStr.length; i++) {
                dataInfo.setBatchId(batchIdStr[i]);
                dataInfo.setStatus(DataUtil.BATCH_STATUS_3);
                dataInfo.setFlag(DataUtil.BATCH_STATUS_3);
                impDataMetlifeService.updateTransBatchInfo(dataInfo);
            }
            this.setRESULT_MESSAGE("提交成功,已移至审核");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            this.setRESULT_MESSAGE("提交失败,数据异常,请稍后重试");
            return ERROR;
        }
    }

    /**
     * 审核通过
     *
     * @return
     */
    public String AuditPassTransBatchDataInfo() {
        try {
            if (dataInfo == null) {
                dataInfo = new TransDataInfo();
            }
            String[] batch = batchId.split(",");
            String batchIds = "";
            String batchIdData = "";
            for (int i = 0; i < batch.length; i++) {
                String[] unPassCount = batch[i].split("-");
                dataInfo.setBatchId(unPassCount[0]);
                impDataMetlifeService.saveTransDataToTransInfo(dataInfo);
                batchIdData += unPassCount[0] + ",";
                impDataMetlifeService.findTransDataTransIdByBatchId(unPassCount[0]);
                impDataMetlifeService.deleteTransDataInfoByBatchId(batchIdData.split(","),"flag");
                if(unPassCount[1]!=null&&Integer.parseInt(unPassCount[1])==0){
                    batchIds  += unPassCount[0] +",";
                    impDataMetlifeService.deleteTransBatchInfoByBatchId(batchIds.split(","));
                }else{
                    dataInfo = new TransDataInfo();
                    dataInfo.setStatus(DataUtil.DATA_STATUS_1);
                    dataInfo.setBatchId(unPassCount[0]);
                    impDataMetlifeService.updateTransBatchInfo(dataInfo);
                }
            }
            this.setRESULT_MESSAGE("审核通过成功");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            this.setRESULT_MESSAGE("审核通过失败,数据异常,请稍后重试");
            return ERROR;
        }
    }

    /**
     * 审核拒绝
     */
    public String AuditNoTransBatchDataInfo() {
        try {
            if (dataInfo == null) {
                dataInfo = new TransDataInfo();
            }
            String[] batch = batchId.split(",");
            for (int i = 0; i < batch.length; i++) {
                dataInfo.setBatchId(batch[i]);
                dataInfo.setStatus(DataUtil.BATCH_STATUS_6);
                impDataMetlifeService.updateTransBatchInfo(dataInfo);
            }
            this.setRESULT_MESSAGE("审核拒绝成功");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            this.setRESULT_MESSAGE("审核拒绝失败,数据异常,请稍后重试");
            return ERROR;
        }
    }


    public String upLoadTransDataList() {
        try {
            ExcelIOUtil excelIOUtil = new ExcelIOUtil();
            Map map = excelIOUtil.checkFile(attachment, attachmentFileName);
            boolean flag = (Boolean) map.get("flag");
            File file = (File) map.get("file");
            if (flag) {
                List<Dictionary> headList = userInterfaceConfigService.getDictionarys1("TRANS_INFO_IMP_M", "", "");
                List<Map<String, String>> dataList = excelIOUtil.doImportFile(file, headList);
                for (int i = 0; i < dataList.size(); i++) {
                    Map rowMap = new HashMap();
                    rowMap = dataList.get(i);
                    dealRowMap(rowMap);
                }
                //获取批次主健
                String batchId = dataList.get(0).get("batchId");
                //获取当前用户
                String impUser = this.getCurrentUser().getUsername();
                //获取当前机构
                String impInst = this.getCurrentUser().getOrgId();
                //获取时间
                TransDataInfo dataInfo = new TransDataInfo();
                dataInfo.setBatchId(batchId);
                dataInfo.setImpUser(impUser);
                dataInfo.setStatus("0");//未校验
                impDataMetlifeService.saveTransBatchInfoList(dataInfo);
                impDataMetlifeService.saveTransDataInfoList(dataList);
                return SUCCESS;
            } else {
                this.setRESULT_MESSAGE((String) map.get("resultMessages"));
                return ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.setRESULT_MESSAGE("导入数据异常,请稍后重试");
            return ERROR;
        }
    }

    /**
     * 列数据处理
     *
     * @param rowMap
     * @return
     */
    public void dealRowMap(Map rowMap) {
        //是否手工开票
        String transFapiaoFlag = (String) rowMap.get("transFapiaoFlag");
        if (checkNull(transFapiaoFlag)) {
            rowMap.put("transFapiaoFlag", "M");
        }
        //是否冲账
        String isReverse = (String) rowMap.get("isReverse");
        if (checkNull(isReverse)) {
            rowMap.put("isReverse", "N");
        }
        String taxFlag = (String) rowMap.get("taxFlag");
        //含税交易
        if (null == taxFlag || "".equals(taxFlag) || "Y".equals(taxFlag)) {
            rowMap.put("taxFlag", "Y");
            //补充税率(交易认定)
            String transTypeId = (String) rowMap.get("transType");//交易ID
            String t10 = (String) rowMap.get("t10");//交易机构
            Organization org = new Organization();
            org.setId(t10);
            //获取税号
            org = organizationService.getOrganization(org);
            String taxNo = "";
            if (org != null) {
                taxNo = org.getTaxperNumber();
            }
            VerificationInfo transType = new VerificationInfo();
            transType.setTransTypeId(transTypeId);
            transType.setTaxNo(taxNo);
            List list = transTypeService.findTaxRateBytransType(transType);
            if (list.size() > 0) {
                transType = (VerificationInfo) list.get(0);
                String taxRate = transType.getTaxRate();
                rowMap.put("taxRate", taxRate);
                //价税分离
                String transAmt = (String) rowMap.get("accTamt");//价税合计
                BigDecimal transAmtBd = new BigDecimal(transAmt);
                BigDecimal taxRateBd = new BigDecimal(taxRate);
                BigDecimal taxAmtF = transAmtBd.multiply(taxRateBd);
                BigDecimal taxAmtS = taxAmtF.divide((BigDecimal.ONE.add(taxRateBd)), 10, BigDecimal.ROUND_HALF_DOWN);
                BigDecimal taxAmtBd = taxAmtF.divide((BigDecimal.ONE.add(taxRateBd)), 2, BigDecimal.ROUND_HALF_DOWN);
                BigDecimal shortAndOverBd = taxAmtS.subtract(taxAmtBd);
                BigDecimal incomeBd = transAmtBd.subtract(taxAmtBd);
                String taxAmt = taxAmtBd.toString();//税额
                String shortAndOver = shortAndOverBd.toString();//尾差
                String income = incomeBd.toString();
                rowMap.put("taxAmt", taxAmt);
                rowMap.put("shortAndOver", shortAndOver);
                rowMap.put("income", income);//收入
            }
            //不含税交易
        } else if ("N".equals(taxFlag)) {
            //补充税率(交易认定)
            String transTypeId = (String) rowMap.get("transType");//交易ID
            String instcode = (String) rowMap.get("t10");//交易机构
            Organization org = new Organization();
            org.setId(instcode);
            //获取税号
            String taxNo = organizationService.getOrganization(org).getTaxperNumber();
            VerificationInfo transType = new VerificationInfo();
            transType.setTransTypeId(transTypeId);
            transType.setTaxNo(taxNo);
            List list = transTypeService.findTaxRateBytransType(transType);
            if (list.size() > 0) {
                transType = (VerificationInfo) list.get(0);
                String taxRate = transType.getTaxRate();
                rowMap.put("taxRate", taxRate);
                //价税分离
                String income = (String) rowMap.get("accTamt");//收入
                BigDecimal incomeBd = new BigDecimal(income);
                BigDecimal taxRateBd = new BigDecimal(taxRate);
                BigDecimal taxAmtBd = incomeBd.multiply(taxRateBd);
                BigDecimal shortAndOverBd = BigDecimal.ZERO;
                BigDecimal transAmtBd = incomeBd.add(taxAmtBd);
                String taxAmt = taxAmtBd.toString();//税额
                String shortAndOver = shortAndOverBd.toString();//尾差
                String transAmt = transAmtBd.toString();//价税合计
                rowMap.put("taxAmt", taxAmt);
                rowMap.put("shortAndOver", shortAndOver);
                rowMap.put("income", income);
                rowMap.put("accTamt", transAmt);
            }
        }
    }


    /**
     * 列表画面 导出
     */
    public void impdataMetLifeToExcel() throws Exception {
        try {
            String name = "attachment;filename="
                    + URLEncoder.encode("数据导入.xls", "UTF-8").toString();
            response.setHeader("Content-type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", name);
            OutputStream os = response.getOutputStream();
            writeMetLifeDataToExcel(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    /**
     * 创建excel
     *
     * @param os
     * @throws IOException
     * @throws RowsExceededException
     * @throws WriteException
     * @throws Exception
     */
    private void writeMetLifeDataToExcel(OutputStream os) throws IOException,
            RowsExceededException, WriteException, Exception {
        WritableWorkbook wb = Workbook.createWorkbook(os);
        WritableSheet ws = null;
        ws = wb.createSheet("销项数据导入", 0);

        Label header1 = new Label(0, 0, "客户号", JXLTool.getHeader());
        ws.addCell(header1);
        ws.setColumnView(0, 25);
        Label header2 = new Label(1, 0, "产品代码", JXLTool.getHeader());
        ws.addCell(header2);
        ws.setColumnView(1, 25);
        Label header3 = new Label(2, 0, "交易时间", JXLTool.getHeader());
        ws.addCell(header3);
        ws.setColumnView(2, 25);
        Label header4 = new Label(3, 0, "交易金额", JXLTool.getHeader());
        ws.addCell(header4);
        ws.setColumnView(3, 25);
        Label header5 = new Label(4, 0, "税率", JXLTool.getHeader());
        ws.addCell(header5);
        ws.setColumnView(4, 25);
        Label header6 = new Label(5, 0, "收入会计科目", JXLTool.getHeader());
        ws.addCell(header6);
        ws.setColumnView(5, 25);
        Label header7 = new Label(6, 0, "交易发生机构", JXLTool.getHeader());
        ws.addCell(header7);
        ws.setColumnView(6, 25);
        Label header8 = new Label(7, 0, "T1", JXLTool.getHeader());
        ws.addCell(header8);
        ws.setColumnView(7, 25);
        Label header9 = new Label(8, 0, "T2", JXLTool.getHeader());
        ws.addCell(header9);
        ws.setColumnView(8, 25);
        Label header10 = new Label(9, 0, "T3", JXLTool.getHeader());
        ws.addCell(header10);
        ws.setColumnView(9, 25);
        Label header11 = new Label(10, 0, "T4", JXLTool.getHeader());
        ws.addCell(header11);
        ws.setColumnView(10, 25);
        Label header12 = new Label(11, 0, "T5", JXLTool.getHeader());
        ws.addCell(header12);
        ws.setColumnView(11, 25);
        Label header13 = new Label(12, 0, "T6", JXLTool.getHeader());
        ws.addCell(header13);
        ws.setColumnView(11, 25);
        Label header14 = new Label(13, 0, "T7", JXLTool.getHeader());
        ws.addCell(header14);
        ws.setColumnView(11, 25);
        Label header15 = new Label(14, 0, "T8", JXLTool.getHeader());
        ws.addCell(header15);
        ws.setColumnView(11, 25);
        Label header16 = new Label(15, 0, "T9", JXLTool.getHeader());
        ws.addCell(header16);
        ws.setColumnView(11, 25);

        dataInfo = new TransDataInfo();
        dataInfo.setBatchId(batchId);
        dataInfo.setStatus(status);
        List impDatas = impDataMetlifeService.findTransDataInfoListByBatchId(dataInfo, null);
        int count = 1;
        for (int i = 0; i < impDatas.size(); i++) {
            int column = count++;
            setWritableSheet(ws, (TransDataInfo) impDatas.get(i), column);
        }
        wb.write();
        wb.close();
    }

    /**
     * 创建excel
     *
     * @param ws
     * @param dataInfo
     * @param column
     * @throws WriteException
     */
    private void setWritableSheet(WritableSheet ws, TransDataInfo dataInfo,
                                  int column) throws WriteException {
        Label cell1 = new Label(0, column, dataInfo.getCowNnum(),
                JXLTool.getContentFormat());
        ws.addCell(cell1);
        Label cell2 = new Label(1, column, dataInfo.getProDuctCode(),
                JXLTool.getContentFormat());
        ws.addCell(cell2);
        Label cell3 = new Label(2, column, dataInfo.getTrdt(),
                JXLTool.getContentFormat());
        ws.addCell(cell3);
        Label cell4 = new Label(3, column, dataInfo.getAccTamt(),
                JXLTool.getContentFormat());
        ws.addCell(cell4);
        String taxRate = dataInfo.getTaxRate();
        if (taxRate.startsWith(".")) {
            taxRate = "0" + taxRate;
        }
        Label cell5 = new Label(4, column, taxRate,
                JXLTool.getContentFormat());
        ws.addCell(cell5);
        Label cell6 = new Label(5, column, dataInfo.getAlTref(),
                JXLTool.getContentFormat());
        ws.addCell(cell6);

        Label cell7 = new Label(6, column, dataInfo.getT10(),
                JXLTool.getContentFormat());
        ws.addCell(cell7);
        Label cell8 = new Label(7, column, dataInfo.getT1(),
                JXLTool.getContentFormat());
        ws.addCell(cell8);
        Label cell9 = new Label(8, column, dataInfo.getT2(),
                JXLTool.getContentFormat());
        ws.addCell(cell9);
        Label cell10 = new Label(9, column, dataInfo.getT3(),
                JXLTool.getContentFormat());
        ws.addCell(cell10);
        Label cell11 = new Label(10, column, dataInfo.getT4(),
                JXLTool.getContentFormat());
        ws.addCell(cell11);
        Label cell12 = new Label(11, column, dataInfo.getT5(),
                JXLTool.getContentFormat());
        ws.addCell(cell12);
        Label cell13 = new Label(12, column, dataInfo.getT6(),
                JXLTool.getContentFormat());
        ws.addCell(cell13);
        Label cell14 = new Label(13, column, dataInfo.getT7(),
                JXLTool.getContentFormat());
        ws.addCell(cell14);
        Label cell15 = new Label(14, column, dataInfo.getT8(),
                JXLTool.getContentFormat());
        ws.addCell(cell15);
        Label cell16 = new Label(15, column, dataInfo.getT9(),
                JXLTool.getContentFormat());
        ws.addCell(cell16);
    }


    /**
     * 非空校验
     *
     * @param source
     * @return
     */
    public boolean checkNull(String source) {
        if (null == source || "".equals(source)) {
            return true;
        } else {
            return false;
        }
    }

    public ImpDataMetlifeService getImpDataMetlifeService() {
        return impDataMetlifeService;
    }

    public void setImpDataMetlifeService(ImpDataMetlifeService impDataMetlifeService) {
        this.impDataMetlifeService = impDataMetlifeService;
    }

    public TransDataInfo getDataInfo() {
        return dataInfo;
    }

    public void setDataInfo(TransDataInfo dataInfo) {
        this.dataInfo = dataInfo;
    }

    public TransTypeService getTransTypeService() {
        return transTypeService;
    }

    public void setTransTypeService(TransTypeService transTypeService) {
        this.transTypeService = transTypeService;
    }

    public File getAttachment() {
        return attachment;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentFileName() {
        return attachmentFileName;
    }

    public void setAttachmentFileName(String attachmentFileName) {
        this.attachmentFileName = attachmentFileName;
    }

    @Override
    public UserInterfaceConfigService getUserInterfaceConfigService() {
        return userInterfaceConfigService;
    }

    @Override
    public void setUserInterfaceConfigService(UserInterfaceConfigService userInterfaceConfigService) {
        this.userInterfaceConfigService = userInterfaceConfigService;
    }

    @Override
    public OrganizationService getOrganizationService() {
        return organizationService;
    }

    @Override
    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public CustomerService getCustomerService() {
        return customerService;
    }

    @Override
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public List<TransDataInfo> getBatchIdList() {
        return batchIdList;
    }

    public void setBatchIdList(List<TransDataInfo> batchIdList) {
        this.batchIdList = batchIdList;
    }
}
