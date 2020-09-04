package com.cjit.gjsz.datadeal.model;

public class RptLogInfo {

	private String logtype = "";
	private String tableid = "";
	private String filetype = "";
	private String userid = "";
	private String updatetime = "";
	private String businessno = "";
	private String businessid = "";
	private String subid = "";
	private String datastatus = "";
	private String column01 = "";
	private String column02 = "";
	private String column03 = "";
	private String column04 = "";
	private String column05 = "";
	private String column06 = "";
	private String column07 = "";
	private String column08 = "";
	private String column09 = "";
	private String column10 = "";
	private String column11 = "";
	private String column12 = "";
	private String column13 = "";
	private String column14 = "";
	private String column15 = "";
	private String column16 = "";
	private String column17 = "";
	private String column18 = "";
	private String column19 = "";
	private String column20 = "";
	private String column21 = "";
	private String column22 = "";
	private String column23 = "";
	private String column24 = "";
	private String column25 = "";
	private String column26 = "";
	private String column27 = "";
	private String column28 = "";
	private String column29 = "";
	private String column30 = "";
	private String column31 = "";
	private String column32 = "";
	private String column33 = "";
	private String column34 = "";
	private String column35 = "";
	private String column36 = "";
	private String column37 = "";
	private String column38 = "";
	private String column39 = "";
	private String column40 = "";
	private String column41 = "";
	private String column42 = "";
	private String column43 = "";
	private String column44 = "";
	private String column45 = "";
	private String column46 = "";
	private String column47 = "";
	private String column48 = "";
	private String column49 = "";
	private String column50 = "";
	private String column51 = "";
	private String column52 = "";
	private String column53 = "";
	private String column54 = "";
	private String column55 = "";
	private String column56 = "";
	private String column57 = "";
	private String column58 = "";
	private String column59 = "";
	private String column60 = "";
	private String column61 = "";
	private String column62 = "";
	private String column63 = "";
	private String column64 = "";
	private String column65 = "";
	private String column66 = "";
	private String column67 = "";
	private String column68 = "";
	private String column69 = "";
	private String column70 = "";
	private String columnm01 = "";
	private String columnm02 = "";
	private String columnm03 = "";
	private String columnm04 = "";
	private String columnm05 = "";
	private String columnm06 = "";
	private String columnm07 = "";
	private String columnm11 = "";
	private String columnm12 = "";
	private String columnm13 = "";
	private String columnm14 = "";
	private String columnm15 = "";
	private String instCode = "";
	private String orderColumn = "";
	private String orderDirection = "";
	private String updatetimeBegin = "";
	private String updatetimeEnd = "";
	private String searchCondition = "";

	public RptLogInfo() {
	}

	public RptLogInfo(String tableId, String fileType, String businessNo,
			String businessId, String subId) {
		this.tableid = tableId;
		this.filetype = fileType;
		this.businessno = businessNo;
		this.businessid = businessId;
		this.subid = subId;
	}

	public String getLogtype() {
		return logtype;
	}

	public String getLogtypeView() {
		if ("insert".equals(logtype)) {
			return "添加";
		} else if ("update".equals(logtype)) {
			return "修改";
		} else if ("delete".equals(logtype)) {
			return "删除";
		} else if ("import".equals(logtype)) {
			return "导入";
		} else if ("send".equalsIgnoreCase(logtype)) {
			return "发送报文";
		} else if ("success".equalsIgnoreCase(logtype)) {
			return "接收成功";
		} else if ("init".equalsIgnoreCase(logtype)) {
			return "初始";
		}
		return logtype;
	}

	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

	public String getTableid() {
		return tableid;
	}

	public void setTableid(String tableid) {
		this.tableid = tableid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public String getUpdatetimeView() {
		if (updatetime != null && updatetime.length() == 17) {
			String year = updatetime.substring(0, 4);
			String month = updatetime.substring(4, 6);
			String day = updatetime.substring(6, 8);
			String hour = updatetime.substring(8, 10);
			String minute = updatetime.substring(10, 12);
			String second = updatetime.substring(12, 14);
			return year + "-" + month + "-" + day + " " + hour + ":" + minute
					+ ":" + second;
		}
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getBusinessid() {
		return businessid;
	}

	public void setBusinessid(String businessid) {
		this.businessid = businessid;
	}

	public String getSubid() {
		return subid;
	}

	public void setSubid(String subid) {
		this.subid = subid;
	}

	public String getDatastatus() {
		return datastatus;
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public String getColumn01() {
		return column01 == null ? null : column01.replaceAll("'", "''");
	}

	public void setColumn01(String column01) {
		this.column01 = column01;
	}

	public String getColumn02() {
		return column02 == null ? null : column02.replaceAll("'", "''");
	}

	public void setColumn02(String column02) {
		this.column02 = column02;
	}

	public String getColumn03() {
		return column03 == null ? null : column03.replaceAll("'", "''");
	}

	public void setColumn03(String column03) {
		this.column03 = column03;
	}

	public String getColumn04() {
		return column04 == null ? null : column04.replaceAll("'", "''");
	}

	public void setColumn04(String column04) {
		this.column04 = column04;
	}

	public String getColumn05() {
		return column05 == null ? null : column05.replaceAll("'", "''");
	}

	public void setColumn05(String column05) {
		this.column05 = column05;
	}

	public String getColumn06() {
		return column06 == null ? null : column06.replaceAll("'", "''");
	}

	public void setColumn06(String column06) {
		this.column06 = column06;
	}

	public String getColumn07() {
		return column07 == null ? null : column07.replaceAll("'", "''");
	}

	public void setColumn07(String column07) {
		this.column07 = column07;
	}

	public String getColumn08() {
		return column08 == null ? null : column08.replaceAll("'", "''");
	}

	public void setColumn08(String column08) {
		this.column08 = column08;
	}

	public String getColumn09() {
		return column09 == null ? null : column09.replaceAll("'", "''");
	}

	public void setColumn09(String column09) {
		this.column09 = column09;
	}

	public String getColumn10() {
		return column10 == null ? null : column10.replaceAll("'", "''");
	}

	public void setColumn10(String column10) {
		this.column10 = column10;
	}

	public String getColumn11() {
		return column11 == null ? null : column11.replaceAll("'", "''");
	}

	public void setColumn11(String column11) {
		this.column11 = column11;
	}

	public String getColumn12() {
		return column12 == null ? null : column12.replaceAll("'", "''");
	}

	public void setColumn12(String column12) {
		this.column12 = column12;
	}

	public String getColumn13() {
		return column13 == null ? null : column13.replaceAll("'", "''");
	}

	public void setColumn13(String column13) {
		this.column13 = column13;
	}

	public String getColumn14() {
		return column14 == null ? null : column14.replaceAll("'", "''");
	}

	public void setColumn14(String column14) {
		this.column14 = column14;
	}

	public String getColumn15() {
		return column15 == null ? null : column15.replaceAll("'", "''");
	}

	public void setColumn15(String column15) {
		this.column15 = column15;
	}

	public String getColumn16() {
		return column16 == null ? null : column16.replaceAll("'", "''");
	}

	public void setColumn16(String column16) {
		this.column16 = column16;
	}

	public String getColumn17() {
		return column17 == null ? null : column17.replaceAll("'", "''");
	}

	public void setColumn17(String column17) {
		this.column17 = column17;
	}

	public String getColumn18() {
		return column18 == null ? null : column18.replaceAll("'", "''");
	}

	public void setColumn18(String column18) {
		this.column18 = column18;
	}

	public String getColumn19() {
		return column19 == null ? null : column19.replaceAll("'", "''");
	}

	public void setColumn19(String column19) {
		this.column19 = column19;
	}

	public String getColumn20() {
		return column20 == null ? null : column20.replaceAll("'", "''");
	}

	public void setColumn20(String column20) {
		this.column20 = column20;
	}

	public String getColumn21() {
		return column21 == null ? null : column21.replaceAll("'", "''");
	}

	public void setColumn21(String column21) {
		this.column21 = column21;
	}

	public String getColumn22() {
		return column22 == null ? null : column22.replaceAll("'", "''");
	}

	public void setColumn22(String column22) {
		this.column22 = column22;
	}

	public String getColumn23() {
		return column23 == null ? null : column23.replaceAll("'", "''");
	}

	public void setColumn23(String column23) {
		this.column23 = column23;
	}

	public String getColumn24() {
		return column24 == null ? null : column24.replaceAll("'", "''");
	}

	public void setColumn24(String column24) {
		this.column24 = column24;
	}

	public String getColumn25() {
		return column25 == null ? null : column25.replaceAll("'", "''");
	}

	public void setColumn25(String column25) {
		this.column25 = column25;
	}

	public String getColumn26() {
		return column26 == null ? null : column26.replaceAll("'", "''");
	}

	public void setColumn26(String column26) {
		this.column26 = column26;
	}

	public String getColumn27() {
		return column27 == null ? null : column27.replaceAll("'", "''");
	}

	public void setColumn27(String column27) {
		this.column27 = column27;
	}

	public String getColumn28() {
		return column28 == null ? null : column28.replaceAll("'", "''");
	}

	public void setColumn28(String column28) {
		this.column28 = column28;
	}

	public String getColumn29() {
		return column29 == null ? null : column29.replaceAll("'", "''");
	}

	public void setColumn29(String column29) {
		this.column29 = column29;
	}

	public String getColumn30() {
		return column30 == null ? null : column30.replaceAll("'", "''");
	}

	public void setColumn30(String column30) {
		this.column30 = column30;
	}

	public String getColumn31() {
		return column31 == null ? null : column31.replaceAll("'", "''");
	}

	public void setColumn31(String column31) {
		this.column31 = column31;
	}

	public String getColumn32() {
		return column32 == null ? null : column32.replaceAll("'", "''");
	}

	public void setColumn32(String column32) {
		this.column32 = column32;
	}

	public String getColumn33() {
		return column33 == null ? null : column33.replaceAll("'", "''");
	}

	public void setColumn33(String column33) {
		this.column33 = column33;
	}

	public String getColumn34() {
		return column34 == null ? null : column34.replaceAll("'", "''");
	}

	public void setColumn34(String column34) {
		this.column34 = column34;
	}

	public String getColumn35() {
		return column35 == null ? null : column35.replaceAll("'", "''");
	}

	public void setColumn35(String column35) {
		this.column35 = column35;
	}

	public String getColumn36() {
		return column36 == null ? null : column36.replaceAll("'", "''");
	}

	public void setColumn36(String column36) {
		this.column36 = column36;
	}

	public String getColumn37() {
		return column37 == null ? null : column37.replaceAll("'", "''");
	}

	public void setColumn37(String column37) {
		this.column37 = column37;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public String getUpdatetimeBegin() {
		return updatetimeBegin;
	}

	public void setUpdatetimeBegin(String updatetimeBegin) {
		this.updatetimeBegin = updatetimeBegin;
	}

	public String getUpdatetimeEnd() {
		return updatetimeEnd;
	}

	public void setUpdatetimeEnd(String updatetimeEnd) {
		this.updatetimeEnd = updatetimeEnd;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getColumn38() {
		return column38 == null ? null : column38.replaceAll("'", "''");
	}

	public void setColumn38(String column38) {
		this.column38 = column38;
	}

	public String getColumn39() {
		return column39 == null ? null : column39.replaceAll("'", "''");
	}

	public void setColumn39(String column39) {
		this.column39 = column39;
	}

	public String getColumn40() {
		return column40 == null ? null : column40.replaceAll("'", "''");
	}

	public void setColumn40(String column40) {
		this.column40 = column40;
	}

	public String getColumn41() {
		return column41 == null ? null : column41.replaceAll("'", "''");
	}

	public void setColumn41(String column41) {
		this.column41 = column41;
	}

	public String getColumn42() {
		return column42 == null ? null : column42.replaceAll("'", "''");
	}

	public void setColumn42(String column42) {
		this.column42 = column42;
	}

	public String getColumn43() {
		return column43 == null ? null : column43.replaceAll("'", "''");
	}

	public void setColumn43(String column43) {
		this.column43 = column43;
	}

	public String getColumn44() {
		return column44 == null ? null : column44.replaceAll("'", "''");
	}

	public void setColumn44(String column44) {
		this.column44 = column44;
	}

	public String getColumn45() {
		return column45 == null ? null : column45.replaceAll("'", "''");
	}

	public void setColumn45(String column45) {
		this.column45 = column45;
	}

	public String getColumn46() {
		return column46 == null ? null : column46.replaceAll("'", "''");
	}

	public void setColumn46(String column46) {
		this.column46 = column46;
	}

	public String getColumn47() {
		return column47 == null ? null : column47.replaceAll("'", "''");
	}

	public void setColumn47(String column47) {
		this.column47 = column47;
	}

	public String getColumn48() {
		return column48 == null ? null : column48.replaceAll("'", "''");
	}

	public void setColumn48(String column48) {
		this.column48 = column48;
	}

	public String getColumn49() {
		return column49 == null ? null : column49.replaceAll("'", "''");
	}

	public void setColumn49(String column49) {
		this.column49 = column49;
	}

	public String getColumn50() {
		return column50 == null ? null : column50.replaceAll("'", "''");
	}

	public void setColumn50(String column50) {
		this.column50 = column50;
	}

	public String getColumn51() {
		return column51 == null ? null : column51.replaceAll("'", "''");
	}

	public void setColumn51(String column51) {
		this.column51 = column51;
	}

	public String getColumn52() {
		return column52 == null ? null : column52.replaceAll("'", "''");
	}

	public void setColumn52(String column52) {
		this.column52 = column52;
	}

	public String getColumn53() {
		return column53 == null ? null : column53.replaceAll("'", "''");
	}

	public void setColumn53(String column53) {
		this.column53 = column53;
	}

	public String getColumn54() {
		return column54 == null ? null : column54.replaceAll("'", "''");
	}

	public void setColumn54(String column54) {
		this.column54 = column54;
	}

	public String getColumn55() {
		return column55 == null ? null : column55.replaceAll("'", "''");
	}

	public void setColumn55(String column55) {
		this.column55 = column55;
	}

	public String getColumn56() {
		return column56 == null ? null : column56.replaceAll("'", "''");
	}

	public void setColumn56(String column56) {
		this.column56 = column56;
	}

	public String getColumn57() {
		return column57 == null ? null : column57.replaceAll("'", "''");
	}

	public void setColumn57(String column57) {
		this.column57 = column57;
	}

	public String getColumn58() {
		return column58 == null ? null : column58.replaceAll("'", "''");
	}

	public void setColumn58(String column58) {
		this.column58 = column58;
	}

	public String getColumn59() {
		return column59 == null ? null : column59.replaceAll("'", "''");
	}

	public void setColumn59(String column59) {
		this.column59 = column59;
	}

	public String getColumn60() {
		return column60 == null ? null : column60.replaceAll("'", "''");
	}

	public void setColumn60(String column60) {
		this.column60 = column60;
	}

	public String getColumn61() {
		return column61 == null ? null : column61.replaceAll("'", "''");
	}

	public void setColumn61(String column61) {
		this.column61 = column61;
	}

	public String getColumn62() {
		return column62 == null ? null : column62.replaceAll("'", "''");
	}

	public void setColumn62(String column62) {
		this.column62 = column62;
	}

	public String getColumn63() {
		return column63 == null ? null : column63.replaceAll("'", "''");
	}

	public void setColumn63(String column63) {
		this.column63 = column63;
	}

	public String getColumn64() {
		return column64 == null ? null : column64.replaceAll("'", "''");
	}

	public void setColumn64(String column64) {
		this.column64 = column64;
	}

	public String getColumn65() {
		return column65 == null ? null : column65.replaceAll("'", "''");
	}

	public void setColumn65(String column65) {
		this.column65 = column65;
	}

	public String getColumn66() {
		return column66 == null ? null : column66.replaceAll("'", "''");
	}

	public void setColumn66(String column66) {
		this.column66 = column66;
	}

	public String getColumnm01() {
		return columnm01 == null ? null : columnm01.replaceAll("'", "''");
	}

	public void setColumnm01(String columnm01) {
		this.columnm01 = columnm01;
	}

	public String getColumnm02() {
		return columnm02 == null ? null : columnm02.replaceAll("'", "''");
	}

	public void setColumnm02(String columnm02) {
		this.columnm02 = columnm02;
	}

	public String getColumnm03() {
		return columnm03 == null ? null : columnm03.replaceAll("'", "''");
	}

	public void setColumnm03(String columnm03) {
		this.columnm03 = columnm03;
	}

	public String getColumnm04() {
		return columnm04 == null ? null : columnm04.replaceAll("'", "''");
	}

	public void setColumnm04(String columnm04) {
		this.columnm04 = columnm04;
	}

	public String getColumnm05() {
		return columnm05 == null ? null : columnm05.replaceAll("'", "''");
	}

	public void setColumnm05(String columnm05) {
		this.columnm05 = columnm05;
	}

	public String getColumnm06() {
		return columnm06;
	}

	public void setColumnm06(String columnm06) {
		this.columnm06 = columnm06;
	}

	public String getColumnm07() {
		return columnm07;
	}

	public void setColumnm07(String columnm07) {
		this.columnm07 = columnm07;
	}

	public String getColumnm11() {
		return columnm11 == null ? null : columnm11.replaceAll("'", "''");
	}

	public void setColumnm11(String columnm11) {
		this.columnm11 = columnm11;
	}

	public String getColumnm12() {
		return columnm12 == null ? null : columnm12.replaceAll("'", "''");
	}

	public void setColumnm12(String columnm12) {
		this.columnm12 = columnm12;
	}

	public String getColumnm13() {
		return columnm13 == null ? null : columnm13.replaceAll("'", "''");
	}

	public void setColumnm13(String columnm13) {
		this.columnm13 = columnm13;
	}

	public String getColumnm14() {
		return columnm14 == null ? null : columnm14.replaceAll("'", "''");
	}

	public void setColumnm14(String columnm14) {
		this.columnm14 = columnm14;
	}

	public String getColumnm15() {
		return columnm15 == null ? null : columnm15.replaceAll("'", "''");
	}

	public void setColumnm15(String columnm15) {
		this.columnm15 = columnm15;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getBusinessno() {
		return businessno == null ? "" : businessno.trim();
	}

	public void setBusinessno(String businessno) {
		this.businessno = businessno;
	}

	public String getColumn67() {
		return column67 == null ? null : column67.replaceAll("'", "''");
	}

	public void setColumn67(String column67) {
		this.column67 = column67;
	}

	public String getColumn68() {
		return column68 == null ? null : column68.replaceAll("'", "''");
	}

	public void setColumn68(String column68) {
		this.column68 = column68;
	}

	public String getColumn69() {
		return column69 == null ? null : column69.replaceAll("'", "''");
	}

	public void setColumn69(String column69) {
		this.column69 = column69;
	}

	public String getColumn70() {
		return column70 == null ? null : column70.replaceAll("'", "''");
	}

	public void setColumn70(String column70) {
		this.column70 = column70;
	}

}
