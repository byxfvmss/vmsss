package com.cjit.vms.metlife.model;

import cjit.crms.util.DateUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import com.cjit.common.util.NumberUtils;
import com.cjit.vms.trans.util.DataUtil;

/**
 * Created by Abel-西阳 on 2016/2/19.
 */
public class HistoryTransInfo {

    private String busiNessId;//交易id
    private String cherNum;//保单号
    private String repNum;//旧保单号
    private String ttmPrcno;//投保单号
    private String cownNum;//客户号
    private String cownName;//客户名称
    private String oriGcurr;//交易币种
    private BigDecimal oriGamt;//原币种交易金额
    private BigDecimal accTamt;//本币交易金额
    private String batcTrcde;//交易描述
    private String invTyp;//发票类型
    private String feeTyp;//费用类型
    private String billFreq;//交费频率
    private String polYear;//保单年度
    private String dsouRce;//数据来源
    private String altRef;//收入会计科目
    private String zntCode01;//t0
    private String zntCode02;//t1
    private String zntCode03;//t2
    private String zntCode04;//t3
    private String zntCode05;//t4
    private String zntCode06;//t5
    private String zntCode07;//t6
    private String zntCode08;//t7
    private String zntCode09;//t8
    private String zntCode010;//t9
    private String lifCnum;//被保险人客户号
    private String planLongDesc;//主险名称
    private Integer premTerm;//期数
    private String withDrawyn;//是否收回发票
    private String vrtFnd;//基金代码
    private String znprJtcd;//项目代码
    private String longDesc;//项目描述
    private String speFlag;//特殊类型
    private String spereMark;//后续处理的具体内容
    private String dupyn;//是否重复数据
    private String priyn;//是否已打印
    private String impno;//原dmp发票号码
    private String[] busiNessIds;
    private String feeTypCh;
    private String billFreqCh;
    private String dsouRceCh;
    private String chanNelCh;



    /**
     * 以防调用日期出错 调用日期 以及 日期字符串是请务必注意以下注释 谢谢
     */
    private Date trdt;//交易日期 - 注:此日期不为空时自动转换成 2000-01-01 类型的字符串 调用名称是 trdtStr
    private String trdtStr;//交易日期字符串 - 注:此日期字符串不为空时自动转换成 2000-01-01 格式的日期 调用名称是 trdt

    private Date trdtBeginDate;
    private String trdtBeginStr;//交易开始日期字符串 - 注:此日期字符串不为空时自动转换成 2000-01-01 格式的日期 调用名称是 trdtBeginDate
    private Date trdtEndDate;
    private String trdtEndStr;//交易结束日期字符串 - 注:此日期字符串不为空时自动转换成 2000-01-01 格式的日期 调用名称是 trdtEndDate

    private Date hisSdte;//承保日期 - 注:此日期不为空时自动转换成 2000-01-01 类型的字符串 调用名称是 hisSdteStr
    private String hisSdteStr;//承保日期字符串 - 注:此日期字符串不为空时自动转换成 2000-01-01 格式的日期 调用名称是 hisSdte

    private Date hisSdteBeginDate;
    private String hisSdteBeginStr;//承保开始日期字符串 - 注:此日期字符串不为空时自动转换成 2000-01-01 格式的日期 调用名称是 hisSdteBeginDate
    private Date hisSdteEndDate;
    private String hisSdteEndStr;//承保结束日期字符串 - 注:此日期字符串不为空时自动转换成 2000-01-01 格式的日期 调用名称是 hisSdteEndStr

    private Date instFrom;//交费起始日期 - 注:此日期不为空时自动转换成 2000-01-01 类型的字符串 调用名称是 instFromStr
    private String instFromStr;//交费起始日期字符串 - 注:此日期字符串不为空时自动转换成 2000-01-01 格式的日期 调用名称是 trdt

    private Date insTto;//交费终止日期 - 注:此日期不为空时自动转换成 2000-01-01 类型的字符串 调用名称是 insTtoStr
    private String insTtoStr;//交费终止日期字符串 - 注:此日期字符串不为空时自动转换成 2000-01-01 格式的日期 调用名称是 trdt

    private Date occDate;//生效日期 - 注:此日期不为空时自动转换成 2000-01-01 类型的字符串 调用名称是 occDateStr
    private String occDateStr;//生效日期字符串 - 注:此日期字符串不为空时自动转换成 2000-01-01 格式的日期 调用名称是 trdt

    private String transFerFlag;
    private String transFerFlagCh;

    public String getTransFerFlag() {
        return transFerFlag;
    }

    public String getTransFerFlagCh() {
        return DataUtil.getTransFerFlag(transFerFlag);
    }

    public void setTransFerFlag(String transFerFlag) {
        this.transFerFlag = transFerFlag;
    }

    public String getCownName() {
        return cownName;
    }

    public void setCownName(String cownName) {
        this.cownName = cownName;
    }


    public String getTtmPrcno() {
        return ttmPrcno;
    }

    public void setTtmPrcno(String ttmPrcno) {
        this.ttmPrcno = ttmPrcno;
    }

    public String getBusiNessId() {
        return busiNessId;
    }

    public void setBusiNessId(String busiNessId) {
        this.busiNessId = busiNessId;
    }

    public String getCherNum() {
        return cherNum;
    }

    public void setCherNum(String cherNum) {
        this.cherNum = cherNum;
    }

    public String getRepNum() {
        return repNum;
    }

    public void setRepNum(String repNum) {
        this.repNum = repNum;
    }

    public String getCownNum() {
        return cownNum;
    }

    public void setCownNum(String cownNum) {
        this.cownNum = cownNum;
    }

    public String getOriGcurr() {
        return oriGcurr;
    }

    public void setOriGcurr(String oriGcurr) {
        this.oriGcurr = oriGcurr;
    }

    public BigDecimal getOriGamt() {
        return oriGamt;
    }

    public void setOriGamt(BigDecimal oriGamt) {
        this.oriGamt = oriGamt;
    }

    public String getAccTamtStr() {
        return NumberUtils.format(accTamt,"",2);
    }
    public BigDecimal getAccTamt() {
        return accTamt;
    }

    public void setAccTamt(BigDecimal accTamt) {
        this.accTamt = accTamt;
    }

    public Date getTrdt() {
        return trdt;
    }

    public void setTrdt(Date trdt) {
        this.trdt = trdt;
    }

    public String getBatcTrcde() {
        return batcTrcde;
    }

    public void setBatcTrcde(String batcTrcde) {
        this.batcTrcde = batcTrcde;
    }

    public String getInvTyp() {
        return invTyp;
    }

    public void setInvTyp(String invTyp) {
        this.invTyp = invTyp;
    }

    public String getFeeTyp() {
        return feeTyp;
    }

    public String getFeeTypCh() {
        return DataUtil.getFeeTyp(feeTyp);
    }

    public String getBillFreqCh() {
        return DataUtil.getBillFreq(billFreq);
    }

    public String getChanNelCh() {
        return DataUtil.getChanNel(zntCode07);
    }

    public String getDsouRceCh() {
        return DataUtil.getDsouRce(dsouRce);
    }

    public void setFeeTyp(String feeTyp) {
        this.feeTyp = feeTyp;
    }

    public String getBillFreq() {
        return billFreq;
    }

    public void setBillFreq(String billFreq) {
        this.billFreq = billFreq;
    }

    public String getPolYear() {
        return polYear;
    }

    public void setPolYear(String polYear) {
        this.polYear = polYear;
    }

    public Date getHisSdte() {
        return hisSdte;
    }

    public void setHisSdte(Date hisSdte) {
        this.hisSdte = hisSdte;
    }

    public String getDsouRce() {
        return dsouRce;
    }

    public void setDsouRce(String dsouRce) {
        this.dsouRce = dsouRce;
    }

    public String getAltRef() {
        return altRef;
    }

    public void setAltRef(String altRef) {
        this.altRef = altRef;
    }

    public String getZntCode01() {
        return zntCode01;
    }

    public void setZntCode01(String zntCode01) {
        this.zntCode01 = zntCode01;
    }

    public String getZntCode02() {
        return zntCode02;
    }

    public void setZntCode02(String zntCode02) {
        this.zntCode02 = zntCode02;
    }

    public String getZntCode03() {
        return zntCode03;
    }

    public void setZntCode03(String zntCode03) {
        this.zntCode03 = zntCode03;
    }

    public String getZntCode04() {
        return zntCode04;
    }

    public void setZntCode04(String zntCode04) {
        this.zntCode04 = zntCode04;
    }

    public String getZntCode05() {
        return zntCode05;
    }

    public void setZntCode05(String zntCode05) {
        this.zntCode05 = zntCode05;
    }

    public String getZntCode06() {
        return zntCode06;
    }

    public void setZntCode06(String zntCode06) {
        this.zntCode06 = zntCode06;
    }

    public String getZntCode07() {
        return zntCode07;
    }

    public void setZntCode07(String zntCode07) {
        this.zntCode07 = zntCode07;
    }

    public String getZntCode08() {
        return zntCode08;
    }

    public void setZntCode08(String zntCode08) {
        this.zntCode08 = zntCode08;
    }

    public String getZntCode09() {
        return zntCode09;
    }

    public void setZntCode09(String zntCode09) {
        this.zntCode09 = zntCode09;
    }

    public String getZntCode010() {
        return zntCode010;
    }

    public void setZntCode010(String zntCode010) {
        this.zntCode010 = zntCode010;
    }

    public String getLifCnum() {
        return lifCnum;
    }

    public void setLifCnum(String lifCnum) {
        this.lifCnum = lifCnum;
    }

    public String getPlanLongDesc() {
        return planLongDesc;
    }

    public void setPlanLongDesc(String planLongDesc) {
        this.planLongDesc = planLongDesc;
    }

    public Date getInstFrom() {
        return instFrom;
    }

    public void setInstFrom(Date instFrom) {
        this.instFrom = instFrom;
    }

    public Date getInsTto() {
        return insTto;
    }

    public void setInsTto(Date insTto) {
        this.insTto = insTto;
//        this.insTtoStr = DateUtil.parseDateToString(insTto,DateUtil.FORMAT_DATE);
    }

    public Date getOccDate() {
        return occDate;
    }

    public void setOccDate(Date occDate) {
        this.occDate = occDate;
    }

    public Integer getPremTerm() {
        return premTerm;
    }

    public void setPremTerm(Integer premTerm) {
        this.premTerm = premTerm;
    }

    public String getWithDrawyn() {
        return withDrawyn;
    }

    public void setWithDrawyn(String withDrawyn) {
        this.withDrawyn = withDrawyn;
    }

    public String getVrtFnd() {
        return vrtFnd;
    }

    public void setVrtFnd(String vrtFnd) {
        this.vrtFnd = vrtFnd;
    }

    public String getZnprJtcd() {
        return znprJtcd;
    }

    public void setZnprJtcd(String znprJtcd) {
        this.znprJtcd = znprJtcd;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getSpeFlag() {
        return speFlag;
    }

    public void setSpeFlag(String speFlag) {
        this.speFlag = speFlag;
    }

    public String getSpereMark() {
        return spereMark;
    }

    public void setSpereMark(String spereMark) {
        this.spereMark = spereMark;
    }

    public String getDupyn() {
        return dupyn;
    }

    public void setDupyn(String dupyn) {
        this.dupyn = dupyn;
    }

    public String getPriyn() {
        return priyn;
    }

    public String getPriynCh() {
        if("N".equals(priyn)){
            return "否";
        }else{
            return "是";
        }
    }

    public void setPriyn(String priyn) {
        this.priyn = priyn;
    }

    public String getImpno() {
        return impno;
    }

    public void setImpno(String impno) {
        this.impno = impno;
    }

    public String getTrdtStr() {
        if (this.trdt != null) {
            return DateUtil.parseDateToString(this.getTrdt(), DateUtil.FORMAT_DATE);
        }
        return trdtStr;
    }

    public void setTrdtStr(String trdtStr) throws ParseException {
        if (this.trdt == null) {
            this.setTrdt(DateUtil.parseStringToDate(trdtStr, DateUtil.FORMAT_DATE));
        }
        this.trdtStr = trdtStr;
    }

    public String getHisSdteStr() {
        if (this.hisSdte != null) {
            return DateUtil.parseDateToString(this.getHisSdte(), DateUtil.FORMAT_DATE);
        }
        return hisSdteStr;
    }

    public void setHisSdteStr(String hisSdteStr) throws ParseException {
        if (this.hisSdte == null) {
            this.setHisSdte(DateUtil.parseStringToDate(hisSdteStr, DateUtil.FORMAT_DATE));
        }
        this.hisSdteStr = hisSdteStr;
    }

    public String getInstFromStr() {
        if (this.instFrom != null) {
            return DateUtil.parseDateToString(this.getInstFrom(), DateUtil.FORMAT_DATE);
        }
        return instFromStr;
    }

    public void setInstFromStr(String instFromStr) throws ParseException {
        if (this.instFrom == null) {
            this.setInstFrom(DateUtil.parseStringToDate(instFromStr, DateUtil.FORMAT_DATE));
        }
        this.instFromStr = instFromStr;
    }

    public String getInsTtoStr() {
        if (this.insTto != null) {
            return DateUtil.parseDateToString(this.getInsTto(), DateUtil.FORMAT_DATE);
        }
        return insTtoStr;
    }

    public void setInsTtoStr(String insTtoStr) throws ParseException {
        if (this.insTto == null) {
            this.setInsTto(DateUtil.parseStringToDate(insTtoStr, DateUtil.FORMAT_DATE));
        }
        this.insTtoStr = insTtoStr;
    }

    public String getOccDateStr() {
        if (this.occDate != null) {
            return DateUtil.parseDateToString(this.getOccDate(), DateUtil.FORMAT_DATE);
        }
        return occDateStr;
    }

    public void setOccDateStr(String occDateStr) throws ParseException {
        if (this.occDate == null) {
            this.setOccDate(DateUtil.parseStringToDate(occDateStr, DateUtil.FORMAT_DATE));
        }
        this.occDateStr = occDateStr;
    }


    public String getTrdtBeginStr() {
        return trdtBeginStr;
    }

    public void setTrdtBeginStr(String trdtBeginStr) {
        this.trdtBeginStr = trdtBeginStr;
    }

    public String getTrdtEndStr() {
        return trdtEndStr;
    }

    public void setTrdtEndStr(String trdtEndStr) {
        this.trdtEndStr = trdtEndStr;
    }

    public String getHisSdteBeginStr() {
        return hisSdteBeginStr;
    }

    public void setHisSdteBeginStr(String hisSdteBeginStr) {
        this.hisSdteBeginStr = hisSdteBeginStr;
    }

    public String getHisSdteEndStr() {
        return hisSdteEndStr;
    }

    public void setHisSdteEndStr(String hisSdteEndStr) {
        this.hisSdteEndStr = hisSdteEndStr;
    }


    /**
     *
     */

    public Date getTrdtBeginDate() throws ParseException {
        if (this.trdtBeginStr != null && !"".equals(this.trdtBeginStr)) {
            return DateUtil.parseStringToDate(this.trdtBeginStr, DateUtil.FORMAT_DATE);
        }
        return trdtBeginDate;
    }

    public void setTrdtBeginDate(Date trdtBeginDate) {
        this.trdtBeginDate = trdtBeginDate;
    }

    public Date getTrdtEndDate() throws ParseException {
        if (this.trdtEndStr != null && !"".equals(this.trdtEndStr)) {
            return DateUtil.parseStringToDate(this.trdtEndStr, DateUtil.FORMAT_DATE);
        }
        return trdtEndDate;
    }

    public void setTrdtEndDate(Date trdtEndDate) {
        this.trdtEndDate = trdtEndDate;
    }

    /**
     *
     */

    public Date getHisSdteBeginDate() throws ParseException{
        if(this.hisSdteBeginStr!=null&&!"".equals(this.hisSdteBeginStr)){
            return DateUtil.parseStringToDate(this.hisSdteBeginStr,DateUtil.FORMAT_DATE);
        }
        return hisSdteBeginDate;
    }

    public void setHisSdteBeginDate(Date hisSdteBeginDate) {
        this.hisSdteBeginDate = hisSdteBeginDate;
    }

    public Date getHisSdteEndDate() throws ParseException{
        if(this.hisSdteEndStr!=null&&!"".equals(this.hisSdteEndStr)){
            return DateUtil.parseStringToDate(this.hisSdteEndStr,DateUtil.FORMAT_DATE);
        }
        return hisSdteEndDate;
    }

    public void setHisSdteEndDate(Date hisSdteEndDate) {
        this.hisSdteEndDate = hisSdteEndDate;
    }

    public String getDupynStr(){
        if(dupyn!=null && "Y".equals(dupyn)){
            return "是";
        }else if(dupyn!=null && "N".equals(dupyn)){
            return "否";
        }
        return "";
    }

//    public String getChanNelStr(){
//        return DataUtil.getChanNel(chanNelCh);
//    }
}
