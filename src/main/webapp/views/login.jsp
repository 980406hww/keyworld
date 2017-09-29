<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<%@ include file="/commons/global.jsp" %>
<head>
    <title>用户登录</title>
    <meta name="viewport" content="width=device-width">
    <%@ include file="/commons/basejs.jsp" %>
    <script language="javascript" type="text/javascript" src="/toastmessage/jquery.toastmessage.js"></script>
    <link rel="stylesheet" href="/toastmessage/css/jquery.toastmessage.css">
    <link rel="stylesheet" type="text/css" href="${staticPath }/static/style/css/login.css?v=201612202107" />
    <script type="text/javascript" src="${staticPath }/static/login.js?v=20170115" charset="utf-8"></script>
</head>
<body onkeydown="enterlogin();">
<div class="top_div"></div>
<div style="background: rgb(255, 255, 255); margin: -100px auto auto; border: 1px solid rgb(231, 231, 231);border-image:none;width:400px;text-align: center;">
    <form method="post" id="loginform">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div style="width: 165px; height: 96px; position: absolute;">
            <div class="tou"></div>
            <div class="initial_left_hand" id="left_hand"></div>
            <div class="initial_right_hand" id="right_hand"></div>
        </div>
        <P style="padding: 30px 0px 10px; position: relative;">
            <span class="u_logo"></span>
            <input class="ipt" type="text" name="username" placeholder="请输入登录名"/>
        </P>
        <P style="position: relative;">
            <span class="p_logo"></span>
            <input class="ipt" id="password" type="password" name="password" placeholder="请输入密码"/>
        </P>
        <P style="padding: 10px 0px 10px; position: relative;">
            <input class="captcha" type="text" name="captcha" placeholder="请输入验证码"/>
            <img id="captcha" alt="验证码" src="${path }/captcha" data-src="${path }/captcha?t=" style="vertical-align:middle;border-radius:4px;width:94.5px;height:35px;cursor:pointer;">
        </P>
        <span style="position: relative;float: left;margin-left: 33px;">
            <select id="entryType" name="entryType" style="width: 120px;height:20px;padding:0px;" class="ipt">
                <option value="qz">全站链接</option>
                <option value="pt">普通链接</option>
                <option value="fm">负面链接</option>
                <option value="bc">其他</option>
            </select>
        </span>
        <span style="position: relative;text-align: left;margin-right:120px; margin-top: 2px">
            <input class="rememberMe" type="checkbox" name="rememberMe" id="rememberMe" value="1" checked style="vertical-align:middle;height:20px;display: none"/>
            <label for="rememberMe"></label><span style="font-size: 14px;position: relative;top:-4px;left:5px">记住密码</span>
        </span>
        <div style="height: 50px; line-height: 50px; margin-top: 10px;border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
            <P style="margin: 0px 35px 20px 45px;">
                <span style="float: left;">
                    <a style="color: rgb(204, 204, 204);" href="javascript:;">忘记密码?</a>
                </span>
                <span style="float: right;">
                    <a style="color: rgb(204, 204, 204); margin-right: 10px;" href="javascript:;">注册</a>
                    <a style="background: rgb(0, 142, 173); padding: 7px 10px; border-radius: 4px; border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255); font-weight: bold;" href="javascript:;" onclick="submitForm()">登录</a>
                </span>
            </P>
        </div>
    </form>
</div>
<div style="text-align:center;">
</div>
</body>
</html>
