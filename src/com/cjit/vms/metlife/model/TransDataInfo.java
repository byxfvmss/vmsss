package com.cjit.vms.metlife.model;

import com.cjit.vms.trans.util.DataUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Abel on 2016/4/7.
 */
public class TransDataInfo {

    private String dataId;//id
    private String transId;//交易id
    private String batchId;//	批次id
    private String cowNnum;//客户号
    private String trdt;//交易时间
    private String accTamt;//交易金额
    private String taxRate;//税率
    private String alTref;//收入会计科目
    private String t1;//t1
    private String t2;//t2
    private String t3;//t3
    private String t4;//t4
    private String t5;//t5
    private String t6;//t6
    private String t7;//t7
    private String t8;//t8
    private String t9;//t9
    private String t10;//t10(机构)
    private String t11;
    private String mStatus;//"状态 0-未校验 2-通过校验 1-未通过校验"
    private String status;//"状态 0-未校验 2-通过校验 1-未通过校验"
    private String dStatus;//导入数据字段校验状态
    private String impUser;//导入用户
    private String impTime;//导入时间
    private String reMark;//备注
    private List lstAuthInstId;
    private String startTime;
    private String endTime;
    private String count;
    private String passCount;
    private String unPassCount;
    private String message;
    private String proDuctCode;
    private String instName;
    private String flag;
    private String reaSon;
    private String upperStatus;

    public String getStatusStr(){
        return DataUtil.getBatchStatus(status);
    }
    public String getmStatusStr(){
        return DataUtil.getDataStatus(mStatus);
    }
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReMark() {
        return reMark;
    }

    public void setReMark(String reMark) {
        this.reMark = reMark;
    }

    public List getLstAuthInstId() {
        return lstAuthInstId;
    }

    public void setLstAuthInstId(List lstAuthInstId) {
        this.lstAuthInstId = lstAuthInstId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getCowNnum() {
        return cowNnum;
    }

    public void setCowNnum(String cowNnum) {
        this.cowNnum = cowNnum;
    }

    public String getTrdt() {
        return trdt;
    }

    public void setTrdt(String trdt) {
        this.trdt = trdt;
    }

    public String getAccTamt() {
        return accTamt;
    }

    public void setAccTamt(String accTamt) {
        this.accTamt = accTamt;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getAlTref() {
        return alTref;
    }

    public void setAlTref(String alTref) {
        this.alTref = alTref;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getT3() {
        return t3;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    public String getT4() {
        return t4;
    }

    public void setT4(String t4) {
        this.t4 = t4;
    }

    public String getT5() {
        return t5;
    }

    public void setT5(String t5) {
        this.t5 = t5;
    }

    public String getT6() {
        return t6;
    }

    public void setT6(String t6) {
        this.t6 = t6;
    }

    public String getT7() {
        return t7;
    }

    public void setT7(String t7) {
        this.t7 = t7;
    }

    public String getT8() {
        return t8;
    }

    public void setT8(String t8) {
        this.t8 = t8;
    }

    public String getT9() {
        return t9;
    }

    public void setT9(String t9) {
        this.t9 = t9;
    }

    public String getT10() {
        return t10;
    }

    public void setT10(String t10) {
        this.t10 = t10;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getdStatus() {
        return dStatus;
    }

    public void setdStatus(String dStatus) {
        this.dStatus = dStatus;
    }

    public String getImpUser() {
        return impUser;
    }

    public void setImpUser(String impUser) {
        this.impUser = impUser;
    }

    public String getImpTime() {
        return impTime;
    }

    public void setImpTime(String impTime) {
        this.impTime = impTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPassCount() {
        return passCount;
    }

    public void setPassCount(String passCount) {
        this.passCount = passCount;
    }

    public String getUnPassCount() {
        return unPassCount;
    }

    public void setUnPassCount(String unPassCount) {
        this.unPassCount = unPassCount;
    }

    public String getT11() {
        return t11;
    }

    public void setT11(String t11) {
        this.t11 = t11;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProDuctCode() {
        return proDuctCode;
    }

    public void setProDuctCode(String proDuctCode) {
        this.proDuctCode = proDuctCode;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getReaSon() {
        return reaSon;
    }

    public void setReaSon(String reaSon) {
        this.reaSon = reaSon;
    }

    public String getUpperStatus() {
        return upperStatus;
    }

    public void setUpperStatus(String upperStatus) {
        this.upperStatus = upperStatus;
    }
}
