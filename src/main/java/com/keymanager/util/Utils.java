package com.keymanager.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	public static String TIME_FORMAT = "yyyy-MM-dd HH:mm";

	public static String DATE_FORMAT = "yyyy-MM-dd";

	public static boolean isPower(int number){
		if (number == 1) {
			return true;
		} else {
			do {
				if (number % 2 == 0) {
					number = number / 2;
				} else {
					return false;
				}
			} while (number != 1);
			return true;
		}
	}

	public static long getIntervalMines(String str) {
		//时间处理类
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long currentTime = System.currentTimeMillis();
		try {
			long time = sdf.parse(str).getTime();
			return (currentTime - time) / 1000 / 60;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getIntervalDays(Date compareDate, Date date) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(compareDate);

		int compareDateDay = calendar.get(Calendar.DAY_OF_YEAR);

		calendar.setTime(date);

		int dateDays = calendar.get(Calendar.DAY_OF_YEAR);

		return dateDays - compareDateDay;

	}

	public static int getTwoDateIntervalDays(Date compareDate, Date date) {

		long compareDateTime = compareDate.getTime();

		long time = date.getTime();

		return (int) ((time - compareDateTime) / (1000 * 60 * 60 * 24));
	}

	public static int getApartDayNum(String startTime, String endTime) {
		// 返回相隔天数
		int days = 0;
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		try {
			Date start = dateFormat.parse(startTime);
			Date end = dateFormat.parse(endTime);

			Calendar tempStart = Calendar.getInstance();
			tempStart.setTime(start);

			Calendar tempEnd = Calendar.getInstance();
			tempEnd.setTime(end);

			// 不包含结束
			while (tempStart.before(tempEnd)) {
				days++;
				tempStart.add(Calendar.DAY_OF_YEAR, 1);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return days;

	}

	public static List<String> getDays(String startTime, String endTime, String pattern) {
		// 返回的日期集合
		List<String> days = new ArrayList<>();

		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		DateFormat sdf = new SimpleDateFormat(pattern);
		try {
			Date start = dateFormat.parse(startTime);
			Date end = dateFormat.parse(endTime);

			Calendar tempStart = Calendar.getInstance();
			tempStart.setTime(start);

			Calendar tempEnd = Calendar.getInstance();
			tempEnd.setTime(end);

			// 不包含结束
			while (tempStart.before(tempEnd)) {
				days.add(sdf.format(tempStart.getTime()));
				tempStart.add(Calendar.DAY_OF_YEAR, 1);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return days;
	}

	public static Integer getCurrentHour() {
		Calendar calendar = Calendar.getInstance();
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		return hour;
	}

	public static int getDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return day;
	}

	public static String parseParam(String param) {
		String tmpParam = "";
		if ((param == null) || (param.equals(""))) {
			tmpParam = "";
		} else if (param.split(" ").length > 0) {
			tmpParam = param.split(" ")[0];
		} else {
			tmpParam = param;
		}

		return tmpParam;
	}

	public static boolean isSameDate(Timestamp date1, Timestamp date2) {
		if (date1 != null && date2 != null) {
			return formatDatetime(date1, "yyyy-MM-dd").equals(formatDatetime(date2, "yyyy-MM-dd"));
		}
		return date1 == date2;
	}

	public static boolean isSameStr(String value1, String value2){
		if(value1 != null && value2 != null){
			return value1.equals(value2);
		}
		return value1 == value2;
	}

	public static String formatDatetime(Timestamp date, String format) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String formatDate(Date date, String format) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static Timestamp getCurrentTimestamp() {
		Calendar now = Calendar.getInstance();
		Timestamp times = new Timestamp(now.getTimeInMillis());
		return times;
	}

	public static String removeDoubleZeros(Double value) {
		if(value == null) {
			return "";
		}
		return ((value - Math.round(value)) == 0) ? Math.round(value) + "" : value + "";
	}

	public static String formatDouble(double value) {
		DecimalFormat df2 = new DecimalFormat("####.00");
		return df2.format(value);
	}

	public static Timestamp addMonth(Timestamp date, int month) {
		Timestamp tmpDate = new Timestamp(date.getTime());
		tmpDate.setMonth(date.getMonth() + month);
		return tmpDate;
	}

	public static Timestamp yesterday() {
		return addDay(getCurrentTimestamp(), -1);
	}

	public static Timestamp addDay(Timestamp date, int day) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, day);
		Timestamp tmpDate = new Timestamp(calendar.getTimeInMillis());
		return tmpDate;
	}

	public static Timestamp addMinutes(Timestamp date, int minutes) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.MINUTE, minutes);
		Timestamp tmpDate = new Timestamp(calendar.getTimeInMillis());
		return tmpDate;
	}

	public static String getCurrentDate() {
		String currentDate = formatDatetime(getCurrentTimestamp(), "yyyy-MM-dd");
		return currentDate;
	}

	public static String getPageInfo(int iCurPage, int iPageSize, int recordCount, int pageCount, String condition, String fileName) {
		int iPos = fileName.indexOf("?");
		if (iPos == -1) {
			fileName = fileName + "?fz=1";
		}

		int firstPage = 0;
		int lastPage = 0;

		String numPageInfo = "";

		if (iCurPage <= 4) {
			firstPage = 1;
			if (pageCount >= 8) {
				lastPage = 8;
			} else {
				lastPage = pageCount;
			}

		} else if (pageCount <= 8) {
			firstPage = 1;
			lastPage = pageCount;
		} else {
			lastPage = iCurPage + 4;
			if (lastPage < pageCount) {
				firstPage = iCurPage - 3;
			} else {
				lastPage = pageCount;
				firstPage = lastPage - 7;
				if (firstPage < 1) {
					firstPage = 1;
				}
			}

		}

		for (int i = firstPage; i <= lastPage; i++) {
			if (i == iCurPage) {
				numPageInfo = numPageInfo + " <a href='javascript:void(0)' class='pion'>" + new Integer(i).toString() + "</a>";
			} else {
				numPageInfo = numPageInfo + " <a class='pioff' href='" + fileName;
				numPageInfo = numPageInfo + "&pg=" + i + "&ps=" + iPageSize + "&cd=" + condition + "'>";
				numPageInfo = numPageInfo + new Integer(i).toString();
				numPageInfo = numPageInfo + "</a>";
			}
		}

		String pageInfo = "<a href='javascript:void(0);' class='pion' style='padding-left:10;padding-right:10;'>共" + recordCount + "条</a>";
		if (recordCount > iPageSize) {
			if (pageCount < iPageSize) {
				pageInfo = pageInfo + numPageInfo;
			} else if (iCurPage <= 1) {
				pageInfo = pageInfo + numPageInfo;
				pageInfo = pageInfo + " <a href='" + fileName + "&pg=" + (iCurPage + 1) + "&ps=" + iPageSize + "&cd=" + condition + "' class='pioff'>下一页</a>";
			} else if (iCurPage == pageCount) {
				pageInfo = pageInfo + " <a href='" + fileName + "&pg=" + (iCurPage - 1) + "&ps=" + iPageSize + "&cd=" + condition + "' class='pioff'>上一页</a>";
				pageInfo = pageInfo + numPageInfo;
			} else {
				pageInfo = pageInfo + " <a href='" + fileName + "&pg=" + (iCurPage - 1) + "&ps=" + iPageSize + "&cd=" + condition + "' class='pioff'>上一页</a>";
				pageInfo = pageInfo + numPageInfo;
				pageInfo = pageInfo + " <a href='" + fileName + "&pg=" + (iCurPage + 1) + "&ps=" + iPageSize + "&cd=" + condition + "' class='pioff'>下一页</a>";
			}

		}

		return pageInfo;
	}

	public static Timestamp string2Timestamp(String times) {
		return parseDate(times);
	}

	private static Timestamp parseDate(String times) {
		if (Utils.isNullOrEmpty(times)) {
			return null;
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setLenient(false);
		try {
			Timestamp ts = new Timestamp(format.parse(times).getTime());
			return ts;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Timestamp parseDate(String times, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setLenient(false);
		try {
			Timestamp ts = new Timestamp(dateFormat.parse(times).getTime());
			return ts;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String trimdot(String keyword) {
		if ((keyword == null) || (keyword.equals(""))) {
			return "";
		}

		String key = keyword.trim();
		//
		// int beginIndex = 0;
		// for (int i = 0; i < key.length(); i++)
		// {
		// if (key.subSequence(i, i + 1).equals("."))
		// {
		// if (i != key.length() - 1)
		// continue;
		// beginIndex = key.length() - 1;
		// }
		// else
		// {
		// beginIndex = i;
		// break;
		// }
		// }
		//
		// if (beginIndex == key.length() - 1)
		// {
		// return "";
		// }
		//
		// key = key.substring(beginIndex);
		//
		// int endIndex = key.length();
		//
		// for (int i = key.length(); i > 0; i--)
		// {
		// if (key.subSequence(i - 1, i).equals("."))
		// {
		// if (i != 0)
		// continue;
		// endIndex = 0;
		// }
		// else
		// {
		// endIndex = i;
		// break;
		// }
		// }
		//
		// if (endIndex == 0)
		// {
		// return "";
		// }
		//
		// key = key.substring(0, endIndex);

		return key;
	}

	public static boolean isEmpty(List list) {
		return (list == null || list.size() == 0);
	}

	public static boolean isNullOrEmpty(String str) {
		return (str == null || "".equals(str.trim()));
	}

	public static int prepareBaiduPageNumber(int value) {
		int tmpValue = (value > 0 ? (value - 1) : 0);
		String valueString = tmpValue + "";
		String lastDigit = valueString.substring(valueString.length() - 1);
		return tmpValue - Integer.parseInt(lastDigit);
	}

	public static int calculateOptimizePlanCount(String pagePercentageSetting, int currentIndexCount, int position, int percentage) {
		PagePercentage pagePercentage = PagePercentage.fetchPagePercentage(pagePercentageSetting, 0);
		if (percentage == 0) {
			PagePercentage tmpPagePercentage = PagePercentage.fetchPagePercentage(pagePercentageSetting, (int) Math.round((position / 10.0 + 0.5)));
			if (tmpPagePercentage != null) {
				pagePercentage = tmpPagePercentage;
			}
			int randomValue = Integer.parseInt(Math.round(Math.random() * 100) + "");
			percentage = pagePercentage.getMinPercentage() + randomValue % (pagePercentage.getMaxPercentage() - pagePercentage.getMinPercentage());
		}
		int planCount = (currentIndexCount * percentage) / 100;
		return (planCount > pagePercentage.getMinDefaultValue()) ? planCount : (pagePercentage.getMinDefaultValue() + Integer.parseInt(Math.round(Math.random()
				* (pagePercentage.getMaxDefaultValue() - pagePercentage.getMinDefaultValue()))
				+ ""));
	}

	public static String splitWithComma(String keyword) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keyword.length(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(keyword.substring(i, i + 1));
		}
		return sb.toString();
	}

	public static String trimValue(String value) {
		return value != null ? value.trim() : "";
	}

	public static void removeDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {// 判断目录是否存在
			deleteAll(dir);
		}
	}

	public static void deleteAll(File file) {
		if (file.isFile() || file.list().length == 0) {
			file.delete();
		} else {
			for (File f : file.listFiles()) {
				deleteAll(f);
			}
			file.delete();
		}
	}

	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {// 判断目录是否存在
			//System.out.println("创建目录失败，目标目录已存在！");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
			destDirName = destDirName + File.separator;
		}
		if (dir.mkdirs()) {// 创建目标目录
			//System.out.println("创建目录成功！" + destDirName);
			return true;
		} else {
			//System.out.println("创建目录失败！");
			return false;
		}
	}

	public static String removeDigital(String value){
		Pattern p = Pattern.compile("[\\d]");
		Matcher matcher = p.matcher(value);
		String result = matcher.replaceAll("");
		return result;
	}

	public static void saveFile(InputStream inputStream, String fileName) throws Exception{
		byte[] bt = new byte[2048];
		int len;
		File file = new File(fileName);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdir();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(fileName);
		while((len = inputStream.read(bt)) != -1){
			fileOutputStream.write(bt, 0, len);
		}
		fileOutputStream.flush();
		inputStream.close();
		fileOutputStream.close();
	}

	public static String getWebRootPath() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = ClassLoader.getSystemClassLoader();
		}
		java.net.URL url = classLoader.getResource("");
		String ROOT_CLASS_PATH = url.getPath() + "/";
		File rootFile = new File(ROOT_CLASS_PATH);
		String WEB_INFO_DIRECTORY_PATH = rootFile.getParent() + "/";
		File webInfoDir = new File(WEB_INFO_DIRECTORY_PATH);
		String SERVLET_CONTEXT_PATH = webInfoDir.getParent() + "/";
		return SERVLET_CONTEXT_PATH;
	}

	public static Double getRoundValue(Double doubleValue, int decimalPoint) {
		if (decimalPoint <= 0) {
			return 0.0;
		}
		if (!doubleValue.toString().contains(".")){
			return doubleValue;
		}
		int pointPos = doubleValue.toString().indexOf(".") + 1;
		if (pointPos + decimalPoint + 1 > doubleValue.toString().length()) {
			return doubleValue;
		}

		int value = Integer.parseInt(doubleValue.toString().substring(pointPos + decimalPoint, pointPos + decimalPoint + 1));
		Double value1 = Double.parseDouble(doubleValue.toString().substring(0, pointPos + decimalPoint));
		if (value < 5) {
			return value1;
		} else {
			String valueString = value1.toString();
			if (valueString.length() < pointPos + decimalPoint) {
				valueString = valueString + "0";
			}
			String subValue = valueString.substring(0, pointPos);
			int i = Integer.parseInt(valueString.substring(pointPos , pointPos + decimalPoint));

			if (value1 < 0) {
				if (i == 0) {
					value1 = Double.parseDouble(subValue + 1);
				} else {
					value1 = Double.parseDouble(subValue + (i + 1));
				}
			} else {
				if (i == 9) {
					value1 = Double.parseDouble(subValue) + 1;
				} else {
					value1 = Double.parseDouble(subValue + (i + 1));
				}
			}
			return value1;
		}
	}

	public static void main(String[] args) {
//		System.out.println(Utils.removeDigital("abc032"));
//		System.out.println(Utils.prepareBaiduPageNumber(0));
//		System.out.println(Utils.prepareBaiduPageNumber(10));
//		System.out.println(Utils.prepareBaiduPageNumber(21));
//		System.out.println(Utils.prepareBaiduPageNumber(15));
//		System.out.println(Utils.prepareBaiduPageNumber(100));
//		// System.out.println(calculateOptimizePlanCount(3000, 2, 0));
//		System.out.println(getCurrentTimestamp());
//		System.out.println(addDay(getCurrentTimestamp(), 2));
		for (int i = 0; i < 200; i++) {
			System.out.println("结果： " + Utils.getRoundValue((Math.random() * 0.7 + 0.5), 2));
			System.out.println("结果： " + Utils.getRoundValue(-(Math.random() * 0.7 + 0.5), 2));
		}
		String time1 = "2019-10-10";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			System.out.println(getIntervalDays(sdf.parse(time1), new Date()));
			System.out.println(getTwoDateIntervalDays(sdf.parse(time1), new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}