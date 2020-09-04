package com.cjit.vms.metlife.model;

import java.util.Date;

/**
 * 匹配投表单号表
 * Created by Abel on 2016/2/16.
 */
public class TtmPrcnoMatch {

    private String ttmPrcno;
    private String ttmPrcnoMatchId;
    private String chanNelName;
    private String braNch;
    private Date createTime;
    private Integer matchFlag;
    private String vtmipId;
    private String billId;
    private Date createBeginTime;
    private Date createEndTime;
    private String fapiaoType;
    private String datastatus;


    public String getTtmPrcnoMatchId() {
        return ttmPrcnoMatchId;
    }

    public void setTtmPrcnoMatchId(String ttmPrcnoMatchId) {
        this.ttmPrcnoMatchId = ttmPrcnoMatchId;
    }

    public String getTtmPrcno() {
        return ttmPrcno;
    }

    public void setTtmPrcno(String ttmPrcno) {
        this.ttmPrcno = ttmPrcno;
    }

    public String getChanNelName() {
        return chanNelName;
    }

    public void setChanNelName(String chanNelName) {
        this.chanNelName = chanNelName;
    }

    public String getBraNch() {
        return braNch;
    }

    public void setBraNch(String braNch) {
        this.braNch = braNch;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setMatchFlag(Integer matchFlag) {
        this.matchFlag = matchFlag;
    }

    public Integer getMatchFlag() {
        return matchFlag;
    }

    public String getVtmipId() {
        return vtmipId;
    }

    public void setVtmipId(String vtmipId) {
        this.vtmipId = vtmipId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Date getCreateBeginTime() {
        return createBeginTime;
    }

    public void setCreateBeginTime(Date createBeginTime) {
        this.createBeginTime = createBeginTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public String getFapiaoType() {
        return fapiaoType;
    }

    public void setFapiaoType(String fapiaoType) {
        this.fapiaoType = fapiaoType;
    }

    public String getDatastatus() {
        return datastatus;
    }

    public void setDatastatus(String datastatus) {
        this.datastatus = datastatus;
    }
}
