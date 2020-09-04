package com.cjit.vms.metlife.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cjit.crms.util.DateUtil;

import com.cjit.vms.metlife.model.SpecialRegIster;
import com.cjit.vms.metlife.service.RegIsterListService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.service.TaxDiskInfoService;
import com.cjit.vms.trans.service.VmsCommonService;

public class RegIsterListAction extends DataDealAction {

    private RegIsterListService regIsterListService;
    private SpecialRegIster ister;
    private String flag;
    private String message;
    private String signType;
    private TaxDiskInfoService taxDiskInfoService;
    private List authInstList = new ArrayList();
    protected VmsCommonService vmsCommonService;
    protected Map chanNelList;
    protected Map docTypeList;
    protected Map signList;

    public String getRegisterAction() {

        InstInfo in = new InstInfo();
        in.setUserId(this.getCurrentUser().getId());
        List lstAuthInstId = new ArrayList();
        this.getAuthInstList(lstAuthInstId);
        in.setLstAuthInstIds(lstAuthInstId);
        authInstList = taxDiskInfoService.getInstInfoList(in);

        chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
        docTypeList = this.vmsCommonService.findCodeDictionary("SIGN_DOC_TYPE");
        signList = this.vmsCommonService.findCodeDictionary("SIGN_TYPE");

        if (ister != null && ister.getTtmPrcno() != null && !"".equals(ister.getTtmPrcno())) {
            ister.setTtmPrcno(ister.getTtmPrcno().replaceAll(",", "").trim());
            if (ister.getRepNum() == null || "".equals(ister.getRepNum())) {
                ister.setRepNum("");
            }
        }
        if (ister == null) {
            ister = new SpecialRegIster();
            ister.setLstAuthInstId(lstAuthInstId);
        } else {
            ister.setLstAuthInstId(lstAuthInstId);
        }
        List l = regIsterListService.getRegIsterList(ister, paginationList, true);
        return SUCCESS;
    }

    public String addOrUpRegisterList() {
        String cherNum = request.getParameter("cherNum");
        String flag = request.getParameter("flag");

        chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
        docTypeList = this.vmsCommonService.findCodeDictionary("SIGN_DOC_TYPE");

        InstInfo in = new InstInfo();
        in.setUserId(this.getCurrentUser().getId());
        List lstAuthInstId = new ArrayList();
        this.getAuthInstList(lstAuthInstId);
        in.setLstAuthInstIds(lstAuthInstId);
        authInstList = taxDiskInfoService.getInstInfoList(in);

        if (cherNum != null && !"".equals(cherNum)) {
            ister = new SpecialRegIster();
            ister.setCherNum(cherNum);
            List ls = regIsterListService.getRegIsterList(ister, paginationList, false);
            ister = (SpecialRegIster) ls.get(0);
        }
        this.setFlag(flag);
        return SUCCESS;
    }

    public String saveRegIsterList() {
        try {

            if (this.getFlag() != null && "add".equals(this.getFlag())) {
                if (ister.getCherNum() == null || "".equals(ister.getCherNum())) {
                    ister.setCherNum(" ");
                }
                if (ister.getRepNum() == null || "".equals(ister.getRepNum())) {
                    ister.setRepNum(" ");
                }
                SpecialRegIster isterRegIster = new SpecialRegIster();
                isterRegIster.setCherNum(ister.getCherNum());
                isterRegIster.setRepNum(ister.getRepNum());

                List lstAuthInstId = new ArrayList();
                this.getAuthInstList(lstAuthInstId);

                isterRegIster.setLstAuthInstId(lstAuthInstId);
                List ls = regIsterListService.getRegIsterList(isterRegIster, paginationList, false);
                if (ls.size() > 0) {
                    chanNelList = this.vmsCommonService.findCodeDictionary("SIGN_CHANNEL_TYPE");
                    docTypeList = this.vmsCommonService.findCodeDictionary("SIGN_DOC_TYPE");

                    InstInfo in = new InstInfo();
                    in.setUserId(this.getCurrentUser().getId());
                    in.setLstAuthInstIds(lstAuthInstId);
                    authInstList = taxDiskInfoService.getInstInfoList(in);

                    this.setRESULT_MESSAGE("保单号,旧保单号不可重复录入请重新输入!");
                    return ERROR;
                }
                ister.setCreateUser(this.getCurrentUser().getUsername());
                ister.setCreateDate(DateUtil.parseDateToString(new Date(), DateUtil.FORMAT_DATE));
                regIsterListService.saveRegIsterList(ister, true);
            }
            if (this.getFlag() != null && "update".equals(this.getFlag())) {
                regIsterListService.saveRegIsterList(ister, false);
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String delRegIsterList() {
        try {
            SpecialRegIster sister = new SpecialRegIster();
            sister.setCherNum(request.getParameter("cherNum"));
            regIsterListService.delRegIsterList(sister);
            this.setRESULT_MESSAGE("删除成功!");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            this.setRESULT_MESSAGE("删除失败请重试!");
            return ERROR;
        }
    }

    public SpecialRegIster getIster() {
        return ister;
    }

    public void setIster(SpecialRegIster ister) {
        this.ister = ister;
    }

    public RegIsterListService getRegIsterListService() {
        return regIsterListService;
    }


    public void setRegIsterListService(RegIsterListService regIsterListService) {
        this.regIsterListService = regIsterListService;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TaxDiskInfoService getTaxDiskInfoService() {
        return taxDiskInfoService;
    }

    public void setTaxDiskInfoService(TaxDiskInfoService taxDiskInfoService) {
        this.taxDiskInfoService = taxDiskInfoService;
    }

    public List getAuthInstList() {
        return authInstList;
    }

    public void setAuthInstList(List authInstList) {
        this.authInstList = authInstList;
    }

    @Override
    public VmsCommonService getVmsCommonService() {
        return vmsCommonService;
    }

    @Override
    public void setVmsCommonService(VmsCommonService vmsCommonService) {
        this.vmsCommonService = vmsCommonService;
    }

    public Map getChanNelList() {
        return chanNelList;
    }

    public void setChanNelList(Map chanNelList) {
        this.chanNelList = chanNelList;
    }

    public Map getDocTypeList() {
        return docTypeList;
    }

    public void setDocTypeList(Map docTypeList) {
        this.docTypeList = docTypeList;
    }

    public Map getSignList() {
        return signList;
    }

    public void setSignList(Map signList) {
        this.signList = signList;
    }

    public String getSignType() {
        return signType;
    }
}
