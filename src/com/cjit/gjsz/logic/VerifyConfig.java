package com.cjit.gjsz.logic;

public interface VerifyConfig{

	public boolean verifyCustCodeOfCompany(String baseTable, String businessid);

	public boolean verifyRegno(String regno, String txcode, String txcode2,
			String txFlag);
}
