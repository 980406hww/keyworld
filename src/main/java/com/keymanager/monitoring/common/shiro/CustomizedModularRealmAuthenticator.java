package com.keymanager.monitoring.common.shiro;

import com.keymanager.monitoring.vo.ExtendedUsernamePasswordToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author LiuJie
 * @create 2017-11-02 18:45
 * @desc 自定义Authenticator
 */
public class CustomizedModularRealmAuthenticator extends ModularRealmAuthenticator{

    private Map<String, Object> definedRealms;

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 强制转换回自定义的CustomizedToken
        ExtendedUsernamePasswordToken customizedToken = (ExtendedUsernamePasswordToken) authenticationToken;
        // 登录用户类型
        String loginType = customizedToken.getEntryType();

        Realm realm = null;
        //判断是否为外不调用
        if (StringUtils.isNotEmpty(loginType)&&loginType.equals("External")) {
            realm = (Realm) this.definedRealms.get("ExternalControllerRealm");
        } else {
            realm = (Realm) this.definedRealms.get("shiroDbRealm");
        }
        return this.doSingleRealmAuthentication(realm, authenticationToken);
    }

    public Map<String, Object> getDefinedRealms() {
        return this.definedRealms;
    }

    public void setDefinedRealms(Map<String, Object> definedRealms) {
        this.definedRealms = definedRealms;
    }

}
