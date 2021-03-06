<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#editUserPwdForm').form({
            url : '${path }/user/editUserPwd',
            onSubmit : function() {
                var isValid = $(this).form('validate');
                return isValid;
            },
            success : function(result) {
                result = $.parseJSON(result);
                if (result.success) {
                    parent.$.messager.show({
                        title:'提示',
                        msg:result.msg,
                        showType:'slide',
                        timeout:2000,
                        style:{
                            right:'',
                            top:document.body.scrollTop+100,
                            bottom:''
                        }
                    });
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    $("#errorInfo").css("display","block");
                    setTimeout(function(){$("#errorInfo").css("display","none")},5000);
                }
            }
        });
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;">
            <form id="editUserPwdForm" method="post">
                <table>
                    <tr>
                        <th>登录名：</th>
                        <td><shiro:principal></shiro:principal></td>
                    </tr>
                    <tr>
                        <th>原密码：</th>
                        <td><input name="oldPwd" type="password" placeholder="请输入原密码" class="easyui-validatebox" data-options="required:true"></td>
                    </tr>
                    <tr>
                        <th>新密码：</th>
                        <td><input name="pwd" type="password" placeholder="请输入新密码" class="easyui-validatebox" data-options="required:true"></td>
                    </tr>
                    <tr>
                        <th>重复密码：</th>
                        <td><input name="rePwd" type="password" placeholder="请再次输入新密码" class="easyui-validatebox" data-options="required:true,validType:'eqPwd[\'#editUserPwdForm input[name=pwd]\']'"></td>
                    </tr>
                </table>
                <div style="display: none;color: red;font-size: 12px; height: 12px;" id="errorInfo" align="center">
                    原始密码不正确！
                </div>
            </form>
    </div>
</div>