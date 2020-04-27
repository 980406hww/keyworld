package com.keymanager.ckadmin.common.shiro;

import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.service.IRoleService;
import com.keymanager.monitoring.service.IUserInfoService;
import com.keymanager.monitoring.vo.ExtendedUsernamePasswordToken;
import com.keymanager.monitoring.vo.UserVO;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @description：用于外部使用shiro权限认证
 * @author：liujie
 * @date：2017-11-2 18:03:21
 */
public class ExternalControllerRealm extends AuthorizingRealm {
	private static final Logger LOGGER =  LoggerFactory.getLogger(ExternalControllerRealm.class);

	@Autowired private IUserInfoService userInfoService;
	@Autowired private IRoleService roleService;

	public ExternalControllerRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
		super(cacheManager, matcher);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
//		LOGGER.info("外部Shiro开始登录认证");
		ExtendedUsernamePasswordToken token = (ExtendedUsernamePasswordToken) authcToken;
		UserVO uservo = new UserVO();
		uservo.setLoginName(token.getUsername());
		List<UserInfo> list = userInfoService.selectByLoginName(uservo);
		// 账号不存在
		if (list == null || list.isEmpty()) {
			return null;
		}
		UserInfo user = list.get(0);
		// 账号未启用
		if (user.getStatus() == 1) {
			return null;
		}
		ShiroUser shiroUser = new ShiroUser(user.getLoginName());

		// 认证缓存信息
		return new SimpleAuthenticationInfo(shiroUser, user.getPassword().toCharArray(),
				ShiroByteSource.of(user.getSalt()), getName());
	}

	/**
	 * Shiro权限认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		return null;
	}
}
