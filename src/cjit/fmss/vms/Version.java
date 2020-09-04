package cjit.fmss.vms;

/**
 * 系统版本号标记类
 * @作者: lihaiboA
 * @日期: 2012-9-11 11:00:24 AM
 */
public class Version{

	// http://localhost:8086/vms/homeNoteData.action?type=getVersion
	private static final String version = "vms_20150804";

	public static String getVersion(){
		return version;
	}
}
