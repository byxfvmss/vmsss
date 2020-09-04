package com.cjit.vms.metlife.model;

import com.cjit.vms.trans.util.DataUtil;

import java.util.List;

/**
 * 特殊登记
 * @author Abel
 *
 */
public class SpecialRegIster {

	private String ttmPrcno;
	private String cherNum;
	private String repNum;
	private String instId;
	private String instName;
	private String chanNel;
	private String sign_a;
	private String sign_b;
	private String sign_c;
	private String sign_d;
	private String sign_e;
	private String sign_f;
	private String sign_g;
	private String sign_h;
	private String sign_i;
	private String sign_j;
	private String sign_k;
	private String sign_l;
	private String reMarks;
	private String docType;
	private String createDate;
	private String createUser;
	private String flag;
	private String signType;
	private List lstAuthInstId;
	private String signParam;
	public SpecialRegIster(){
		super();
	}

	public String getChanNelCh(){
		return DataUtil.getChanNel(chanNel);
	}
	public String getTtmPrcno() {
		return ttmPrcno;
	}
	public void setTtmPrcno(String ttmPrcno) {
		this.ttmPrcno = ttmPrcno;
	}
	public String getCherNum() {
		return cherNum;
	}
	public void setCherNum(String cherNum) {
		this.cherNum = cherNum;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getChanNel() {
		return chanNel;
	}
	public void setChanNel(String chanNel) {
		this.chanNel = chanNel;
	}
	public String getSign_a() {
		return sign_a;
	}
	public void setSign_a(String sign_a) {
		this.sign_a = sign_a;
	}
	public String getSign_b() {
		return sign_b;
	}
	public void setSign_b(String sign_b) {
		this.sign_b = sign_b;
	}
	public String getSign_c() {
		return sign_c;
	}
	public void setSign_c(String sign_c) {
		this.sign_c = sign_c;
	}
	public String getSign_d() {
		return sign_d;
	}
	public void setSign_d(String sign_d) {
		this.sign_d = sign_d;
	}
	public String getSign_e() {
		return sign_e;
	}
	public void setSign_e(String sign_e) {
		this.sign_e = sign_e;
	}
	public String getSign_f() {
		return sign_f;
	}
	public void setSign_f(String sign_f) {
		this.sign_f = sign_f;
	}
	public String getSign_g() {
		return sign_g;
	}
	public void setSign_g(String sign_g) {
		this.sign_g = sign_g;
	}
	public String getSign_h() {
		return sign_h;
	}
	public void setSign_h(String sign_h) {
		this.sign_h = sign_h;
	}
	public String getSign_i() {
		return sign_i;
	}
	public void setSign_i(String sign_i) {
		this.sign_i = sign_i;
	}
	public String getSign_j() {
		return sign_j;
	}
	public void setSign_j(String sign_j) {
		this.sign_j = sign_j;
	}
	public String getSign_k() {
		return sign_k;
	}
	public void setSign_k(String sign_k) {
		this.sign_k = sign_k;
	}
	public String getSign_l() {
		return sign_l;
	}
	public void setSign_l(String sign_l) {
		this.sign_l = sign_l;
	}
	public String getReMarks() {
		return reMarks;
	}
	public void setReMarks(String reMarks) {
		this.reMarks = reMarks;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getRepNum() {
		return repNum;
	}

	public void setRepNum(String repNum) {
		this.repNum = repNum;
	}

	public String getSignType() {
		String signTypeStr = "";
		if(this.getSign_a()!=null&& DataUtil.SIGN_A.equals(this.getSign_a())){
			signTypeStr += DataUtil.SIGN_A_CH +",";
		}if(this.getSign_b()!=null&& DataUtil.SIGN_B.equals(this.getSign_b())){
			signTypeStr += DataUtil.SIGN_B_CH +",";
		}if(this.getSign_c()!=null&& DataUtil.SIGN_C.equals(this.getSign_c())){
			signTypeStr += DataUtil.SIGN_C_CH +",";
		}if(this.getSign_d()!=null&& DataUtil.SIGN_D.equals(this.getSign_d())){
			signTypeStr += DataUtil.SIGN_D_CH +",";
		}if(this.getSign_e()!=null&& DataUtil.SIGN_E.equals(this.getSign_e())){
			signTypeStr += DataUtil.SIGN_E_CH +",";
		}if(this.getSign_f()!=null&& DataUtil.SIGN_F.equals(this.getSign_f())){
			signTypeStr += DataUtil.SIGN_F_CH +",";
		}if(this.getSign_g()!=null&& DataUtil.SIGN_G.equals(this.getSign_g())){
			signTypeStr += DataUtil.SIGN_A_CH +",";
		}if(this.getSign_h()!=null&& DataUtil.SIGN_H.equals(this.getSign_h())){
			signTypeStr += DataUtil.SIGN_H_CH +",";
		}if(this.getSign_i()!=null&& DataUtil.SIGN_I.equals(this.getSign_i())){
			signTypeStr += DataUtil.SIGN_I_CH +",";
		}if(this.getSign_j()!=null&& DataUtil.SIGN_J.equals(this.getSign_j())){
			signTypeStr += DataUtil.SIGN_J_CH +",";
		}if(this.getSign_k()!=null&& DataUtil.SIGN_K.equals(this.getSign_k())){
			signTypeStr += DataUtil.SIGN_K_CH +",";
		}if(this.getSign_l()!=null&& DataUtil.SIGN_L.equals(this.getSign_l())){
			signTypeStr += DataUtil.SIGN_L_CH +",";
		}
		String[] str = signTypeStr.split(",");
		if(str.length>0&&signTypeStr!=null&&!"".equals(signTypeStr)){
			return signTypeStr.substring(0,signTypeStr.length()-1);
		}
		return "";
	}


	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSignParam() {
		return signParam;
	}

	public void setSignParam(String signParam) {
		this.signParam = signParam;
	}

	public List getLstAuthInstId() {
		return lstAuthInstId;
	}

	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
}
