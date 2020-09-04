package com.cjit.common.constant;

public interface Constants{

	public static final String USER = "currentUser";
	public static final String USER_LOGINID = "loginId";
	public static final String SYSTEM_ID = "00801";
	public static final String MENU_ID = "0010.0010";
	public static final String STRING_TYPE = "字符型";
	public static final String NUMBER_TYPE = "数值型";
	public static final String DATE_TYPE = "日期型";
	
	
	//西安添加
	/** LOGIN_USER 登陆用户key */
	public static final String LOGIN_USER = "LOGIN_USER";
	/** BASE_SYS_LOG_LOGIN 用户登录日志 */
	public static final String BASE_SYS_LOG_LOGIN = "00001";
	/** BASE_SYS_LOG_BASEINFO 基本信息操作日志 */
	public static final String BASE_SYS_LOG_BASEINFO = "00002";
	/** BASE_SYS_LOG_AUTHORITY 权限操作日志 */
	public static final String BASE_SYS_LOG_AUTHORITY = "00003";
	/** SYSTEM_COMMON_ID 系统管理ID 作取系统公共资源用,写死 */
	public static final String SYSTEM_COMMON_ID = "00003";
	/** 系统菜单表名 */
	public static final String SYSTEM_MENU_TABLE = "u_base_menu";
	/** 系统机构表名 */
	public static final String SYSTEM_INST_TABLE = "u_base_inst";
	/** 资源类型-公有 */
	public static final String PUB_RESOURCE_DIC_TYPE = "PUB";
	/** 资源类型-私有 */
	public static final String PRI_RESOURCE_DIC_TYPE = "PRI";
	/** 参数缓存名 */
	public static final String PAPAMETER_CACHE_MAP = "PAPAMETER_CACHE_MAP";
	/** 注销信息提示 */
	public static final String PARAM_SYS_LOGOUT_INFO = "PARAM_SYS_LOGOUT_INFO";
	/** 主题参数路径名 */
	public static final String PARAM_THEME_PATH_CONFIG = "PARAM_SYS_THEME_PATH";
	/** 帮助文件路径 */
	public static final String PARAM_SYS_HELPFILE_PATH = "PARAM_SYS_HELPFILE_PATH";
	/** 登录是否验证IP */
	public static final String PARAM_SYS_LOGIN_IP_VALIDATE = "PARAM_SYS_LOGIN_IP_VALIDATE";
	/** 用户修改是否审核 */
	public static final String PARAM_SYS_USER_CHANGE_AUDIT = "PARAM_SYS_USER_CHANGE_AUDIT";
	/** 用户是否逻辑删除 */
	public static final String PARAM_SYS_USER_DELETE_LOGIC = "PARAM_SYS_USER_DELETE_LOGIC";
	/** 启用密码策略 */
	public static final String PARAM_SYS_ISUSE = "PARAM_SYS_ISUSE";
	/** 登陆页文件提示开启 */
	public static final String PARAM_SYS_LOGIN_PAGE_TIPS = "PARAM_SYS_LOGIN_PAGE_TIPS";
	/** 登陆页大logo */
	public static final String PARAM_SYS_BIG_LOGO_PIC_NAME = "PARAM_SYS_BIG_LOGO_PIC_NAME";
	/** 首页小logo */
	public static final String PARAM_SYS_SMALL_LOGO_PIC_NAME = "PARAM_SYS_SMALL_LOGO_PIC_NAME";
	/**登录页显示文字*/
	public static final String PARAM_SYS_TEXT_VALUE = "PARAM_SYS_TEXT_VALUE";
	/** 每天输错密码次数 */
	public static final String PARAM_SYS_ERROR_NUM = "PARAM_SYS_ERROR_NUM";
	/** 用户失效天数 */
	public static final String PARAM_SYS_USER_OVERDUE_DAYS = "PARAM_SYS_USER_OVERDUE_DAYS";
	/** 是否定时对失效用户进行锁定 */
	public static final String PARAM_SYS_TIMING_USER_UPDATE = "PARAM_SYS_TIMING_USER_UPDATE";
	/** 是否开启用户新增审核 */
	public static final String PARAM_SYS_USER_ADD_AUDIT = "PARAM_SYS_USER_ADD_AUDIT";
	/** 是否开启机构审核 */
	public static final String PARAM_SYS_INST_AUDIT = "PARAM_SYS_INST_AUDIT";
	/** 是否开启参数配置审核 */
	public static final String PARAM_SYS_PARAM_CONFIG_AUDIT = "PARAM_SYS_PARAM_CONFIG_AUDIT";
	/** 是否开启子系统信息配置审核 */
	public static final String PARAM_SYS_SUBSYSTEM_INFO_CONFIG_AUDIT = "PARAM_SYS_SUBSYSTEM_INFO_CONFIG_AUDIT";
	
	public static final String PARAM_SYS_INVAL_DAYS = "PARAM_SYS_INVAL_DAYS";
	/**密码失效提醒时间频度*/
	public static final String PARAM_SYS_PWD_REMIND_TIME ="PARAM_SYS_PWD_REMIND_TIME";
	
	public static final String PARAM_SYS_INITPWD = "PARAM_SYS_INITPWD";
	
	public static final String PARAM_SYS_ALLOW_SAMEUSER_LOGIN = "PARAM_SYS_ALLOW_SAMEUSER_LOGIN";
	/**【权限变更是否全部可见】-德意志提出的变态要求，给谁配置了【权限变更查看】功能，谁就可以看到全部变更的信息*/
	public static final String PARAM_SYS_USER_CHANGE_SHOW_ALL = "PARAM_SYS_USER_CHANGE_SHOW_ALL";
	/**0034901: 20120210-北京银行-首页中用户信息改为通过系统参数控制是否显示（参数设置为显示时，显示用户信息；否则不显示）。*/
	public static final String PARAM_SYS_USER_INFO_SHOW = "PARAM_SYS_USER_INFO_SHOW";
	
	/** 菜单资源id，写死 */
	public static final int MENU_RES_ID = 34;
	/** 机构资源id，写死 */
	public static final int INST_RES_ID = 35;
	/** 系统当前的主题存在session中的key */
	public static final String SESSION_THEME_KEY = "sysTheme";
	/** 匹配登录名称正则表达式字符串形式 */
	public static final String PATTERN_NAME = "^\\w*$";
	/** 匹配登录密码正则表达式字符串形式 */
	public static final String PATTERN_PWD = "^[\\w!@#$]*$";
	
	/** 字符 NO */
	public static final String STR_NO = "NO";	
	/** 字符 YES */
	public static final String STR_YES = "YES";  
	public static final String SESSION_MENU_LANGUAGE = "language";
	
	/** 所有子系统要响应该请求返回如下格式的jdbc连接信息
	 *  {"dbDriver":"com.microsoft.sqlserver.jdbc.SQLServerDriver","dbPassword":"sa","dbUrl":"jdbc:sqlserver://bj-sa-guojz:1443;database=METABASE;","dbUserName":"sa","hibernateDialect":""}*/
	public static final String DATASOURCE_CONFIG_URL = "dataSourceConfig.ajax";
	/** 操作成功 */
	public static final String OPERATION_SUCC = "1";
	/** 操作失败 */
	public static final String OPERATION_FAIL = "0";

	/**默认URL IP地址占位符*/
	public static final String DEFAULT_URL_IP = "$address$";
	public static final String DEFAULT_URL_IP_UNICODE = "\\u0024address\\u0024";

	/**默认URL 端口占位符*/
	public static final String DEFAULT_URL_PORT = "$port$";
	public static final String DEFAULT_URL_PORT_UNICODE = "\\u0024port\\u0024";

	public static final String URL_PORT = "port";
	public static final String URL_IP = "ipAddress";
	
	public static final int SESSION_NORMAL = 0;
	public static final int SESSION_TIME_OUT = -2;
	public static final int SESSION_MUTIL_LOGIN = -1;
	
	public static final String PARAM_SYS_LANGUAGE_CHANGE = "PARAM_SYS_LANGUAGE_CHANGE";//是否开启语言国际化
	public static final String PARAM_ADMIN_LOGIN = "PARAM_ADMIN_LOGIN";//是否admin免验证直接登陆
	
	
	
	
	public static final String PARAM_WELCOME_STYLE = "PARAM_WELCOME_STYLE";//欢迎图片上的文字样式
	public static final String PARAM_TIME_OUT = "PARAM_TIME_OUT";//登陆超时时间(单位秒)
	public static final String PARAM_REFRESH_CACHE = "PARAM_REFRESH_CACHE";//刷新系统缓存时间间隔(集群/单位秒)
	public static final String PARAM_COLONY= "PARAM_COLONY";//是否使用集群
	public static final String PARAM_CLEAR_USER= "PARAM_CLEAR_USER";//清除在线用户的任务执行时间(hh:mm)
	public static final String PARAM_KICKOUT= "PARAM_KICKOUT";//是否可以被动注销
	public static final String PARAM_KICKOUT_TIME = "PARAM_KICKOUT_TIME";//被动注销时间(单位秒)
	
	public static final String PARAM_USYS_INNER_URL = "PARAM_USYS_INNER_URL";//USYS内网地址
}
