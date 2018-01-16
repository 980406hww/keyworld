package com.keymanager.util;

import com.keymanager.monitoring.enums.TerminalTypeEnum;

import java.util.*;

public class Constants {
	public final static String ROW_SPLITTOR = "__row__";
	public final static String HEADER_SPLITTOR = "__header__";
	public final static String SUB_ROW_SPLITTOR = "__subrow__";
	public final static String COLUMN_SPLITTOR = "__col__";
	public final static String SERVICE_PROVIDER_BAIDUPTOP123 = "baidutop123";
	public final static int NEED_PREVIOUS_CLICK_POSITION = 5;
	public final static String ACCOUNT_LOG_STATUS_UN_PAID = "UnPaid";
	public final static String ACCOUNT_LOG_STATUS_PAID_PARTIALLY = "PaidPartially";
	public final static String ACCOUNT_LOG_STATUS_PAID_ALL = "PaidAll";

	public final static String COLLECT_METHOD_ALL = "全收";
	public final static String COLLECT_METHOD_JUSTPC = "都有排名，只收电脑";
	public final static String COLLECT_METHOD_JUSTSHOUJI = "都有排名，只收手机";
	public final static String COLLECT_METHOD_BOTHPC_AND_SHOUJI_JUSTONE = "电脑和手机都收，手机只收一个";
	
	public final static String BAIDU_TYPE_PC = "PC";
	public final static String BAIDU_TYPE_JISU = "Jisu";
	public final static String BAIDU_TYPE_CHUPING = "Chuping";

	public final static String CONFIG_TYPE_PAGE_PERCENTAGE = "PagePercentage";
	public final static String CONFIG_KEY_PAGE_PC_PERCENTAGE = "PCPercentage";
	public final static String CONFIG_KEY_PAGE_PHONE_PERCENTAGE = "PhonePercentage";
	public final static String CONFIG_KEY_MAX_INVALID_COUNT = "MaxInvalidCount";
	public final static String CONFIG_KEY_VMWARE = "VMWARE";
	public final static String CONFIG_TYPE_OPTIMIZATION_DATE = "OptimizationDate";
	public final static String CONFIG_TYPE_OPTIMIZATION_TYPE = "OperationType";
	public final static String CONFIG_TYPE_XL_TJ = "xl_tj";
	public final static String CONFIG_KEY_NEGATIVE_KEYWORDS = "NegativeKeywords";
	public final static String CONFIG_TYPE_ZHANNEI_SOGOU = "zhannei_sogou";
	public final static String CONFIG_KEY_NOPOSITION_MAX_INVALID_COUNT = "NoPositionMaxInvalidCount";
	
	public final static Map<String, String> ACCOUNT_LOG_STATUS_MAP = new HashMap<String, String>();
	static {
		ACCOUNT_LOG_STATUS_MAP.put(ACCOUNT_LOG_STATUS_UN_PAID, "未付");
		ACCOUNT_LOG_STATUS_MAP.put(ACCOUNT_LOG_STATUS_PAID_PARTIALLY, "已付部分");
		ACCOUNT_LOG_STATUS_MAP.put(ACCOUNT_LOG_STATUS_PAID_ALL, "付清");
	}	
	
	public final static String CUSTOMER_KEYWORD_PAY_STATUS_PAID_ALL = "PaidAll";
	public final static String CUSTOMER_KEYWORD_PAY_STATUS_UN_PAID = "UnPaid";
	public final static String CUSTOMER_KEYWORD_PAY_STATUS_PUSH_PAY = "PushPay";
	public final static String CUSTOMER_KEYWORD_PAY_STATUS_NO_NEED = "NoNeed";
	
	public final static String EXCEL_TYPE_SUPER_USER_SIMPLE = "SuperUserSimple";
	public final static String EXCEL_TYPE_SUPER_USER_FULL = "SuperUserFull";
	
	public final static String SEARCH_ENGINE_BAIDU = "百度";
	public final static String SEARCH_ENGINE_SOGOU = "搜狗";
	public final static String SEARCH_ENGINE_360 = "360";
	public final static String SEARCH_ENGINE_SM = "神马";
	public final static String SEARCH_ENGINE_GOOGLE = "谷歌";
	public final static Map<String, String> SEARCH_ENGINE_URL_MAP = new HashMap<String, String>();
	static {
		SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_BAIDU + "_" + TerminalTypeEnum.PC.name(), "http://www.baidu.com/s?wd=");
		SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_360 + "_" + TerminalTypeEnum.PC.name(), "http://www.so.com/s?q=");
		SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_SOGOU + "_" + TerminalTypeEnum.PC.name(), "http://www.sogou.com/web?query=");

		SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_BAIDU + "_" + TerminalTypeEnum.Phone.name(), "http://m.baidu.com/s?wd=");
		SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_360 + "_" + TerminalTypeEnum.Phone.name(), "http://www.so.com/s?q=");
		SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_SOGOU + "_" + TerminalTypeEnum.Phone.name(), "http://www.sogou.com/web?query=");
	}


	public final static Map<String, String> CLIENT_STATUS_VALID_MAP = new LinkedHashMap<String, String>();
	static {
		CLIENT_STATUS_VALID_MAP.put("全部","");
		CLIENT_STATUS_VALID_MAP.put("暂停","0");
		CLIENT_STATUS_VALID_MAP.put("监控中","1");
	}

	public final static Map<String, String> CLIENT_STATUS_ORDERBY_MAP = new LinkedHashMap<String, String>();
	static {
		CLIENT_STATUS_ORDERBY_MAP.put("fClientID","ID");
		CLIENT_STATUS_ORDERBY_MAP.put("fLastVisitTime","最后工作时间");
		CLIENT_STATUS_ORDERBY_MAP.put("fLastSendNotificationTime","发通知时间");
		CLIENT_STATUS_ORDERBY_MAP.put("fRestartTime","重启时间");
		CLIENT_STATUS_ORDERBY_MAP.put("fRestartOrderingTime","重启排序时间");
		CLIENT_STATUS_ORDERBY_MAP.put("fRestartCount desc","重启次数");
	}

	public final static List<String> QZSETTING_STATUS_LIST = new ArrayList<String>();
	static {
		QZSETTING_STATUS_LIST.add("");
		QZSETTING_STATUS_LIST.add("Processing");
		QZSETTING_STATUS_LIST.add("Completed");
		QZSETTING_STATUS_LIST.add("DownloadTimesUsed");
	}

	public final static List<String> SEARCH_STYLE_LIST = new ArrayList<String>();
	static {
		SEARCH_STYLE_LIST.add("神马");
		SEARCH_STYLE_LIST.add("搜狗电脑");
		SEARCH_STYLE_LIST.add("搜狗手机");
		SEARCH_STYLE_LIST.add("360电脑");
		SEARCH_STYLE_LIST.add("百度电脑");
		SEARCH_STYLE_LIST.add("百度手机");
	}
}
