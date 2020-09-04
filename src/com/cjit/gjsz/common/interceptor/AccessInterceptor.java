package com.cjit.gjsz.common.interceptor;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import sun.misc.BASE64Decoder;
import cjit.fmss.webservice.UrlConnectService;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.AuthorityService;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.gjsz.system.service.UserService;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 类说明:访问控制
 * 
 * @author E-mail:
 * @version
 * @since 2008-6-30 下午10:32:14
 */
public class AccessInterceptor implements Interceptor {

	/** ENCODING 编码格式 */
	private static final String ENCODING = "utf-8";
	private static final String THEME_PATH = "themepath";
	private static final String GLOBAL_JS_PATH = "globalpath";
	/** 用户service */
	private UserService userService;
	/** 权限service */
	private AuthorityService authorityService;
	/** 机构service */
	private OrganizationService organizationService;
	// private ChangeThemeService changeThemeService;
	/**
	 * 
	 */
	private static final long serialVersionUID = -6493330665665134064L;

	public String intercept(ActionInvocation invocation) throws Exception {
		Map session = invocation.getInvocationContext().getSession();
		HttpServletRequest request = ServletActionContext.getRequest();
		// 设置主题对象
		setThemeObject(request);
		if (needCheckSession(request.getRequestURI())) {
			// 从session取用户信息
			this.doUnifiedLogin(session);
		}
		String strReturn = null;
		try {
			strReturn = invocation.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strReturn;
	}

	private void setThemeObject(HttpServletRequest request) throws Exception {
		String uprrTheme = request.getParameter("uprrTheme");
		if (!StringUtils.isEmpty(uprrTheme)
				&& !StringUtils.isEmpty(request.getParameter("parentUrl"))) {
			String themepath = this.decode(request.getParameter("uprrTheme"),
					ENCODING);
			themepath = themepath.substring(1, themepath.length() - 1);
			String uprrPath = this.decode(request.getParameter("parentUrl"),
					ENCODING);
			String theme_path = uprrPath + themepath;
			request.getSession().getServletContext().setAttribute(THEME_PATH,
					theme_path);
			String global_js_path = uprrPath
					.substring(0, uprrPath.length() - 1);
			request.getSession().getServletContext().setAttribute(
					GLOBAL_JS_PATH, global_js_path);
		}
	}

	private boolean needCheckSession(String path) {
		File pathfile = new File(path);
		String p_path = null;
		if (pathfile.getParent() != null) {
			p_path = pathfile.getParent().replace('\\', '/');
		}
		if (path.endsWith("restoreDb.action")) {
			return false;
		}
		// all except AuthenticateAction.action need
		if (path.endsWith("/login.action") || path.endsWith("/check.action")
				|| path.endsWith("/page/errorpage.jsp")
				|| path.endsWith("/page/login.jsp")
				|| path.endsWith("logout.action")) {
			return false;
		}
		if (path.endsWith(".action") || path.endsWith(".jsp")
				|| path.endsWith(".ajax")) {
			return true;
		}
		// all root file don't need
		if (p_path == null || p_path.equals("/gjsz") || p_path.equals("/"))
			return false;
		return false;
	}

	/**
	 * <p>
	 * 方法名称: doUnifiedLogin|描述:处理统一登陆
	 * </p>
	 * 
	 * @param session
	 * @throws Exception
	 */
	private void doUnifiedLogin(Map session) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		// session中无该用户，则从平台取值判断
		boolean isUprrRequest = !StringUtils.isEmpty(request
				.getParameter("loginId"))
				&& !StringUtils.isEmpty(request.getParameter("userId"))
				&& !StringUtils.isEmpty(request.getParameter("parentUrl"));
		if (!isUprrRequest) {
			return;
		}
		String loginId = this.decode(request.getParameter("loginId"), ENCODING);
		String userId = this.decode(request.getParameter("userId"), ENCODING);
		String parentUrl = this.decode(request.getParameter("parentUrl"),
				ENCODING);
		// String uprrTheme = this.decode(request.getParameter("uprrTheme"),
		// ENCODING);
		// String uprrIP = "";
		// 已经登录
		if (session.get(Constants.USER) != null) {
			User user = (User) session.get(Constants.USER);
			if (null != user) {
				// UPRR请求用户和session中的用户相同,直接返回, 否则重新设置Session
				if (loginId != null && !"".equals(loginId)
						&& loginId.equals(session.get(Constants.USER_LOGINID))
						&& userId.equals(user.getId())) {
					return;
				} else {
					session.remove(Constants.USER);
					session.remove(Constants.USER_LOGINID);
					session.remove(ScopeConstants.CURRENT_INST_CODE);// 清除session中当前机构信息
				}
			}
		}
		// if(loginId != null && loginId.length() > 0){
		// uprrIP = loginId.substring(0, loginId.indexOf("-"));
		// }
		// 当两个ip相等时
		// if (uprrIP.equals(request.getRemoteAddr())) {
		UrlConnectService urlService = new UrlConnectService();
		// 访问平台url
		parentUrl = this.decode(request.getParameter("parentUrl"), ENCODING);
		String result = urlService.getResult(parentUrl
				+ "checkLogin.ajax?loginId=" + loginId);
		// 用户已登录uprr系统时
		if (!("Err1000".equals(result))) {
			User user = new User();
			// String userId = this.decode(request.getParameter("userId"),
			// ENCODING);
			user.setId(userId);
			// 取用户详细信息
			user = userService.getUser(user);
			// 暂时判断子系统与平台是否存在这同一用户
			if (user == null) {
				System.out.println("子系统不存在该用户");
				response.sendRedirect(parentUrl
						+ "messageDisplay.ajax?mes=1002");
			} else {
				// 把用户信息放入子系统session
				// List authoritys = authorityService.findAuthorityByUser(user,
				// null);
				List organizations = organizationService
						.findOrganizationByUser(user);
				// List ids = authorityService.findAuthorityByUser(user);
				// user.setIds(ids);
				// user.setAuthoritys(CollectionUtil.getNumberSQLIds(authoritys));
				user.setOrgs(organizations);
				session.put(Constants.USER, user);
				session.put(Constants.USER_LOGINID, loginId);
			}
		} else {
			System.out.println("用户没有登陆uprr系统");
			response.sendRedirect(parentUrl + "messageDisplay.ajax?mes=1000");
		}
		// } else {
		// System.out.println("IP验证失败，IP不一致");
		// response.sendRedirect(parentUrl + "messageDisplay.ajax?mes=1001");
		// }
	}

	/**
	 * <p>
	 * 方法名称: decode|描述:解码
	 * </p>
	 * 
	 * @param value
	 *            待解码的值
	 * @param encoding
	 *            编码
	 * @return 解码的值
	 * @throws Exception
	 */
	private String decode(String value, String encoding) throws Exception {
		BASE64Decoder deCoder = new BASE64Decoder();
		return new String(deCoder.decodeBuffer(java.net.URLDecoder.decode(
				value, encoding)));
	}

	public void destroy() {
	}

	public void init() {
	}

	/**
	 * @return userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService
	 *            要设置的 userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @return authorityService
	 */
	public AuthorityService getAuthorityService() {
		return authorityService;
	}

	/**
	 * @param authorityService
	 *            要设置的 authorityService
	 */
	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}

	/**
	 * @return organizationService
	 */
	public OrganizationService getOrganizationService() {
		return organizationService;
	}

	/**
	 * @param organizationService
	 *            要设置的 organizationService
	 */
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
	// public void setChangeThemeService(ChangeThemeService changeThemeService){
	// this.changeThemeService = changeThemeService;
	// }
}
