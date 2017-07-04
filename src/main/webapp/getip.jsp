<%@page contentType="text/html;charset=utf-8"%>

<%
        String ip = request.getHeader("X-Forwarded-For");
        
        out.println("X-Forwarded-For================" + ip + "<br><br>");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        
        out.println("Proxy-Client-IP================" + request.getHeader("Proxy-Client-IP") + "<br><br>");
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        
        out.println("WL-Proxy-Client-IP================" + request.getHeader("WL-Proxy-Client-IP") + "<br><br>");
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        
        out.println("HTTP_CLIENT_IP================" + request.getHeader("HTTP_CLIENT_IP") + "<br><br>");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        
        out.println("HTTP_X_FORWARDED_FOR================" + request.getHeader("HTTP_X_FORWARDED_FOR") + "<br><br>");
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        out.println("request.getRemoteAddr()================" + request.getRemoteAddr() + "<br><br>");
        
        out.println("ip===" + ip);
%>

<script langluage="javascript">
	 alert("<%=ip%>");
</script>