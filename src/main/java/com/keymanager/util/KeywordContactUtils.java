package com.keymanager.util;

import javax.servlet.http.HttpServletRequest;

public class KeywordContactUtils {
	public static String prepareCondition(HttpServletRequest request){
		String condition = "";
		String keyword = request.getParameter("keyword");
		String group = request.getParameter("group");
		String position = request.getParameter("position");
		String city = request.getParameter("city");
		String updateFromTime = request.getParameter("updateFromTime");
		String updateToTime = request.getParameter("updateToTime");
		if (!Utils.isNullOrEmpty(updateFromTime)){
			condition = condition + " and fLastCaijiTime >= STR_TO_DATE('" + updateFromTime.trim() + "', '%Y-%m-%d') ";
		}
		if (!Utils.isNullOrEmpty(updateToTime)){
			condition = condition + " and fLastCaijiTime <= STR_TO_DATE('" + updateToTime.trim() + "', '%Y-%m-%d') ";
		}
		
		String[] haveQq = request.getParameterValues("haveQq");
		if (haveQq != null){
			condition = condition + " AND (fQq is not null AND fQq <> '') ";
		}
		String[] haveEmail = request.getParameterValues("haveEmail");
		if (haveEmail != null){
			condition = condition + " AND (fEmail is not null AND fEmail <> '') ";
		}
		String[] haveMobile = request.getParameterValues("haveMobile");
		if (haveMobile != null){
			condition = condition + " AND (fMobile is not null AND fMobile <> '') ";
		}
		String[] havePhone = request.getParameterValues("havePhone");
		if (havePhone != null){
			condition = condition + " AND (fPhone is not null AND fPhone <> '') ";
		}
		String[] haveContactPerson = request.getParameterValues("haveContactPerson");
		if (haveContactPerson != null){
			condition = condition + " AND (fContactRen is not null AND fContactRen <> '') ";
		}
		String[] unClick = request.getParameterValues("unClick");
		if (unClick != null){
			condition = condition + " AND (fIsClick = 0) ";
		}
		String[] isDomain = request.getParameterValues("isDomain");
		if (isDomain != null){
			condition = condition + " AND (fIsDomain = 1) ";
		}
		if (!Utils.isNullOrEmpty(keyword)){
			condition = condition + " and fKeyword like '%" + keyword.trim() + "%' ";
		}
		
		if (!Utils.isNullOrEmpty(group)){
			condition = condition + " and fGroup = '" + group.trim() + "' ";
		}
		
		if (!Utils.isNullOrEmpty(city)){
			condition = condition + " and fCity = '" + city.trim() + "' ";
		}
		
		if (!Utils.isNullOrEmpty(position)){
			condition = condition + " and fPosition <= " + position.trim() + " ";
		}
		return condition;
	}
}
