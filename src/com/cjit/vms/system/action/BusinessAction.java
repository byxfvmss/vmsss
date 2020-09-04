package com.cjit.vms.system.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Business;
import com.cjit.vms.system.model.UserColComments;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.util.SecurityPassword;

public class BusinessAction extends DataDealAction {
	
	protected static final String FORM_BUSI = "formBusiness";
	protected static final String FORM_BUSI_Sup = "formBusinessSup";
	protected static final String FORM_BUSI_Edit = "formBusinessEdit";
	private static final long serialVersionUID = 1L;
	private Business business = new Business();
	private List businessInfoList;
	private String id;// 交易id号
	private String responesForCheck;
	private String message;
	private List infoList;
	private UserColComments userColComments = new UserColComments();
	
	public String listBusiness() {
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 方法名称: listBusiinessHead|描述: 显示交易认定列表列表查询帧
	 * </p>
	 * 
	 * @return 显示交易认定设置列表查询帧
	 */
	public String listBusiinessHead() {
		return listBusinessMain();
	}
	
	public String listBusinessMain() {
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				this.request.getSession().removeAttribute("message");
				this.message = "";
				fromFlag = null;
			}
			User currentUser = this.getCurrentUser();
//			String busiCode = request.getParameter("busicode");
//			String busiCName = request.getParameter("busicname");
			String taxRate = request.getParameter("taxrate");
//			String hasTax = request.getParameter("hastax");
//			String autoprint = request.getParameter("autoprint");
//			System.out.println("是否自动打印：" + autoprint);
//			if(!StringUtils.isNotEmpty(busiCode)){
//				business.setBusinessCode(null);
//			}else{
//				business.setBusinessCode(busiCode);
//			}
//			if(!StringUtils.isNotEmpty(busiCName)){
//				business.setBusinessCName(null);
//			}else{
//				business.setBusinessCName(busiCName);
//			}
			if(!StringUtils.isNotEmpty(taxRate)){
				business.setTaxRate(null);
			}else{
				business.setTaxRate(new BigDecimal(taxRate));
			}
//			if(!StringUtils.isNotEmpty(hasTax)){
//				business.setHasTax(null);
//			}else{
//				business.setHasTax(hasTax);
//			}
//			if (!StringUtils.isNotEmpty(autoprint)) {
//				business.setAutoPrint(null);
//			}else{
//				if ("-1".equals(autoprint)) {
//					business.setAutoPrint(null);
//				}else{
//					business.setAutoPrint(autoprint);
//				}
//			}
			businessInfoList = businessService.findBusinessMgtList(business, paginationList);
			this.request.setAttribute("businessInfoList", businessInfoList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BusinessAction-listBusinessMain", e);
		}
		return ERROR;
	}
	
	public String findBusiAndBusiSup() {
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				this.request.getSession().removeAttribute("message");
				this.message = "";
				fromFlag = null;
			}
			User currentUser = this.getCurrentUser();
			String busiCode = request.getParameter("businesscode");
			if(!StringUtils.isNotEmpty(busiCode)){
				business.setBusinessCode(null);
			}else{
				business.setBusinessCode(busiCode);
			}
			
			businessInfoList = businessService.findBusiAndBusiSup(business, currentUser.getId(), paginationList);
			this.request.setAttribute("businessInfoList", businessInfoList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BusinessAction-listBusinessMain", e);
		}
		return ERROR;
	}
	
	/**
	 * <p>
	 * 方法名称: loadInstAndUsrXml|描述: 异步获取交易和用户
	 * </p>
	 */
	public void loadBusinessAndUsrXml() {
//		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		User currentUser = this.getCurrentUser();
		try {
			String level = SecurityPassword.filterStr(request.getParameter("level"));
			String reInit = SecurityPassword.filterStr(request.getParameter("reInit"));
			id = SecurityPassword.filterStr(id);
//			String level = request.getParameter("level");
//			String reInit = request.getParameter("reInit");
			String strInstList = null;
			if (StringUtils.isNotEmpty(level)) {// 这里的层级是根据点击页面按钮进来的
				strInstList = businessService.loadInstAndUsrXmlEx(id, Integer
						.valueOf(level).intValue(), currentUser, reInit);
			} else {
				strInstList = businessService.loadInstAndUsrXmlEx(id, currentUser, reInit);
			}
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print(strInstList);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.getWriter().close();
				System.gc();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * <p>
	 * 方法名称: createBusiness|描述: 新增
	 * </p>
	 * 
	 * @return
	 */
	public String createBusiness() {
		try {
			return FORM_BUSI;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}
	
	/**
	 * <p>
	 * 方法名称: saveInvoice|描述: 保存业务信息
	 * </p>
	 * 
	 * @return 发票列表
	 * 
	 */
	public String saveBusiness() {
		String editType=request.getParameter("editType");
//		business.setTaxRate(business.getTaxRate());
//		business.setSql(business.getSql());
		if(editType.equals("edit")){
			String sql = business.getSql().substring(("select TRANS_TYPE from VMS_TRANS_INFO where 1=1 ").length(), business.getSql().length());
			responesForCheck = transInfoService.checkTranSql(sql);
			if(responesForCheck.equals("sql语法错误！")){
				this.message = "修改失败,sql语法错误";
				infoList = transInfoService.findUserColComments(userColComments);
				return "editError";
			}
			business.setSql(sql);
			businessService.updateBusinessMgt(business);
			this.message = "修改成功";
			return SUCCESS;
		}else{
			try{
//				Business businessForInst = new Business();
//				businessForInst = 	businessService.getInstByInstId(business.getBusinessCode());
//				if(businessForInst != null){
//					this.message = "已存在该交易码："+businessForInst.getBusinessCode();
//					return SUCCESS;
//				}else{
//					business.setBusinessId(StringUtil.getUUID());
//					business.setIsUse("1");
//					business.setOrderNum(Integer.valueOf(0));
					Business businessForInst = new Business();
					businessForInst.setSerialNo(business.getSerialNo());
					List list = businessService.findBusinessMgtList(businessForInst, paginationList);
					if(list!=null&&list.size()>0){
						this.message = "编码重复";
						setRESULT_MESSAGE("编码重复");
						return ERROR;
					}
					businessService.saveMgt(business);
					this.message = "添加交易成功";
					return SUCCESS;
//				}
			}catch (Exception e) {
				e.printStackTrace();
				log.error("BusinessAction-saveBusiness", e);
				this.message = "添加交易失败";
				setRESULT_MESSAGE("添加交易失败");
				return ERROR;
			}
		}
	}

	/**
	 * <p>
	 * 方法名称: createBusinessSup|描述: 新增子节点
	 * </p>
	 * 
	 * @return
	 */
	public String createBusinessSup() {
		try {
			String id = request.getParameter("id");
			Business bSup = new Business();
			bSup = businessService.getBusinessListByCode(id);
			return FORM_BUSI_Sup;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}
	
	public String deleteBusiness(){
		String[] selectSerialNos = this.request.getParameterValues("selectSerialNos");
//		for(int i=0;i<getIds().size();i++){
//			Business business = businessService.getBusinessListByCode((String) getIds().get(i));
//			if(business!=null){
//				businessService.updateBusinessIsUse(business);
//				
//			}
//		}
		
		for (int i = 0; i < selectSerialNos.length; i++) {
			if(selectSerialNos[i]!=null&&!"".equals(selectSerialNos[i]))
				businessService.deleteMgt(selectSerialNos[i]);
		}
		
		this.message = "删除成功";
		return SUCCESS;
	}
	
	public String editBusi() {
		try {
			this.message = null;
			String taxRateId = request.getParameter("taxRateId");
			business = (Business) businessService.getMgtById(taxRateId);
			String sql = "";
			if(business.getSql()==null||"".equalsIgnoreCase(business.getSql())
					||"null".equalsIgnoreCase(business.getSql()))
				sql = "select TRANS_TYPE from VMS_TRANS_INFO where 1=1 ";
			else
				sql = "select TRANS_TYPE from VMS_TRANS_INFO where 1=1 "+business.getSql();
			business.setSql(sql);
//			StringBuffer sb = new StringBuffer();
//			sb.append("SELECT u.comments,u.columnName FROM UserColCommentsDo u where tableName='VMS_TRANS_INFO' ");
			infoList = transInfoService.findUserColComments(userColComments);
//			Object[] object;
//			UserColComments tid;
//			for(Object item:infoList){
//				object = (Object[]) item;
//				tid = new UserColComments();
//				String s = (String) object[0];
//				String[] ary = s.split("（");//调用API方法按照逗号分隔字符串
//				s=ary[0];
//				tid.setComments(s);
//				tid.setColumnName((String) object[1]);
//				list.add(tid);
//			}
			return FORM_BUSI_Edit;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}
	
	public BigDecimal getCastRate(BigDecimal num){
		return num.divide(new BigDecimal(100));
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getResponesForCheck() {
		return responesForCheck;
	}

	public void setResponesForCheck(String responesForCheck) {
		this.responesForCheck = responesForCheck;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List getInfoList() {
		return infoList;
	}

	public void setInfoList(List infoList) {
		this.infoList = infoList;
	}

	public UserColComments getUserColComments() {
		return userColComments;
	}

	public void setUserColComments(UserColComments userColComments) {
		this.userColComments = userColComments;
	}
	
}
