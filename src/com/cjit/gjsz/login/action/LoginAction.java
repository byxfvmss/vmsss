/**
 * LoginAction
 */
package com.cjit.gjsz.login.action;

import java.net.URL;
import java.util.Date;
import java.util.List;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.interfacemanager.service.ChangeThemeService;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.login.util.LoginUtil;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.AuthorityService;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.gjsz.system.service.UserService;

/**
 * @author huboA
 */
public class LoginAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2734311800443893062L;
	public static final String LIMITDATE = "2010-06-30";
	private OrganizationService organizationService;
	private UserService userService;
	private AuthorityService authorityService;
	private UserInterfaceConfigService userInterfaceConfigService;
	private ChangeThemeService changeThemeService;
	private User user;
	private String ueserName = "sa";
	private String IP;
	private String password = "";
	private int operResult = 0;// 默认是0 1表示成功 2表示失败

	public int getOperResult(){
		return operResult;
	}

	public void setOperResult(int operResult){
		this.operResult = operResult;
	}

	public String getIP(){
		return IP;
	}

	public void setIP(String ip){
		IP = ip;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	/**
	 * 尝试一下是否可以连接数据库，如果能够连接则返回登录页面 如果不能连接，则输入数据库的IP地址和密码，重启服务器
	 * @return
	 */
	public String login(){
		LoginUtil loginUtil = new LoginUtil();
		URL uri = this.getClass().getResource("/config/config.properties");
		String path = uri.getFile();
		if(path.startsWith("/")){
			path = path.substring(1);
		}
		path = path.replaceAll("%20", " ");
		// /String themepath;
		// /String uprrPath;
		// HttpServletRequest request = ServletActionContext.getRequest();
		if(loginUtil.testDbExit(path)){
			userInterfaceConfigService.getAllDictionarysByCache();
			// 获得主题部分刚到拦截器中
			/*
			 * 
			themepath=changeThemeService.getCurrentTheme();
			uprrPath=changeThemeService.getCrrentWebPath();
			System.out.println("path="+themepath);
			System.out.println("uprrPath="+uprrPath);
			HttpSession session=request.getSession();
			session.setAttribute("themepath",themepath);
			session.setAttribute("uprrPath", uprrPath);
			//saveThemePath save=new saveThemePath();
			//save.savepath(themepath);
			*/
			return SUCCESS;
		}
		return "dbrestore";
	}

	/**
	 * 注销
	 * @return
	 */
	public String logout(){
		try{
			// 让HttpSession失效
			// webwork替换成struts2修改
			// ServletActionContext.getRequest().getSession().invalidate();
			this.request.getSession().invalidate();
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public String check(){
		try{
			// response.setHeader("Pragma", "No-cache");
			// response.setDateHeader("Expires", 0);
			// response.setHeader("Cache-Control", "no-cache");
			// 将Session中所有attribute清掉
			// ((SessionMap) ActionContext.getContext().getSession()).clear();
			// 让HttpSession失效
			// ServletActionContext.getRequest().getSession().invalidate();
			// 让SessionMap失效
			// if (session != null && !session.isEmpty()) {
			// ServletActionContext.getRequest().getSession().invalidate();
			// }
			user = this.userService.checkUser(user);
			if(user == null){
				this.addActionMessage("用户名或密码错误。");
				return INPUT;
			}
			// 以下是试用版有效期判断-------------------------------------------
			Date limitDate = DateUtils.stringToDate(LIMITDATE, "yyyy-MM-dd");
			Date nowDate = new Date();
			long nowDateLong = nowDate.getTime();
			long limitDateLong = limitDate.getTime();
			if(limitDateLong < nowDateLong){
				this
						.addActionMessage("试用版超过有效期.");
				return INPUT;
			}
			// 有效期判断结束-------------------------------------------
			// List authoritys = authorityService.findAuthorityByUser(user,
			// null);
			List organizations = organizationService
					.findOrganizationByUser(user);
			// ids = authorityService.findAuthorityByUser(user);
			// if(CollectionUtil.isNotEmpty(authoritys)){
			// user.setIds(ids);
			// }
			// if(CollectionUtil.isNotEmpty(authoritys)){
			// user.setAuthoritys(CollectionUtil.getNumberSQLIds(authoritys));
			user.setOrgs(organizations);
			// }
			session.put(Constants.USER, user);
			if(user.getChangePassword() == 0){
				this.addActionMessage("请修改您的登录密码。");
				return "change";
			}
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String restoreDb(){
		LoginUtil loginUtil = new LoginUtil();
		URL uri = this.getClass().getResource("/config/config.properties");
		String path = uri.toString().substring(6);
		String webRootPath = this.servletContext.getRealPath("/");
		boolean result = loginUtil.restoreDb(path, webRootPath, ueserName,
				password, IP);
		if(result){
			operResult = 1;
			return SUCCESS;
		}else{
			operResult = 2;
			return ERROR;
		}
	}

	public void setOrganizationService(OrganizationService organizationService){
		this.organizationService = organizationService;
	}

	public void setUserService(UserService userService){
		this.userService = userService;
	}

	public User getUser(){
		return user;
	}

	public void setUser(User user){
		this.user = user;
	}

	public void setAuthorityService(AuthorityService authorityService){
		this.authorityService = authorityService;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService(){
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService){
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public String getUeserName(){
		return ueserName;
	}

	public void setUeserName(String ueserName){
		this.ueserName = ueserName;
	}

	public UserService getUserService(){
		return userService;
	}

	public ChangeThemeService getChangethemeservice(){
		return changeThemeService;
	}

	public void setChangethemeservice(ChangeThemeService changethemeservice){
		this.changeThemeService = changethemeservice;
	}

	public void setChangeThemeService(ChangeThemeService changeThemeService){
		this.changeThemeService = changeThemeService;
	}
}
