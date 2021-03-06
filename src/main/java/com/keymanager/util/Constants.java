package com.keymanager.util;

import com.keymanager.monitoring.enums.TerminalTypeEnum;

import java.util.*;

public class Constants {

    public final static String OLD_PERMISSION_VERSION = "1.0";
    public final static String NEW_PERMISSION_VERSION = "2.0";

    public final static String ROW_SPLITTOR = "__row__";
    public final static String HEADER_SPLITTOR = "__header__";
    public final static String SUB_ROW_SPLITTOR = "__subrow__";
    public final static String COLUMN_SPLITTOR = "__col__";
    public final static String SERVICE_PROVIDER_BAIDUPTOP123 = "baidutop123";
    public final static int NEED_PREVIOUS_CLICK_POSITION = 5;
    public final static String ACCOUNT_LOG_STATUS_UN_PAID = "UnPaid";
    public final static String ACCOUNT_LOG_STATUS_PAID_PARTIALLY = "PaidPartially";
    public final static String ACCOUNT_LOG_STATUS_PAID_ALL = "PaidAll";
    public final static String POSITIVELIST_OPERATION_TYPE = "1";
    public final static String CUSTOMER_KEYWORD_REFRESH_STAT_INFO_CONFIG_VALUE = "8";

    public final static String QZ_SETTING_CRAWLER_STATUS_NEW = "new";
    public final static String QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD = "designationWord";

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
    // new
    public final static String CONFIG_TYPE_CAPTURE_TITLE = "CaptureTitle";

    public final static String CONFIG_TYPE_CAPTURE_RANKING = "CaptureRanking";
    public final static String CONFIG_TYPE_RANK_JOB_CITY = "RankJobCity";
    public final static String CONFIG_TYPE_WEBSITE_CHECK_SIGN = "WebsiteCheckSign";

    public final static String CONFIG_KEY_VMWARE = "VMWARE";
    public final static String CONFIG_TYPE_OPTIMIZATION_DATE = "OptimizationDate";
    public final static String CONFIG_TYPE_OPTIMIZATION_TYPE = "OperationType";
    public final static String CONFIG_TYPE_SEARCH_ENGINE = "SearchEngine";
    public final static String CONFIG_TYPE_TJ_XG = "tj_xg";
    public final static String CONFIG_TYPE_CT = "ct";
    public final static String CONFIG_TYPE_FROM_SOURCE = "src";
    public final static String CONFIG_TYPE_COUNT_PER_ELEMENT = "CountPerElement";
    public final static String CONFIG_KEY_NEGATIVE_KEYWORDS = "NegativeKeywords";
    public final static String CONFIG_TYPE_ZHANNEI_SOGOU = "zhannei_sogou";
    public final static String CONFIG_KEY_NOPOSITION_MAX_INVALID_COUNT = "NoPositionMaxInvalidCount";
    public final static String CONFIG_TYPE_START_UP = "StartUp";
    public final static String CONFIG_KEY_DOWNLOADING_CLIENT_COUNT = "DownloadingClientCount";
    public final static String CONFIG_TYPE_NEGATIVE_KEYWORD = "NegativeKeyword";
    public final static String CONFIG_KEY_BAIDU = "Baidu";
    public final static String CONFIG_KEY_SOGOU = "Sogou";
    public final static String CONFIG_KEY_360 = "360";
    public final static String CONFIG_TYPE_DAILY_REPORT = "DailyReport";
    public final static String CONFIG_TYPE_DAILY_REPORT_AUTO_TRIGGER = "AutoTrigger";
    public final static String CONFIG_TYPE_DAILY_REPORT_TYPE = "DailyReportType";
    public final static String CONFIG_TYPE_DAILY_REPORT_CUSTOMER_UUID = "DailyReportCustomerUuid";
    public final static String CONFIG_TYPE_DEFAULT_OPTIMIZE_GROUPNAME = "DefaultOptimizeGroupName";
    public final static String CONFIG_TYPE_KEYWORD_INFO_SYNCHRONIZE = "KeywordInfoSynchronize";
    public final static String CONFIG_KEY_USERNAME = "UserName";
    public final static String CONFIG_KEY_PASSWORD = "Password";
    public final static String CONFIG_KEY_INTERVAL_MINUTES = "IntervalMinutes";
    public final static String CONFIG_KEY_WEBPATH = "WebPath";
    public final static String CONFIG_TYPE_ZIP_ENCRYPTION = "ZipEncryption";
    public final static String CONFIG_TYPE_MONITOR_OPTIMIZE_GROUPNAME = "MonitorOptimizeGroupName";
    public final static String CONFIG_TYPE_NORANK_OPTIMIZE_GROUPNAME = "NoRankOptimizeGroupName";
    public final static String CONFIG_TYPE_WEBSITE_WHITE_LIST = "WebsiteWhiteList";
    public final static String CONFIG_KEY_URL = "Url";
    public final static String CONFIG_TYPE_SAME_CUSTOMER_KEYWORD_COUNT = "SameCustomerKeywordCount";
    public final static String CONFIG_KEY_CLIENT_COOKIE_COUNT = "ClientCookieCount";
    public final static String CONFIG_TYPE_COOKIE_GROUP = "CookieSwitchGroup";
    public final static String CONFIG_TYPE_VPS_BACKEND_ACCOUNT = "VPSBackendAccount";
    public final static String CONFIG_TYPE_VPS_BACKEND_PASSWORD = "VPSBackendPassword";
    public final static String CONFIG_TYPE_OPTIMIZE_METHOD_GROUPNAME = "OptimizeMethodGroupName";
    public final static String CONFIG_TYPE_QZ_KEYWORD = "QZKeyword";
    public final static String CONFIG_KEY_KEYWORD_MAX_COUNT = "MaxCount";
    public final static String CONFIG_TYPE_QZSETTING_KEYWORD_RANK = "QZSettingKeywordRank";
    public final static String CONFIG_KEY_UPPER_VALUE = "UpperValue";
    public final static String CONFIG_KEY_LOWER_VALUE = "LowerValue";
    public final static String CONFIG_KEY_ONE_WEEK_DIFF = "OneWeekDiff";
    public final static String CONFIG_KEY_DIFFERENCEVALUE_VALUE = "DifferenceValue";
    public final static String CONFIG_KEY_CRAWLER_HOUR = "CrawlerHour";
    public final static String CONFIG_TYPE_QZSETTING = "QZSetting";
    public final static String CONFIG_KEY_QZ_TASKNUMBER = "TaskNumber";
    public final static String CONFIG_TYPE_EXTERNALUSER = "ExternalUser";
    public final static String CONFIG_KEY_EXTERNALUSER = "ExternalUser";
    public final static String CONFIG_VALUE_EXTERNALUSER = "externaluser";
    public final static String CONFIG_TYPE_SCREENED_WEBSITE = "ScreenedWebsite";
    public final static String CONFIG_TYPE_NO_ENTERED_KEYWORD_SCHEDULE_SWITCH = "NoEnteredKeywordScheduleSwitch";
    public final static String CONFIG_KEY_SWITCH_NUMBER = "SwitchNumber";
    public final static String CONFIG_TYPE_MAX_INVALID_COUNT = "MaxInvalidCount";
    public final static String CONFIG_TYPE_INDUSTRY_TEL_REG = "IndustryTelReg";
    public final static String CONFIG_KEY_INDUSTRY_TEL_REG = "IndustryTelReg";
    public final static String CONFIG_TYPE_INDUSTRY_QQ_REG = "IndustryQqReg";
    public final static String CONFIG_KEY_INDUSTRY_QQ_REG = "IndustryQqReg";

    public final static String CONFIG_TYPE_SYNC_QZ_CUSTOMER_KEYWORD = "SyncQZCustomerKeyword";
    public final static String CONFIG_KEY_SYNC_QZ_CUSTOMER_TAG = "SyncQZCustomerTag";

    public final static String CONFIG_TYPE_SYNC_CUSTOMER_PT_KEYWORD_SWITCH = "SyncCustomerPtKeywordSwitch";
    public final static String CONFIG_KEY_SYNC_CUSTOMER_PT_KEYWORD = "SyncCustomerPtKeyword";

    public final static String CONFIG_TYPE_SYNC_CUSTOMER_PT_KEYWORD = "SyncCustomerPtKeyword";
    public final static String CONFIG_KEY_SYNC_CUSTOMER_NAME = "SyncCustomerName";

    public final static String CONFIG_TYPE_SYNC_PT_KEYWORD_TIME = "SyncPtKeywordTime";
    public final static String CONFIG_TYPE_SYNC_PT_OPERA_STATUS_TIME = "SyncPtOperaStatusTime";

    public final static String CONFIG_TYPE_CLEAN_TIME = "CleanTime";
    public final static String CONFIG_KEY_CLEAN_NAME = "Super";

    public final static String CONFIG_TYPE_SYNC_KEYWORD_ROW_NUMBER = "SyncKeywordRowNumber";
    public final static String CONFIG_KEY_SYNC_KEYWORD_ROW_NUMBER_NAME = "DefaultRowNumber";

    public final static String CONFIG_TYPE_KEYWORD_SUB_LIST_SIZE = "KeywordSubListSize";
    public final static String CONFIG_KEY_KEYWORD_SUB_LIST_NAME = "DefaultSize";

    public final static String CONFIG_TYPE_DEFAULT_OPTIMIZE_GROUP = "DefaultOptimizeGroup";
    public final static String CONFIG_KEY_DEFAULT_OPTIMIZE_GROUP_NAME = "DefaultOptimizeGroupName";

    public final static String CONFIG_TYPE_DEFAULT_MACHINE_GROUP = "DefaultMachineGroup";
    public final static String CONFIG_KEY_DEFAULT_MACHINE_GROUP_NAME = "DefaultMachineGroupName";

    public final static String CONFIG_TYPE_SYNC_OPERA_STATUS_SWITCH = "SyncOperaStatusSwitch";
    public final static String CONFIG_KEY_SYNC_OPERA_STATUS_SWITCH_NAME = "SyncOperaStatusSwitchName";



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
    public final static String EXCEL_TYPE_SUPER_INDUSTRY_SIMPLE = "SuperIndustrySimple";

    public final static String ALL_SEARCH_ENGINE = "All";
    public final static String SEARCH_ENGINE_BAIDU = "百度";
    public final static String SEARCH_ENGINE_SOGOU = "搜狗";
    public final static String SEARCH_ENGINE_360 = "360";
    public final static String SEARCH_ENGINE_SM = "神马";
    public final static String SEARCH_ENGINE_GOOGLE = "谷歌";
    public final static String SEARCH_ENGINE_BING = "必应";
    public final static String SEARCH_ENGINE_BING_CHINA = "必应中国";
    public final static String SEARCH_ENGINE_BING_JAPAN = "必应日本";

    public final static String DAILY_REPORT_PERCENTAGE = "DailyReportPercentage";

    public final static Map<String, String> SEARCH_ENGINE_URL_MAP = new HashMap<String, String>();

    static {
        SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_BAIDU + "_" + TerminalTypeEnum.PC.name(), "http://www.baidu.com/s?wd=");
        SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_360 + "_" + TerminalTypeEnum.PC.name(), "http://www.so.com/s?q=");
        SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_SOGOU + "_" + TerminalTypeEnum.PC.name(), "https://www.sogou.com/sie?query=");
        SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_SM + "_" + TerminalTypeEnum.PC.name(), "http://m.sm.cn/s?q=");
        SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_BING + "_" + TerminalTypeEnum.PC.name(), "http://m.sm.cn/s?q=");
        SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_GOOGLE + "_" + TerminalTypeEnum.PC.name(), "http://m.sm.cn/s?q=");

        SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_BAIDU + "_" + TerminalTypeEnum.Phone.name(), "http://m.baidu.com/s?wd=");
        SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_360 + "_" + TerminalTypeEnum.Phone.name(), "http://m.so.com/s?q=");
        SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_SOGOU + "_" + TerminalTypeEnum.Phone.name(), "https://m.sogou.com/web/searchList.jsp?keyword=");
        SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_SM + "_" + TerminalTypeEnum.Phone.name(), "http://m.sm.cn/s?q=");
        SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_BING + "_" + TerminalTypeEnum.Phone.name(), "http://m.sm.cn/s?q=");
        SEARCH_ENGINE_URL_MAP.put(SEARCH_ENGINE_GOOGLE + "_" + TerminalTypeEnum.Phone.name(), "http://m.sm.cn/s?q=");
    }

    public final static Map<String, String> SEARCH_ENGINE_MAP = new LinkedHashMap<String, String>();

    static {
        SEARCH_ENGINE_MAP.put(SEARCH_ENGINE_BAIDU, TerminalTypeEnum.PC.name());
        SEARCH_ENGINE_MAP.put(SEARCH_ENGINE_BAIDU + TerminalTypeEnum.Phone.name(), TerminalTypeEnum.Phone.name());

        SEARCH_ENGINE_MAP.put(SEARCH_ENGINE_SOGOU, TerminalTypeEnum.PC.name());
        SEARCH_ENGINE_MAP.put(SEARCH_ENGINE_SOGOU + TerminalTypeEnum.Phone.name(), TerminalTypeEnum.Phone.name());

        SEARCH_ENGINE_MAP.put(SEARCH_ENGINE_360, TerminalTypeEnum.PC.name());
        SEARCH_ENGINE_MAP.put(SEARCH_ENGINE_360 + TerminalTypeEnum.Phone.name(), TerminalTypeEnum.Phone.name());

        SEARCH_ENGINE_MAP.put(SEARCH_ENGINE_SM + TerminalTypeEnum.Phone.name(), TerminalTypeEnum.Phone.name());

        SEARCH_ENGINE_MAP.put(SEARCH_ENGINE_BING_CHINA, TerminalTypeEnum.PC.name());
        SEARCH_ENGINE_MAP.put(SEARCH_ENGINE_BING_JAPAN, TerminalTypeEnum.PC.name());

        SEARCH_ENGINE_MAP.put(SEARCH_ENGINE_GOOGLE, TerminalTypeEnum.PC.name());
    }

    public final static Map<String, String> CLIENT_STATUS_VALID_MAP = new LinkedHashMap<String, String>();

    static {
        CLIENT_STATUS_VALID_MAP.put("全部", "");
        CLIENT_STATUS_VALID_MAP.put("暂停", "0");
        CLIENT_STATUS_VALID_MAP.put("监控中", "1");
    }

    public final static Map<String, String> CLIENT_STATUS_ORDERBY_MAP = new LinkedHashMap<String, String>();

    static {
        CLIENT_STATUS_ORDERBY_MAP.put("fClientID", "ID");
        CLIENT_STATUS_ORDERBY_MAP.put("fLastVisitTime", "最后工作时间");
        CLIENT_STATUS_ORDERBY_MAP.put("fLastSendNotificationTime", "发通知时间");
        CLIENT_STATUS_ORDERBY_MAP.put("fRestartTime", "重启时间");
        CLIENT_STATUS_ORDERBY_MAP.put("fRestartOrderingTime", "重启排序时间");
        CLIENT_STATUS_ORDERBY_MAP.put("fRestartCount desc", "重启次数");
    }

    public final static List<String> QZSETTING_STATUS_LIST = new ArrayList<String>();

    static {
        QZSETTING_STATUS_LIST.add("");
        QZSETTING_STATUS_LIST.add("Processing");
        QZSETTING_STATUS_LIST.add("Completed");
        QZSETTING_STATUS_LIST.add("DownloadTimesUsed");
    }

    public final static Map<String, String> QZ_RANK_STANDARD_SPECIES_MAP = new LinkedHashMap<>();

    static {
        QZ_RANK_STANDARD_SPECIES_MAP.put("全部", "");
        QZ_RANK_STANDARD_SPECIES_MAP.put("爱站", "aiZhan");
        QZ_RANK_STANDARD_SPECIES_MAP.put("5118", "5118");
        QZ_RANK_STANDARD_SPECIES_MAP.put("指定词", "designationWord");
        QZ_RANK_STANDARD_SPECIES_MAP.put("其他", "other");
    }

    public final static Map<String, String> QZ_OPERATION_OPTIMIZATION_TYPE_MAP = new LinkedHashMap<>();

    static {
        QZ_OPERATION_OPTIMIZATION_TYPE_MAP.put("全部", "");
        QZ_OPERATION_OPTIMIZATION_TYPE_MAP.put("主优化", "1");
        QZ_OPERATION_OPTIMIZATION_TYPE_MAP.put("次优化", "2");
        QZ_OPERATION_OPTIMIZATION_TYPE_MAP.put("辅助优化", "0");
    }

    public final static List<String> QZSETTING_WEIGHT_LIST = new ArrayList<>();

    static {
        QZSETTING_WEIGHT_LIST.add("0");
        QZSETTING_WEIGHT_LIST.add("1");
        QZSETTING_WEIGHT_LIST.add("2");
        QZSETTING_WEIGHT_LIST.add("3");
        QZSETTING_WEIGHT_LIST.add("4");
        QZSETTING_WEIGHT_LIST.add("5");
        QZSETTING_WEIGHT_LIST.add("6");
        QZSETTING_WEIGHT_LIST.add("7");
        QZSETTING_WEIGHT_LIST.add("8");
        QZSETTING_WEIGHT_LIST.add("9");
    }

    public final static String CONFIG_OPERATE_KEYWORD = "OperateKeywordConfig";
}