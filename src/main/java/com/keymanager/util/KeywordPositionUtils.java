package com.keymanager.util;

import javax.servlet.http.HttpServletRequest;

public class KeywordPositionUtils {
	public static String prepareCondition(HttpServletRequest request) {
		String condition = "";
		String keyword = request.getParameter("keyword");
		String level = request.getParameter("level");
		String group = request.getParameter("group");
		String position = request.getParameter("position");
		String positionType = request.getParameter("positionType");
		String domain = request.getParameter("domain");
		if (!Utils.isNullOrEmpty(keyword)) {
			condition = condition + " and fKeyword like '%" + keyword.trim() + "%' ";
		}

		if (!Utils.isNullOrEmpty(level)) {
			condition = condition + " and fLevel = " + level.trim() + " ";
		}

		if (!Utils.isNullOrEmpty(group)) {
			condition = condition + " and fGroup = '" + group.trim() + "' ";
		}

		if (!Utils.isNullOrEmpty(domain)) {
			condition = condition + " and fDomain = '" + domain.trim() + "' ";
		}

		if (!Utils.isNullOrEmpty(positionType)) {
			if (!Utils.isNullOrEmpty(position)) {
				if (positionType.equals("")) {
					condition = condition + " and ((fPosition > 0 and fPosition <= " + position.trim() + ") or (fChupingPosition > 0 and fChupingPosition <= " + position.trim()
							+ ") or (fJisuPosition > 0 and fJisuPosition <= " + position.trim() + ")) ";
				} else if (positionType.equals("PC")) {
					condition = condition + " and (fPosition > 0 and fPosition <= " + position.trim() + " ) ";
				} else if (positionType.equals("Chuping")) {
					condition = condition + " and (fChupingPosition > 0 and fChupingPosition <= " + position.trim() + " ) ";
				} else if (positionType.equals("Jisu")) {
					condition = condition + " and (fJisuPosition > 0 and fJisuPosition <= " + position.trim() + " ) ";
				}
			}
		}
		return condition;
	}

	public static String prepareOrder(HttpServletRequest request) {
		String positionType = request.getParameter("positionType");
		String orderElement = request.getParameter("orderElement");
		String order = "";
		if ("当前排名".equals(orderElement)) {
			if (positionType.equals("")) {
				order = " order by fPosition ";
			} else if (positionType.equals("PC")) {
				order = " order by fPosition ";
			} else if (positionType.equals("Chuping")) {
				order = " order by fChupingPosition ";
			} else if (positionType.equals("Jisu")) {
				order = " order by fJisuPosition ";
			}
		} else if ("更新时间".equals(orderElement)) {
			if (positionType.equals("")) {
				order = " order by fPcUpdateTime desc ";
			} else if (positionType.equals("PC")) {
				order = " order by fPcUpdateTime desc ";
			} else {
				order = " order by fMobileUpdateTime desc ";
			}
		}
		return order;
	}
}
