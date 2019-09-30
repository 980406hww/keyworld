package com.keymanager.ckadmin.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
  private static final int CONTAINER_NUMBER_LENGTH = 10;
  public static final String HYPHEN_WITH_SPACES = " - ";
  public static final String HYPHEN = "-";
  public static final String DATE_FORMAT = "dd MMM yyyy, HH:mm";
  public static final String PLAIN_DATE_FORMAT = "yyyyMMddHHmmss";
  public static final String DB_DATE_FORMAT = "yyyymmddhh24miss";
  public static final String PLAIN_DATE_FORMAT_24_FROMAT = "yyyyMMMddHH24mm";
  public static final String PLAN_DATE_FORMAT_WITHOUT_TIME = "MMM dd yyyy";

  public static final String DEFAULT_DIGITAL_FORMAT = "###,###,###,###,##0.00";

  public static final String DEFAULT_NUMBER_FORMAT = "##############0.00";

  public static final String DEFAULT_UTIL_DATE_FORMAT = "MM/dd/yyyy HH:mm";

  public static final char NEW_LINE_CHAR = '\n';
  public static final char ORACLE_ESCAPE_CHAR = '^';
  public static final char NEW_COLUMN_CHAR = '\t';
  public static final char NEW_CSV_COLUMN_CHAR = ',';
  public static final char SPACE_CHAR = ' ';
  public static final char ZERO_CHAR = '0';
  public static final char HTML_SPACE_CHAR = '\u00A0';
  public static final char AND_CHAR = '&';
  public static final char PERCENTAGE_CHAR = '%';
  public static final char LEFT_BRACKET_CHAR = '[';
  public static final char STAR_CHAR = '*';

  public static final String EMPTY_STRING = "";
  public static final String YES_STRING = "Y";
  public static final String NO_STRING = "N";
  public static final String YES_FULL_STRING = "YES";
  public static final String NO_FULL_STRING = "NO";
  public static final String DOT = ".";
  public static final String SPACE_STRING = " ";
  public static final String REGX_MULTIPLE_SPACE = "[\\s]+";
  public static final String COMMA = ",";
  public static final String SINGLE_QUOTE = "'";
  public static final String DOUBLE_QUOTE = "''";
  public static final String DELIMITER = ",";
  public static final String LINE_FEED_CHARACTER = "\r\n";
  public static final String SEMICOLON = ";";
  public static final String NEW_LINE_IN_HTML_EMAIL = "<br/>";
  public static final String SLASH_STRING = "/";
  public static final String SLASH_STRING_WITH_SPACE = " / ";
  public static final String BACK_SLASH_STRING = "\\";
  public static final String COLON = ":";
  public static final String TILDE = "~";
  public static final String LEFT_PARENTHESIS = "(";
  public static final String RIGHT_PARENTHESIS = ")";
  public static final String LEFT_BRACKET = "[";
  public static final String RIGHT_BRACKET = "]";
  public static final String UNDER_LINE_TAG_BEGIN = "<u>";
  public static final String UNDER_LINE_TAG_END = "</u>";
  public static final String PERCENTAGE = "%";
  public static final String STAR = "*";
  public static final String EMAIL_AT = "@";
  public static final String SPACE_IN_HTML_FORMAT = "&#160;";
  public static final String AUTOCOMPLETE_DELIMITER = ", ";
  public static final String AUTOCOMPLETE_HIDDEN_DELIMITER = "|";
  public static final String UNDERSCORE = "_";
  public static final String TAB = "\t";
  public static final String XML_TAG_LEFT = "<";
  public static final String XML_TAG_RIGHT = ">";
  public static final String XML_TAG_END = "</";
  public static final String XML_TAG_LEFT_END = "/>";
  public static final String NEW_LINE_STR = "\n";
  public static final String EQUAL_STRING = "=";
  public static final String PLUS_STRING = "+";
  public static final String ZERO = "0";
  public static final String ONE = "1";
  public static final String SEPARATE_DELIMITER = "@@@@@@";
  public static final String HTTP_STRING = "http://";
  public static final String HTTPS_STRING = "https://";
  public static final String NOT_APPLICABLE = "N/A";
  public static final String OMIT_STRING = "...";
  public static final int MAX_STRING_COLUMN_LENGTH = 4000;
  /**
   * <p>
   * Random object used by random method. This has to be not local to the random method so as to not return the same value in the same millisecond.
   * </p>
   */
  private static final Random RANDOM = new Random();

  private static final String[] RANDOM_VALUES = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
      "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
      "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

  private final static String[] HEX = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10", "11",
      "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
      "2A", "2B", "2C", "2D", "2E", "2F", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F", "40", "41",
      "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
      "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71",
      "72", "73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
      "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F", "A0", "A1",
      "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9",
      "BA", "BB", "BC", "BD", "BE", "BF", "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1",
      "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9",
      "EA", "EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };

  private final static byte[] val = { 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
      0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
      0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A,
      0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
      0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
      0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
      0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
      0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
      0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
      0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
      0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F };

  static public BigDecimal asBigDecimal(String stringValue) {
    try {
      return new BigDecimal(stringValue);
    } catch (NumberFormatException nfe) {
      return null;
    }
  }

  static public BigDecimal asNotNullBigDecimal(String stringValue) {
    if (StringUtil.isNotNullNorEmpty(stringValue)) {
      try {
        return new BigDecimal(stringValue);
      } catch (NumberFormatException nfe) {
        return new BigDecimal(0);
      }
    }
    return new BigDecimal(0);
  }
  
  static public boolean asBooleanPrimitive(String stringValue) {
    if (isNullOrEmpty(stringValue))
      return false;
    else
      return "1".equals(stringValue);
  }

  static public Double asDouble(String stringValue) {
    try {
      return new Double(stringValue);
    } catch (NumberFormatException nfe) {
      return null;
    }
  }

  static public Integer asInteger(String stringValue) {
    try {
      return Integer.valueOf(stringValue);
    } catch (NumberFormatException nfe) {
      return null;
    }
  }

  /**
   * <p>
   * Converts a boolean to an int using the convention that <code>zero</code> is <code>false</code>.
   * </p>
   *
   * <pre>
   *   StringUtil.asIntegerPrimitive(true)  = 1
   *   StringUtil.asIntegerPrimitive(false) = 0
   * </pre>
   *
   * @param bool
   *          the boolean to convert
   * @return one if <code>true</code>, zero if <code>false</code>
   * @deprecated User {@link org.apache.commons.lang.BooleanUtils#toInteger(boolean)}
   */
  @Deprecated
  static public int asIntegerPrimitive(boolean booleanValue) {
    return booleanValue ? 1 : 0;
  }

  /**
   * @deprecated Use {@link org.apache.commons.lang.math.NumberUtils#toInt(String)}
   * @param stringValue
   * @return
   */
  @Deprecated
  static public int asIntPrimitive(String stringValue) {
    Integer value = asInteger(stringValue);
    if (value == null)
      return 0;
    else
      return value.intValue();
  }

  static public Long asLong(String stringValue) {
    try {
      return Long.valueOf(stringValue);
    } catch (NumberFormatException nfe) {
      return null;
    }
  }

  /**
   * @deprecated User {@link org.apache.commons.lang.math.NumberUtils#toLong(String)}
   * @param stringValue
   * @return
   */
  @Deprecated
  static public long asLongPrimitive(String stringValue) {
    Long value = asLong(stringValue);
    if (value == null)
      return 0;
    else
      return value.longValue();
  }

  static public String asOracleString(String stringValue) {
    if (stringValue == null)
      return null;
    else
      return "'" + replaceWith(stringValue, '\'', "''") + "'";
  }

  static public java.sql.Date asSqlDate(String stringValue) {
    try {
      return java.sql.Date.valueOf(stringValue);
    } catch (IllegalArgumentException iae) {
      return null;
    }
  }

  static public Date asUtilDate(String stringValue, DateFormat format, String timeZone) {
    if (isNullOrEmpty(stringValue))
      return null;
    try {
      format.setTimeZone(TimeZone.getTimeZone(timeZone));
      return format.parse(stringValue);
    } catch (ParseException iae) {
      iae.printStackTrace();
      return null;
    }
  }

  static public Date asUtilDate(String stringValue, String timeZone) {
    return asUtilDate(stringValue, DEFAULT_UTIL_DATE_FORMAT, timeZone);
  }

  static public Date asUtilDate(String stringValue, String pattern, String timeZone) {
    return asUtilDate(stringValue, new SimpleDateFormat(pattern), timeZone);
  }

  /**
   * <p>
   * Capitalizes a String changing the first letter to upper case as per {@link Character#toUpperCase(char)}. No other letters are changed.
   * </p>
   *
   *
   * <pre>
   * StringUtil.capitalize(null)  = null
   * StringUtil.capitalize(&quot;&quot;)    = &quot;&quot;
   * StringUtil.capitalize(&quot;cat&quot;) = &quot;Cat&quot;
   * StringUtil.capitalize(&quot;cAt&quot;) = &quot;CAt&quot;
   * </pre>
   *
   * @param stringValue
   *          the String to capitalize, may be null
   * @return the capitalized String, <code>null</code> if null String input
   * @see org.apache.commons.lang.StringUtils#capitalize(String)
   */
  public static String capitalize(String stringValue) {
    if (isNullOrEmpty(stringValue))
      return stringValue;
    return new StringBuffer().append(Character.toUpperCase(stringValue.charAt(0))).append(stringValue.substring(1)).toString();
  }

  public static String combineStringWithSeperator(String str1, String str2, String seperator) {
    StringBuilder sb = new StringBuilder();
    if (StringUtil.isNotNullNorEmpty(str1)) {
      sb.append(str1);
      if (StringUtil.isNotNullNorEmpty(str2)) {
        sb.append(seperator);
      }
    }
    if (StringUtil.isNotNullNorEmpty(str2)) {
      sb.append(str2);
    }
    return sb.toString();
  }

  public static boolean compareContainerNumber(String containerNum1, String containerNum2) {
    if (isNullOrEmpty(containerNum1) || isNullOrEmpty(containerNum2)) {
      return false;
    }
    return dropCheckDigitNumber(containerNum1).equals(dropCheckDigitNumber(containerNum2));
  }

  /**
   * <p>
   * This function name is not clear, please use {@link #compareAfterTrimToEmpty(String, String)}
   * </p>
   *
   * @see #compareAfterTrimToEmpty(String, String)
   *
   * @param value1
   *          the first String, may be null
   * @param value2
   *          the second String, may be null
   * @return <code>true</code> if the Strings are equal, or both blank.
   *
   * @deprecated
   */
  @Deprecated
  public static boolean compareString(String value1, String value2) {
    return compareAfterTrimToEmpty(value1, value2);
  }

  /**
   * <p>
   * Compare two string value, trim the value to empty firstly, then use {@link String#equals(Object)} to Compare
   * </p>
   *
   * <pre>
   * StringUtil.compareAfterTrimToEmpty(null, null)=true
   * StringUtil.compareAfterTrimToEmpty(null, '')=true
   * StringUtil.compareAfterTrimToEmpty(null, ' ')=true
   * StringUtil.compareAfterTrimToEmpty('', ' ')=true
   * StringUtil.compareAfterTrimToEmpty('abc', 'abc ')=true
   * StringUtil.compareAfterTrimToEmpty(' abc', 'abc')=true
   * StringUtil.compareAfterTrimToEmpty(' abc ', 'abc')=true
   * StringUtil.compareAfterTrimToEmpty(' abc ', 'aBc')=false
   * </pre>
   *
   * @param value1
   *          the first String, may be null
   * @param value2
   *          the second String, may be null
   * @return <code>true</code> if the Strings are equal, or both blank.
   *
   */
  public static boolean compareAfterTrimToEmpty(String value1, String value2) {
    String tempValue1 = value1 == null ? EMPTY_STRING : trim(value1);
    String tempValue2 = value2 == null ? EMPTY_STRING : trim(value2);

    return tempValue1.equals(tempValue2);
  }
  
  public static boolean compareAfterTrimToEmptyAndIgnoreCase(String value1, String value2) {
    String tempValue1 = value1 == null ? EMPTY_STRING : trim(value1);
    String tempValue2 = value2 == null ? EMPTY_STRING : trim(value2);

    return tempValue1.equalsIgnoreCase(tempValue2);
  }

  /**
   * <p>
   * Compare two string value, use {@link String#compareToIgnoreCase(String)} to Compare, it does not trim to empty firstly.
   * </p>
   *
   * <pre>
   * StringUtil.compareToIgnoreCase(null, null)=0
   * StringUtil.compareToIgnoreCase(null, '')=0
   * StringUtil.compareToIgnoreCase(null, ' ')=0
   * StringUtil.compareToIgnoreCase('', ' ')=0
   * StringUtil.compareToIgnoreCase('abc', 'abc ')<0
   * StringUtil.compareToIgnoreCase(' abc', 'abc')<0
   * StringUtil.compareToIgnoreCase(' abc ', 'abc')<0
   * StringUtil.compareToIgnoreCase(' abc ', 'aBc')<0
   * </pre>
   *
   * @param value1
   *          the first String, may be null
   * @param value2
   *          the second String, may be null
   * @return a negative integer, zero, or a positive integer as the specified String is greater than, equal to(or both blank), or less than this
   *         String, ignoring case considerations.
   *
   */
  public static int compareToIgnoreCase(String value1, String value2) {
    if (isNullOrEmpty(value1) && isNullOrEmpty(value2)) {
      return 0;
    }
    if (value1 == null) {
      return -1;
    }
    if (value2 == null) {
      return 1;
    }
    return value1.compareToIgnoreCase(value2);
  }

  public static String constructAutoCompleteDisplayResult(List<String> displayFields) {
    StringBuilder stringBuilder = new StringBuilder("");
    for (String display : displayFields) {
      stringBuilder.append(display).append(AUTOCOMPLETE_DELIMITER);
    }
    stringBuilder.delete(stringBuilder.length() - AUTOCOMPLETE_DELIMITER.length(), stringBuilder.length());
    return stringBuilder.toString();
  }

  public static String constructAutoCompleteHiddenValue(List<String> hiddenFields) {
    StringBuilder stringBuilder = new StringBuilder("");
    for (String hidden : hiddenFields) {
      stringBuilder.append("|").append(hidden);
    }
    return stringBuilder.toString();
  }

  public static String constructAutoCompleteResult(List<String> displayFields, List<String> hiddenFields) {
    StringBuilder stringBuilder = new StringBuilder("");
    stringBuilder.append(constructAutoCompleteDisplayResult(displayFields));
    stringBuilder.append(constructAutoCompleteHiddenValue(hiddenFields));
    stringBuilder.append(NEW_LINE_CHAR);
    return stringBuilder.toString();
  }

  public static boolean containsPartOfTarget(String[] strArray, String target) {
    if (StringUtil.isNullOrEmpty(strArray))
      return false;

    for (String str : strArray) {
      if (StringUtil.isNotNullNorEmpty(target) && StringUtil.isNotNullNorEmpty(str) && target.indexOf(str) != -1) {
        return true;
      }
    }

    return false;
  }

  public static boolean containsTarget(String[] strArray, String target) {
    if (StringUtil.isNullOrEmpty(strArray))
      return false;

    for (String str : strArray) {
      if (StringUtil.isNotNullNorEmpty(target) && StringUtil.isNotNullNorEmpty(str) && target.equals(str)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @deprecated Use {@link org.apache.commons.lang.StringEscapeUtils#escapeXml(String)}
   * @param s
   * @return
   */
  @Deprecated
  public static String converSpecialXMLCharacter(String s) {
    String str = s;
    if (str.indexOf("&") > -1) {
      str = str.replaceAll("&", "&amp;");
    }
    if (str.indexOf("<") > -1) {
      str = str.replaceAll("<", "&lt;");
    }
    if (str.indexOf(">") > -1) {
      str = str.replaceAll(">", "&gt;");
    }
    if (str.indexOf("'") > -1) {
      str = str.replaceAll("'", "&apos;");
    }
    if (str.indexOf("\"") > -1) {
      str = str.replaceAll("\"", "&quot;");
    }
    return str;
  }

  public static String converSpecialXMLCharacter2(final String s) {
    String str = s;
    if (str.indexOf("&") > -1) {
      str = str.replaceAll("&", "&amp;");
    }
    if (str.indexOf("'") > -1) {
      str = str.replaceAll("'", "&apos;");
    }
    if (str.indexOf("\"") > -1) {
      str = str.replaceAll("\"", "&quot;");
    }
    return str;
  }

  /**
   * @deprecated Use {@link org.apache.commons.lang.StringEscapeUtils#unescapeHtml(String)}
   * @param htmlSourceString
   * @return
   */
  @Deprecated
  public static String convertHtmlUtf8ToUnicode(String htmlSourceString) {
    // String htmlSourceString = "&#31616;&#20307;&#20013;&#25991;" ; //9
    if (htmlSourceString == null || htmlSourceString.length() == 0) {
      return htmlSourceString;
    }
    boolean codeStart = false;
    StringBuffer stringBuffer = new StringBuffer();
    StringBuffer subStringBuffer = new StringBuffer("");
    for (int i = 0, m = htmlSourceString.length(); i < m; i++) {
      char character = htmlSourceString.charAt(i);
      if (character == '&') {
        if ((i + 3) < m && htmlSourceString.charAt(i + 1) == '#') {
          if (codeStart) {
            stringBuffer.append(subStringBuffer);
          }
          i++;
          codeStart = true;
          subStringBuffer = new StringBuffer().append(character).append(htmlSourceString.charAt(i));
        } else {
          if (codeStart) {
            stringBuffer.append(subStringBuffer);
            subStringBuffer = new StringBuffer();
          }
          stringBuffer.append(character);
          codeStart = false;
        }
      }// 012345678&#; 9+3 <12
      else if (character == ';') {
        if (codeStart) {
          String strSubStringBuffer = subStringBuffer.toString();
          if (strSubStringBuffer.length() > 3 && strSubStringBuffer.length() <= 9) {
            // ok,good case
            subStringBuffer.append(character);
            stringBuffer.append(convertToUnicode(subStringBuffer.toString()));
          } else {
            stringBuffer.append(subStringBuffer).append(character);
          }
          subStringBuffer = new StringBuffer();
          codeStart = false;
        } else {
          stringBuffer.append(character);
        }
      } else if (character >= '0' && character <= '9') {
        if (codeStart) {
          subStringBuffer.append(character);
        } else {
          stringBuffer.append(character);
        }
      } else {
        if (codeStart) {
          stringBuffer.append(subStringBuffer);
          subStringBuffer = new StringBuffer();
        }
        stringBuffer.append(character);
        codeStart = false;
      }
    }
    stringBuffer.append(subStringBuffer);
    return stringBuffer.toString();
  }

  public static String convertObjectToString(Object object) {
    if (object == null) {
      return "";
    } else {
      return String.valueOf(object);
    }

  }

  private static char convertToUnicode(String val) {
    String localVal = val.substring(2, val.length() - 1);
    char r = '\0';
    r = (char) Integer.parseInt(localVal);
    return r;
  }

  /**
   * <p>
   * Returns either the passed in String, or if the String is empty(trim firstly) or <code>null</code>, the value of <code>defaultStr</code>.
   * </p>
   *
   * <pre>
   * StringUtil.defaultIfEmpty(null, "")      = ""
   * StringUtil.defaultIfEmpty(null, "NULL")  = "NULL"
   * StringUtil.defaultIfEmpty("", "NULL")    = "NULL"
   * StringUtil.defaultIfEmpty(" ", "NULL")   = "NULL"
   * StringUtil.defaultIfEmpty("bat", "NULL") = "bat"
   * </pre>
   *
   * @see org.apache.commons.lang.StringUtils#defaultIfEmpty(String, String)
   * @see StringUtil#isNullOrEmpty(String)
   * @param str
   *          the String to check, may be null
   * @param defaultStr
   *          the default String to return if the input trim to empty ("") or <code>null</code>, may be null
   * @return the passed in String, or the default
   *
   */
  public static String defaultIfEmpty(String str, String defaultStr) {
    return isNullOrEmpty(str) ? defaultStr : str;
  }

  // input 12456-1 ==> 12456
  public static String dropCheckDigitNumber(String str) {
    if (StringUtil.isNullOrEmpty(str))
      return str;
    int pos = str.indexOf(HYPHEN);
    if (pos > 0) {
      return str.substring(0, pos);
    }
    return str;
  }

  public static String subStringBefore(String str, String seperator) {
      if (StringUtil.isNullOrEmpty(str))
          return str;
      int pos = str.indexOf(seperator);
      if (pos > 0) {
          return str.substring(0, pos);
      }
      return str;
  }

  public static String dropPrefixPercentChar(String str) {
    if (StringUtil.isNullOrEmpty(str))
      return "";
    String localStr = str.trim();
    String regx = "^[%]+";
    return localStr.replaceAll(regx, StringUtil.EMPTY_STRING);
  }

  public static String dropWildcard(String str) {
    if (isNullOrEmpty(str)) {
      return str;
    } else {
      String localStr = str.trim();
      if (localStr.endsWith("*")) {
        return localStr.substring(0, localStr.length() - 1);
      } else {
        return localStr;
      }
    }
  }

  public static String escape(String s) {
    if (isNullOrEmpty(s)) {
      return EMPTY_STRING;
    }
    StringBuffer sbuf = new StringBuffer();
    int len = s.length();
    for (int i = 0; i < len; i++) {
      int ch = s.charAt(i);
      if (ch == ' ') { // space : map to '+'
        sbuf.append("%20");
      } else if ('A' <= ch && ch <= 'Z') { // 'A'..'Z' : as it was
        sbuf.append((char) ch);
      } else if ('a' <= ch && ch <= 'z') { // 'a'..'z' : as it was
        sbuf.append((char) ch);
      } else if ('0' <= ch && ch <= '9') { // '0'..'9' : as it was
        sbuf.append((char) ch);
      } else if (ch == '-' || ch == '_' // unreserved : as it was
          || ch == '.' || ch == '!' || ch == '~' || ch == '*' || ch == '\'' || ch == '(' || ch == ')') {
        sbuf.append((char) ch);
      } else if (ch <= 0x007F) { // other ASCII : map to %XX
        sbuf.append('%');
        sbuf.append(HEX[ch]);
      } else { // unicode : map to %uXXXX
        sbuf.append('%');
        sbuf.append('u');
        sbuf.append(HEX[(ch >>> 8)]);
        sbuf.append(HEX[(0x00FF & ch)]);
      }
    }
    return sbuf.toString();
  }

  public static String escapeOracleSql(String stringValue) {
    String formattedString = stringValue;
    if (stringValue != null) {

      formattedString = replaceWith(formattedString, '\'', "''");
      formattedString = replaceWith(formattedString, '_', ORACLE_ESCAPE_CHAR + "_");
      formattedString = replaceWith(formattedString, '%', ORACLE_ESCAPE_CHAR + "%");
      formattedString = formattedString.trim();
    }
    return formattedString;
  }

  public static String formatDecimal(double number, String format) {
    return new DecimalFormat(format).format(number);
  }

  public static String formatDecimal(BigDecimal number, String format) {
    return new DecimalFormat(format).format(number);
  }

  public static String roundAmount(BigDecimal number, int scale, RoundingMode roundingMode) {
    BigDecimal amount = number.setScale(scale, roundingMode);
    StringBuilder format = new StringBuilder("###0");
    if (scale > 0) {
      format.append(".");
      for (int i = 0; i < scale; i++) {
        format.append("0");
      }
    }
    return new DecimalFormat(format.toString()).format(amount);
  }

  public static String formatAmount(String number, int scale, RoundingMode roundingMode) {
    BigDecimal amount = new BigDecimal(number).setScale(scale, roundingMode);
    StringBuilder format = new StringBuilder("#,##0");
    if (scale > 0) {
      format.append(".");
      for (int i = 0; i < scale; i++) {
        format.append("0");
      }
    }
    return new DecimalFormat(format.toString()).format(amount);
  }

  public static String formatDecimalWithDefaultNumberFormat(BigDecimal number) {
    return new DecimalFormat(DEFAULT_NUMBER_FORMAT).format(number);
  }


  public static String formatForAutoComplete(String... fields) {
    StringBuilder builder = new StringBuilder();
    for (String field : fields) {
      if (isNotNullNorEmpty(field)) {
        builder.append(field).append(AUTOCOMPLETE_DELIMITER);
      }
    }
    if (builder.length() >= AUTOCOMPLETE_DELIMITER.length()) {
      builder.delete(builder.length() - AUTOCOMPLETE_DELIMITER.length(), builder.length());
    }
    return builder.toString();
  }

  public static String generateDigit(int length) {
    String digit = EMPTY_STRING + (long) (Math.random() * Math.pow(10, length));
    StringBuilder result = new StringBuilder(digit);
    int appendLength = length - digit.length();
    for (int i = 0; i < appendLength; i++) {
      result.insert(0, "0");
    }
    return result.toString();
  }

  public static String getContainerCheckDigit(String containerNumber) {
    if (StringUtil.isNullOrEmpty(containerNumber)) {
      return "";
    }
    int pos = containerNumber.lastIndexOf(HYPHEN);
    if (pos > 0) {
      if (containerNumber.length() - pos > 2) {
        return containerNumber.substring(pos + 1, pos + 3);
      } else {
        return containerNumber.substring(pos + 1, containerNumber.length());
      }
    } else if (containerNumber.length() > 10) {
      return containerNumber.substring(10, (containerNumber.length() > 12) ? 12 : containerNumber.length());
    }
    return "";
  }

  // input OOLU1234567 ==> OOLU123456
  public static String getContainerNumberAfterFilterCheckDigit(String str) {
    if (StringUtil.isNullOrEmpty(str))
      return str;
    int pos = str.indexOf(HYPHEN);
    if (pos > 0 && pos <= CONTAINER_NUMBER_LENGTH) {
      return str.substring(0, pos);
    } else if (pos > CONTAINER_NUMBER_LENGTH) {
      return str.substring(0, CONTAINER_NUMBER_LENGTH);
    } else if (pos < 0 && str.length() > CONTAINER_NUMBER_LENGTH) {
      return str.substring(0, CONTAINER_NUMBER_LENGTH);
    }
    return str;
  }

  public static int getStringLength(String str) {
    if (StringUtil.isNullOrEmpty(str))
      return 0;
    return str.length();
  }

  public static String getDisplayString(int begin, int length, String inputStr, String omitTag) {
    if (begin < 0 || begin > inputStr.length()) {
      return null;
    }
    if (begin + length < inputStr.length()) {
      if (length > omitTag.length()) {
        return inputStr.substring(begin, begin + length - omitTag.length()) + omitTag;
      } else {
        return inputStr.substring(begin, begin + length);
      }
    } else {
      return inputStr.substring(begin, inputStr.length());
    }
  }


  /**
   * <p>
   * Capitalizes a String changing the first letter to upper case as per {@link Character#toUpperCase(char)}. No other letters are changed.
   * </p>
   *
   *
   * <pre>
   * StringUtil.capitalize(null)  = &quot;&quot;
   * StringUtil.capitalize(&quot;&quot;)    = &quot;&quot;
   * StringUtil.capitalize(&quot;cat&quot;) = &quot;Cat&quot;
   * StringUtil.capitalize(&quot;cAt&quot;) = &quot;CAt&quot;
   * </pre>
   *
   * @param str
   *          the String to capitalize, may be null
   * @return the capitalized String, <code>null</code> if null String input
   * @see #capitalize(String)
   * @deprecated
   */
  @Deprecated
  public static String getInitialCapital(String str) {
    if (isNotNullNorEmpty(str)) {
      return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    return EMPTY_STRING;
  }

  public static String getLines(String lines, int count) {
    StringBuilder result = new StringBuilder();
    if (lines != null && lines.length() > 0) {
      String[] temp = lines.split(NEW_LINE_STR);
      int i;
      for (i = 0; i < temp.length && i < count; i++) {
        result.append(temp[i]).append(NEW_LINE_STR);
      }
      if (i > 0 && count <= temp.length) {
        result.setLength(result.length() - 1);
      }
    }
    return result.toString();
  }

  public static String getNotNullObjectString(Object object) {
    return (object == null) ? EMPTY_STRING : object.toString().trim();
  }

  public static String getNotNullObjectStringWithSpace(Object object) {
    return (object == null) ? EMPTY_STRING : object.toString();
  }

  public static String getRandomCode(int length) {
    StringBuilder result = new StringBuilder();
    int randomvalue = 0;
    for (int i = 0; i < length; i++) {
      randomvalue = RANDOM.nextInt(RANDOM_VALUES.length);
      result.append(RANDOM_VALUES[randomvalue]);
    }
    return result.toString();
  }

  public static String getRandomString() {
    return UUID.randomUUID().toString();
  }

  public static String getRandomString(int length) {
    String myRandom = "";
    int oneRandomLength = getRandomString().length();
    int j = length / oneRandomLength;
    int i = length % oneRandomLength;
    if (i > 0) {
      j = j + 1;
    }
    for (int m = 0; m < j; m++) {
      myRandom = myRandom.concat(getRandomString());
    }
    return myRandom.substring(0, length);
  }

  public static String getSpaceString(int length) {
    return padWithSpaceUpTo(EMPTY_STRING, length);
  }

  public static String getStringBulderFromBufferedInpustream(BufferedReader bufferedRead) throws IOException {
    String line = null;
    StringBuilder sb = new StringBuilder();
    while ((line = bufferedRead.readLine()) != null) {
      if (StringUtil.isNotNullNorEmpty(line)) {
        sb.append(line).append(NEW_LINE_CHAR);
      }
    }
    return sb.toString();
  }

  // add by Joe Sun for Registration

  /**
   * This method name is bad, and i do not know why pass a DataInputStream as parameter
   */
  public static String getStringBulderFromInpustream(DataInputStream dataInputStream) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(dataInputStream));
    String value = getStringBulderFromBufferedInpustream(br);
    if (value.lastIndexOf(NEW_LINE_CHAR) == value.length() - 1) {
      value = value.substring(0, value.length() - 1);
    }
    return value;
  }

  /**
   * Handling <code>null</code> by returning an empty String ("").
   *
   * @param str
   *          the String to check, may be null
   * @return the checked text, never <code>null</code>
   *
   * @see #ifNullReturn(String, String)
   */
  public static String getStringNonNull(String str) {
    return ifNullReturn(str, EMPTY_STRING);
  }

  public static String getSubStringByMaxLength(String oldString, int maxLength) {
    if (oldString.length() > maxLength) {
      return oldString.substring(0, maxLength);
    }
    return oldString;
  }

  public static int getSumOfASCIICodeForString(String str) {
    int sum = 0;
    if (str != null) {
      char[] ch = str.toCharArray();
      for (int i = 0; i < ch.length; i++) {
        sum += ch[i];
      }
    }
    return sum;
  }

  public static String[] getTokenSegments(String str, char ch) {
    if (str == null || str.length() == 0) {
      return new String[] { EMPTY_STRING };
    }
    List<String> arr = new ArrayList<String>();
    int begin = 0;
    for (int i = 0, m = str.length(); i < m; i++) {
      if (str.charAt(i) == ch) {
        arr.add(str.substring(begin, i));
        begin = i + 1;
      }
    }
    arr.add(str.substring(begin));
    return arr.toArray(new String[arr.size()]);
  }

  /**
   * <p>
   * Returns either the passed in String, or if the String is <code>null</code>, the value of <code>defaultStr</code>.
   * </p>
   *
   * <pre>
   * StringUtil.ifNullReturn(null, &quot;NULL&quot;)  = &quot;NULL&quot;
   * StringUtil.ifNullReturn(&quot;bat&quot;, &quot;NULL&quot;) = &quot;bat&quot;
   * </pre>
   *
   * @see org.apache.commons.lang.StringUtils#defaultIfEmpty(String, String)
   *
   * @param stringValue
   *          the String to check, may be null
   * @param defaultStr
   *          the default String to return if the input is empty ("") or <code>null</code>, may be null
   * @return the passed in String, or the default
   */
  public static String ifNullReturn(String stringValue, String defaultStr) {
    return stringValue == null ? defaultStr : stringValue;
  }

  public static boolean isBooleanString(String str) {
    return !isNullOrEmpty(str) && ("true".equals(str) || "false".equals(str));
  }

  public static boolean isContainsSpace(String str) {
    String tmpStr = trim(str);
    if (tmpStr.indexOf(" ") > 0) {
      return true;
    }
    return false;
  }

  public static boolean isDecimal(String str) {
    if (isNullOrEmptyWithTrim(str)) {
      return false;
    }
    int index = 0;
    boolean isDotAlreadyExists = false;
    for (char c : str.toCharArray()) {
      if (!Character.isDigit(c) && c != '.' && c != '-') {
        return false;
      } else if (c == '.') {
        if (index == 0 || index == str.length() - 1 || isDotAlreadyExists) {
          return false;
        } else {
          isDotAlreadyExists = true;
        }
      } else if (c == '-') {
        if (index != 0 || str.length() == 1) {
          return false;
        }
      }
      index++;
    }
    return true;
  }

  /**
   * @deprecated
   * @param str
   * @return
   */
  @Deprecated
  public static boolean isInAvailableNumber(String str) {
    if (StringUtil.isNullOrEmptyWithTrim(str))
      return true;

    String regx = "[0-9]{1,30}";
    Pattern pattern = Pattern.compile(regx);

    Matcher matcher = pattern.matcher(str);

    return !matcher.find();
  }

  /**
   * @deprecated
   * @param str
   * @return
   */
  @Deprecated
  public static boolean isInAvailableUserID(String str) {

    if (StringUtil.isNullOrEmptyWithTrim(str))
      return true;

    String regx = "^[a-zA-Z_][a-zA-Z0-9_]{5,19}$";
    Pattern pattern = Pattern.compile(regx);

    Matcher matcher = pattern.matcher(str);

    return !matcher.find();
  }

  private static boolean isIncludeSBCCase(String checkStr) {
    if (StringUtil.isNullOrEmpty(checkStr)) {
      return false;
    } else {
      int length = checkStr.length();
      for (int i = 0; i < length; i++) {
        char c = checkStr.charAt(i);
        if (c > 255) {
          return true;
        }
      }
      return false;
    }
  }
  
  /**
   * @see org.apache.commons.validator.EmailValidator#isValid(String)
   *
   * @param emailAddress
   * @return
   */
  public static boolean isInvalidEmailAddressFormatString(String emailAddress) {
    if (StringUtil.isNullOrEmpty(emailAddress)) {
      return false;
    }
    if (emailAddress.indexOf(" ") != -1) {
      return true;
    }
    String filteredEmailAddress = emailAddress;
    if (filteredEmailAddress.indexOf("'") != -1) {
      filteredEmailAddress = StringUtil.replace(filteredEmailAddress, "'", "");
    }
    String regx = "^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[\\w]{2,6}$";
    Pattern pattern = Pattern.compile(regx);
    Matcher matcher = pattern.matcher(filteredEmailAddress);
    return !matcher.find();
  }

  public static boolean matches(String str, String regex) {
    return str == null ? false : str.matches(regex);
  }

  public static boolean isMatchRegularExpression(String regularExpression, String matchingContent) {
    Pattern pattern = Pattern.compile(regularExpression);
    Matcher matcher = pattern.matcher(matchingContent);
    return matcher.find();
  }

  /**
   * <p>
   * Checks if the String contains only characters.
   * </p>
   *
   * <p>
   * <code>null</code> will return <code>false</code>. An empty String ("") will return <code>true</code>.
   * </p>
   *
   * <pre>
   * StringUtil.isAlpha(null)   = false
   * StringUtil.isAlpha("")     = false
   * StringUtil.isAlpha("  ")   = false
   * StringUtil.isAlpha("abc")  = true
   * StringUtil.isAlpha("ab2c") = false
   * StringUtil.isAlpha("ab-c") = false
   * </pre>
   *
   * @param str
   *          the String to check, may be null
   * @return <code>true</code> if only contains characters, and is non-null
   * @see org.apache.commons.lang.StringUtils#isAlpha(String)
   */
  public static boolean isAlpha(String str) {
    return !isNotAlpha(str);
  }

  public static boolean isAlphaNumeric(String str) {
    return matches(str, "^[a-zA-Z0-9]+$");
  }

  public static boolean isAlphaNumericAllowDot(String str) {
    return matches(str, "^[a-zA-Z0-9\\.]+$");
  }

  public static boolean isNotAlpha(String str) {
    return !matches(str, "^[A-Za-z]+$");
  }

  public static boolean isNotAlphaIncludingHyphen(String str) {
    return !matches(str, "^[A-Za-z\\-]+$");
  }

  /**
   * @see org.apache.commons.lang.StringUtils#isAlphanumeric(String)
   *
   * @param str
   * @return
   */
  public static boolean isNotAlphaNumeric(String str) {
    return !matches(str, "^[A-Za-z0-9]+$");
  }

  public static boolean isNotAlphaNumericButSpaceAllowed(String str) {

    if (StringUtil.isNullOrEmptyWithTrim(str))
      return true;

    String regx = "^[A-Za-z0-9 ]+$ *";
    Pattern pattern = Pattern.compile(regx);

    Matcher matcher = pattern.matcher(str);

    return !matcher.find();
  }

  /**
   * The method name is too long.
   *
   * @deprecated
   * @param str
   * @return
   */
  @Deprecated
  public static boolean isNotAlphaNumericButSpacePeriodHyphenAndQuoteAllowed(String str) {

    if (StringUtil.isNullOrEmptyWithTrim(str))
      return true;

    String regx = "^[A-Za-z0-9 '.-]+$ *";
    Pattern pattern = Pattern.compile(regx);

    Matcher matcher = pattern.matcher(str);

    return !matcher.find();
  }

  public static boolean isNotAlphaNumericIncludingHyphen(String str) {
    return !matches(str, "^[A-Za-z0-9\\-]+$");
  }

  public static boolean isNotAlphaNumericIncludingHyphenAndSpace(String str) {
    return !matches(str, "^[A-Za-z0-9\\- ]+$");
  }

  public static boolean isNotAlphaNumericIncludingHyphenAndSpaceAndLinefeeds(String str) {
    return !matches(str, "^[A-Za-z0-9\\- \r\n]+$");
  }

  public static boolean isNotAlphaNumericIncludingUnderscoreAndHyphen(String str) {
    return !matches(str, "^[A-Za-z0-9_\\-]+$");
  }

  public static boolean isNotAlphaNumericIncludingUnderscoreAndHyphenAndSpace(String str) {
    return !matches(str, "^[A-Za-z0-9_\\- ]+$");
  }

  // add by Peisen Lin for InternalSupportCompanySearch
  public static boolean isNotAlphaNumericOrSpecialChars(String str) {
    return !matches(str, "^[A-Za-z0-9*_ ,'\\\".&/\\-()?]+$");
  }

  public static boolean isNotAlphaNumericOrStar(String str) {
    return !matches(str, "^[A-Za-z0-9*]+$");
  }

  public static boolean isNotEnglishButSpaceAllowed(String str) {
    if (StringUtil.isNullOrEmptyWithTrim(str))
      return true;

    String regx = "^[A-Za-z ]+$ *";
    Pattern pattern = Pattern.compile(regx);

    Matcher matcher = pattern.matcher(str);

    return !matcher.find();
  }

  public static boolean isNotEnglishButUnderscoreAllowed(String str) {

    if (StringUtil.isNullOrEmptyWithTrim(str))
      return true;

    String regx = "^[A-Za-z0-9_]+$_*";
    Pattern pattern = Pattern.compile(regx);

    Matcher matcher = pattern.matcher(str);

    return !matcher.find();
  }

  public static boolean isNotNullNorEmpty(String stringValue) {
    return !isNullOrEmpty(stringValue);
  }

  static public boolean isNotNullNorEmpty(String[] stringValues) {
    return !isNullOrEmpty(stringValues);
  }

  public static boolean isNotNumber(String str) {

    if (str == null || str.length() == 0) {
      return false;
    }

    if (StringUtil.isNullOrEmptyWithTrim(str))
      return true;

    for (int i = 0; i < str.length(); i++) {

      if (Character.isSpaceChar(str.charAt(i))) {
        return true;
      }

      if (!Character.isDigit(str.charAt(i))) {
        return true;
      }
    }

    return false;
  }

  public static boolean isNotNumericIncludingHyphen(String str) {
    if (StringUtil.isNullOrEmptyWithTrim(str)) {
      return true;
    }
    String regx = "^[0-9\\-]+$";
    Pattern pattern = Pattern.compile(regx);
    Matcher matcher = pattern.matcher(str);
    return !matcher.find();
  }

  /**
   * <p>
   * Checks if a String is whitespace, empty ("") or null.
   * </p>
   *
   * <pre>
   * StringUtil.isNullOrEmpty(null)      = true
   * StringUtil.isNullOrEmpty(&quot;&quot;)        = true
   * StringUtil.isNullOrEmpty(&quot; &quot;)       = true
   * StringUtil.isNullOrEmpty(&quot;bob&quot;)     = false
   * StringUtil.isNullOrEmpty(&quot;  bob  &quot;) = false
   * </pre>
   *
   * @param str
   *          the String to check, may be null
   * @return <code>true</code> if the String is null, empty or whitespace
   * @see org.apache.commons.lang.StringUtils#isBlank(String)
   */
  public static boolean isNullOrEmpty(String stringValue) {
    return stringValue == null || stringValue.trim().length() == 0;
  }

  static public boolean isNullOrEmpty(String[] stringValues) {
    return ((stringValues == null) || (stringValues.length <= 0));
  }

  static public boolean isNullOrEmptyOrNullString(String stringValue) {
    return stringValue == null || stringValue.trim().length() == 0 || "null".equals(stringValue);
  }

  // Defined by Chris Wong (Jan, 2007) ----------------------------------->
  public static boolean isNullOrEmptyWithTrim(String str) {
    if (StringUtil.isNullOrEmpty(str) || StringUtil.isNullOrEmpty(str.trim())) {
      return true;
    }

    return false;
  }

  public static boolean isNumeric(String str) {
    if (isNullOrEmptyWithTrim(str)) {
      return false;
    }
    int index = 0;
    for (char c : str.toCharArray()) {
      if (!Character.isDigit(c) && c != '-') {
        return false;
      } else if (c == '-') {
        if (index != 0) {
          return false;
        }
      }
      index++;
    }
    return true;
  }

  public static boolean isNumericInAvailableLength(String str, int length) {
    if (isNumeric(str) && str.length() <= length) {
      return true;
    }
    return false;
  }

  public static boolean isPositiveNumber(String str) {
    if (isNullOrEmptyWithTrim(str)) {
      return false;
    }
    for (char c : str.toCharArray()) {
      if (!Character.isDigit(c)) {
        return false;
      }
    }
    return true;
  }

  public static boolean isPureAscii(String str) {
    if (StringUtil.isNullOrEmptyWithTrim(str))
      return true;
    for (char c : str.toCharArray()) {
      if (c >= 127 || c < 0)
        return false;
    }
    return true;
  }

  public static boolean isVisibleAsciiInput(String str) {
    if (StringUtil.isNullOrEmptyWithTrim(str))
      return true;
    for (char c : str.toCharArray()) {
      if (c >= 127 || c < 32)
        return false;
    }
    return true;
  }

  public static boolean isTheSameStringWithNullAndTrimEqual(String str1, String str2) {
    String localStr1 = str1 == null ? null : str1.trim();
    String localStr2 = str2 == null ? null : str2.trim();
    return localStr1 == null ? localStr2 == null : localStr1.equals(localStr2);
  }

  public static boolean isTheSameStringWithNullEqual(String str1, String str2) {
    return str1 == null ? str2 == null : str1.equals(str2);
  }

  public static boolean isWildcardString(String str) {
    return str != null && str.endsWith("*");
  }

  public static boolean isWildcardSearchString(String str, int minimumLength) {
    return isWildcardString(str) && str.length() > minimumLength;
  }

  /**
   * <p>
   * Strip all non digits at the start of a String
   * </p>
   *
   * <pre>
   * StringUtil.leftStripNonDigits(null)  = null
   * StringUtil.leftStripNonDigits(&quot;&quot;)    = &quot;&quot;
   * StringUtil.leftStripNonDigits(&quot;abc&quot;) = &quot;&quot;
   * StringUtil.leftStripNonDigits(&quot;abc1&quot;) = &quot;1&quot;
   * StringUtil.leftStripNonDigits(&quot;abc1d&quot;) = &quot;1d&quot;
   * </pre>
   *
   * @param str
   *          the String to strip
   * @return Stripped String or original String if no stripping is necessary, <code>null</code> if null String input
   * @since R12.0
   */
  public static String leftStripNonDigits(String str) {
    int strLen;
    if (str == null || (strLen = str.length()) == 0) {
      return str;
    }
    int start = 0;
    while ((start != strLen) && !Character.isDigit(str.charAt(start))) {
      start++;
    }
    return str.substring(start);
  }

  public static String ltrim(String text) {
    if (text == null) {
      return text;
    }
    int textLength = text.length(), textCharIndex = 0;
    while (textCharIndex < textLength && text.charAt(textCharIndex) <= ' ') {
      textCharIndex++;
    }
    return (textCharIndex == 0) ? text : text.substring(textCharIndex);
  }

  /**
   *
   * Handling <code>null</code> by returning an empty String ("").
   *
   * @param str
   *          the String to check, may be null
   * @return the checked text, never <code>null</code>
   *
   * @see #getStringNonNull(String)
   */
  public static String nonNullValue(String value) {
    return getStringNonNull(value);
  }

  public static String normalizeWords(String line) {
    if (line == null) {
      return line;
    }
    int index = 0;
    boolean prevWhiteSpace = true;
    StringBuilder sb = new StringBuilder(line.length());
    while (index < line.length()) {
      char c = line.charAt(index++);
      if (!Character.isLowerCase(c)) {
        c = Character.toLowerCase(c);
      }
      boolean currWhiteSpace = Character.isWhitespace(c);
      if (prevWhiteSpace && !currWhiteSpace) {
        c = Character.toUpperCase(c);
      }
      sb.append(c);
      prevWhiteSpace = currWhiteSpace;
    }
    return sb.toString();
  }

  /**
   * <p>
   * Right pad a String with spaces (' ').
   * </p>
   *
   * <p>
   * The String is padded to the size of <code>maxPadSize</code>.
   * </p>
   *
   * @param string
   *          the String to pad out, may be null
   * @param maxPadSize
   *          the size to pad to
   * @return Right padded String or original String if no padding is necessary, <code>null</code> if null String input
   *
   * @see org.apache.commons.lang.StringUtils#rightPad(String, int)
   */
  public static String padWithSpaceUpTo(String string, int maxPadSize) {
    return padWithUpTo(string, SPACE_CHAR, maxPadSize, false);
  }

  /**
   * <p>
   * Pad a String with spaces (' ') at left/right side.
   * </p>
   *
   * <p>
   * The String is padded to the size of <code>maxPadSize</code>.
   * </p>
   *
   * @param string
   *          the String to pad out, may be null
   * @param maxPadSize
   *          the size to pad to
   * @param isBefore
   *          pad at left or right
   * @return Padded String or original String if no padding is necessary, <code>null</code> if null String input
   *
   * @see org.apache.commons.lang.StringUtils#rightPad(String, int)
   * @see org.apache.commons.lang.StringUtils#leftPad(String, int)
   */
  public static String padWithSpaceUpTo(String string, int maxPadSize, boolean isBefore) {
    return padWithUpTo(string, SPACE_CHAR, maxPadSize, isBefore);
  }

  /**
   * <p>
   * Right pad a String with a specified character.
   * </p>
   *
   * <p>
   * The String is padded to the size of <code>maxPadSize</code>.
   * </p>
   *
   * @param string
   *          the String to pad out, may be null
   * @param padChar
   *          the character to pad with
   * @param maxPadSize
   *          the size to pad to
   * @return Right padded String or original String if no padding is necessary, <code>null</code> if null String input
   *
   * @see org.apache.commons.lang.StringUtils#rightPad(String, int, char)
   */
  public static String padWithUpTo(String string, char padChar, int maxPadSize) {
    return padWithUpTo(string, padChar, maxPadSize, false);
  }

  /**
   * <p>
   * Pad a String with a specified character at left/right side.
   * </p>
   *
   * <p>
   * The String is padded to the size of <code>maxPadSize</code>.
   * </p>
   *
   * @param string
   *          the String to pad out, may be null
   * @param padChar
   *          the character to pad with
   * @param maxPadSize
   *          the size to pad to
   * @param isBefore
   *          pad at left or right
   * @return Padded String or original String if no padding is necessary, <code>null</code> if null String input
   *
   * @see org.apache.commons.lang.StringUtils#leftPad(String, int, char)
   * @see org.apache.commons.lang.StringUtils#rightPad(String, int, char)
   */
  public static String padWithUpTo(String string, char padChar, int maxPadSize, boolean isBefore) {
    if (string == null) {
      return null;
    }
    if (string.length() >= maxPadSize) {
      return string;
    }
    final char[] buf = new char[maxPadSize - string.length()];
    Arrays.fill(buf, padChar);

    return isBefore ? new String(buf).concat(string) : string.concat(new String(buf));
  }

  /**
   * <p>
   * Parse a {@link BigInteger} from a {@link String} value. Ignore all non-digit characters.
   * </p>
   *
   * @param str
   *          string to parse
   * @return the BigInteger parsed from passed string.
   * @throws NumberFormatException
   *           <tt>str</tt> is not a valid representation of a BigInteger after ignore non-digit characters
   * @see BigInteger#BigInteger(String)
   */
  public static BigInteger parseBigIntegerIgnoreNonDigits(String str) {
    String patternStr = "[^0-9]";
    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(str);
    return new BigInteger(matcher.replaceAll(EMPTY_STRING));
  }

  /**
   * <p>
   * Case insensitive check if a String starts with a specified prefix. This method is not null safe.
   * </p>
   *
   * @see String#startsWith(String)
   *
   * @param prefix
   * @param text
   * @return
   */
  public static boolean prefixMatch(String prefix, String text) {
    return text.toUpperCase().indexOf(prefix.toUpperCase()) == 0;
  }
  
  /**
   * <p>
   * Replaces non-alpha number character with empty string inside a larger String.
   * </p>
   * </p>
   *
   * <pre>
   * StringUtil.removeNonNumericAndAlphabetChar(null, *)         = null
   * StringUtil.removeNonNumericAndAlphabetChar(&quot;&quot;, *)           = &quot;&quot;
   * StringUtil.removeNonNumericAndAlphabetChar(&quot;a/aa&quot;)  = &quot;aaa&quot;
   * </pre>
   *
   * @param input
   *          text to search and replace in, may be null
   * @return the text with any replacements processed,<code>null</code> if null String input
   * @see #replaceNonAlphaNumericWith(String, String)
   */
  public static String removeNonNumericAndAlphabetChar(String input) {
    return replaceNonAlphaNumericWith(input, EMPTY_STRING);
  }

  /**
   * <p>
   * Remove right zero from number string
   * </p>
   *
   * <pre>
   * &quot;10&quot; = StringUtil.removeSuffixZero(&quot;10&quot;)
   * &quot;10&quot; = StringUtil.removeSuffixZero(&quot;10.&quot;)
   * &quot;10&quot; = StringUtil.removeSuffixZero(&quot;10.0&quot;)
   * &quot;0&quot; = StringUtil.removeSuffixZero(&quot;.0&quot;)
   * &quot;12.01&quot; = StringUtil.removeSuffixZero(&quot;12.01&quot;)
   * &quot;12.01&quot; = StringUtil.removeSuffixZero(&quot;12.010&quot;)
   * </pre>
   *
   * @param number
   *          Number String
   * @return number string that remove zero suffix.
   */
  public static String removeSuffixZero(String number) {
    if (number == null || number.length() == 0) {
      return number;
    }
    int end = number.length();
    if (number.indexOf(DOT) > -1) {
      while (number.charAt(end - 1) == ZERO_CHAR || number.charAt(end - 1) == DOT.charAt(0)) {
        end--;
        if (number.charAt(end) == DOT.charAt(0)) {
          break;
        }
      }
      return end == 0 ? ZERO : number.substring(0, end);
    }
    return number;
  }

  /**
   * <p>
   * Return a String with all occurrences of <code>from</code> within <code>orig</code> replaced with <code>to</code>. If <code>orig</code> contains
   * no occurrences of <code>from</code>, or if <code>from</code> is equal to <code>to</code>, <code>orig</code> itself is returned rather than a copy
   * being made. This is not a null-safe version, Please use {@link #replaceAll(String, String, String)} or
   * {@link org.apache.commons.lang.StringUtils#replace(String, String, String)} as replacement.
   * </p>
   *
   * @param orig
   *          the original String, can not be null
   * @param from
   *          the String to replace within <code>orig</code>, can not be null
   * @param to
   *          the String to replace <code>from</code> with, can not be null
   *
   * @returns a version of <code>orig</code> with all occurrences of <code>from</code> being replaced with <code>to</code>.
   *
   * @throws IllegalArgumentException
   *           if <code>from</code> is empty
   *
   * @see org.apache.commons.lang.StringUtils#replace(String, String, String)
   *
   */
  public static String replace(String orig, String from, String to) {
    int fromLength = from.length();
    if (fromLength == 0) {
      throw new IllegalArgumentException("String to be replaced must not be empty");
    }
    int start = orig.indexOf(from);
    if (start == -1) {
      return orig;
    }
    boolean greaterLength = (to.length() >= fromLength);
    StringBuffer buffer;
    // If the "to" parameter is longer than (or
    // as long as) "from", the final length will
    // be at least as large
    if (greaterLength) {
      if (from.equals(to)) {
        return orig;
      }
      buffer = new StringBuffer(orig.length());
    } else {
      buffer = new StringBuffer();
    }
    char[] origChars = orig.toCharArray();
    int copyFrom = 0;
    while (start != -1) {
      buffer.append(origChars, copyFrom, start - copyFrom);
      buffer.append(to);
      copyFrom = start + fromLength;
      start = orig.indexOf(from, copyFrom);
    }
    buffer.append(origChars, copyFrom, origChars.length - copyFrom);
    return buffer.toString();
  }

  /**
   * <p>
   * Replaces all occurrences of a String within another String. Please use
   * {@link org.apache.commons.lang.StringUtils#replace(String, String, String)} as replacement.
   * </p>
   *
   * <p>
   * A <code>null</code> reference passed to this method is a no-op.
   * </p>
   *
   * <pre>
   * StringUtil.replaceAll(null, *, *)        = null
   * StringUtil.replaceAll(&quot;&quot;, *, *)          = &quot;&quot;
   * StringUtil.replaceAll(&quot;any&quot;, null, *)    = &quot;any&quot;
   * StringUtil.replaceAll(&quot;any&quot;, *, null)    = &quot;any&quot;
   * StringUtil.replaceAll(&quot;any&quot;, &quot;&quot;, *)      = &quot;any&quot;
   * StringUtil.replaceAll(&quot;aba&quot;, &quot;a&quot;, null)  = &quot;aba&quot;
   * StringUtil.replaceAll(&quot;aba&quot;, &quot;a&quot;, &quot;&quot;)    = &quot;b&quot;
   * StringUtil.replaceAll(&quot;aba&quot;, &quot;a&quot;, &quot;z&quot;)   = &quot;zbz&quot;
   * </pre>
   *
   * @param str
   *          text to search and replace in, may be null
   * @param src
   *          the String to search for, may be null
   * @param dest
   *          the String to replace it with, may be null
   * @return the text with any replacements processed, <code>null</code> if null String input
   * @see org.apache.commons.lang.StringUtils#replace(String, String, String)
   */
  public static String replaceAll(String str, String src, String dest) {
    if (str == null || str.length() == 0 || src == null || src.length() == 0 || dest == null || str.length() < src.length()) {
      return str;
    }
    StringBuffer sb = new StringBuffer();
    int strL = str.length();
    int srcL = src.length();
    int compareL = strL - srcL + 1;
    boolean equals = false;
    int position = 0;
    for (int i = 0; i < compareL; i++) {
      equals = false;
      if (str.charAt(i) == src.charAt(0)) {
        position = i + 1;
        if (srcL == 1) {
          equals = true;
        }
        for (int j = 1; j < srcL; position++, j++) {
          if (str.charAt(position) == src.charAt(j)) {
            equals = true;
          } else {
            equals = false;
            break;
          }
        }
        if (equals) {
          sb.append(dest);
          i += srcL - 1;
        } else {
          sb.append(str.charAt(i));
        }
      } else {
        sb.append(str.charAt(i));
      }
    }
    if (!equals) {
      for (int i = compareL; i < strL; i++) {
        sb.append(str.charAt(i));
      }
    } else {
      for (int i = position; i < strL; i++) {
        sb.append(str.charAt(i));
      }
    }
    return sb.toString();
  }

  /**
   * <p>
   * Replaces non-alpha number character with another character inside a larger String. We check character using
   * {@link Character#isLetterOrDigit(char)}.
   * </p>
   * </p>
   *
   * <pre>
   * StringUtil.replaceNonAlphaNumericWith(null, *)         = null
   * StringUtil.replaceNonAlphaNumericWith(&quot;&quot;, *)           = &quot;&quot;
   * StringUtil.replaceNonAlphaNumericWith(&quot;a/aa&quot;, 'z')  = &quot;azaa&quot;
   * </pre>
   *
   * @param string
   *          text to search and replace in, may be null
   * @param replacementChar
   *          the character to replace it with
   * @return the text with any replacements processed,<code>null</code> if null String input
   */
  public static String replaceNonAlphaNumericWith(String string, char replacementChar) {
    return replaceNonAlphaNumericWith(string, String.valueOf(replacementChar));
  }

  /**
   * <p>
   * Replaces non-alpha number character with another string inside a larger String. We check character using {@link Character#isLetterOrDigit(char)}.
   * </p>
   * </p>
   *
   * <pre>
   * StringUtil.replaceNonAlphaNumericWith(null, *)         = null
   * StringUtil.replaceNonAlphaNumericWith(&quot;&quot;, *)           = &quot;&quot;
   * StringUtil.replaceNonAlphaNumericWith(&quot;a/aa&quot;, &quot;z&quot;)  = &quot;azaa&quot;
   * </pre>
   *
   * @param string
   *          text to search and replace in, may be null
   * @param replacement
   *          the string to replace it with, may be null
   * @return the text with any replacements processed,<code>null</code> if null String input
   */
  public static String replaceNonAlphaNumericWith(String string, String replacement) {
    if (isNullOrEmpty(string) || replacement == null)
      return string;
    StringBuffer sb = new StringBuffer(string.length());
    for (int index = 0; index < string.length(); index++) {
      char ch = string.charAt(index);
      if (!Character.isLetterOrDigit(ch)) {
        sb.append(replacement);
      } else {
        sb.append(ch);
      }
    }
    return sb.toString();
  }

  public static String replaceSinlgeQuoteForJavaScript(String originString) {
    return originString.replace(
        StringUtil.SINGLE_QUOTE, StringUtil.BACK_SLASH_STRING + StringUtil.SINGLE_QUOTE);
  }

  /**
   * <p>
   * Replaces a character with another character inside a larger String. This is a null-safe version of {@link String#replace(char, char)}.
   * </p>
   * </p>
   *
   * <pre>
   * StringUtil.replaceWith(null, *, *)         = null
   * StringUtil.replaceWith(&quot;&quot;, *, *)           = &quot;&quot;
   * StringUtil.replaceWith(&quot;abaa&quot;, 'a', 'z')  = &quot;zbzz&quot;
   * </pre>
   *
   * @param string
   *          text to search and replace in, may be null
   * @param searchChar
   *          the character to search for
   * @param replacementChar
   *          the character to replace it with
   * @return the text with any replacements processed,<code>null</code> if null String input
   * @see org.apache.commons.lang.StringUtils#replaceChars(String, char, char)
   */
  public static String replaceWith(String string, char searchChar, char replacementChar) {
    if (isNullOrEmpty(string)) {
      return string;
    }
    return string.replace(searchChar, replacementChar);
  }

  /**
   * <p>
   * Replaces a character with another String inside a larger String.
   * </p>
   *
   * <pre>
   * StringUtil.replaceWith(null, *, *)         = null
   * StringUtil.replaceWith(&quot;&quot;, *, *)           = &quot;&quot;
   * StringUtil.replaceWith(&quot;any&quot;, *, null)     = &quot;any&quot;
   * StringUtil.replaceWith(&quot;abaa&quot;, 'a', &quot;z&quot;)  = &quot;zbzz&quot;
   * </pre>
   *
   * @param string
   *          text to search and replace in, may be null
   * @param searchChar
   *          the character to search for
   * @param replacement
   *          the String to replace it with, may be null
   * @return the text with any replacements processed,<code>null</code> if null String input
   */
  public static String replaceWith(String string, char searchChar, String replacement) {
    if (isNullOrEmpty(string) || replacement == null)
      return string;
    StringBuffer sb = new StringBuffer(string.length());
    for (int index = 0; index < string.length(); index++) {
      char currentChar = string.charAt(index);
      if (currentChar == searchChar) {
        sb.append(replacement);
      } else {
        sb.append(currentChar);
      }
    }
    return sb.toString();
  }

  public static String sbc2DBC(String str) {
    if (!isIncludeSBCCase(str)) {
      return str;
    }
    char[] result = new char[str.length()];
    for (int i = 0; i < str.length(); i++) {
      char code = str.charAt(i);
      if (code >= 65281 && code <= 65373) {// all the char in this area is the english char already
        result[i] = (char) (str.charAt(i) - 65248);// convert the DBC to SBC
      } else {
        switch (code) { // pls refer to Chung's project for details
          case 12288:
            result[i] = (char) (str.charAt(i) - 12288 + 32);
            continue; // for
          case 8212:
            result[i] = (char) (45);
            continue; // for
          case 12290:
            result[i] = (char) (46);
            continue; // for
          case 8216:
            result[i] = (char) (39);
            continue; // for
          case 8217:
            result[i] = (char) (39);
            continue; // for
          case 8220:
            result[i] = (char) (34);
            continue; // for
          case 8221:
            result[i] = (char) (34);
            continue; // for
          case 12308:
            result[i] = (char) (40);
            continue; // for
          case 12309:
            result[i] = (char) (41);
            continue; // for
          case 8242:
            result[i] = (char) (39);
            continue; // for
          case 8243:
            result[i] = (char) (34);
            continue; // for
          case 12289:
            result[i] = (char) (44);
            continue; // for
          default:
            result[i] = str.charAt(i);
        }
      }
    }
    return new String(result);
  }

  private static void splitOneLineTextByWrapFormat(String text, int wrapIndex, List<String> list) {
    int start = 0;
    while (true) {
      if (start + wrapIndex >= text.length()) {
        list.add(text.substring(start, text.length()));
        break;
      }
      String temp = text.substring(start, start + wrapIndex);
      int splitIndex = temp.lastIndexOf(" ");
      if (splitIndex == -1) {
        list.add(temp);
        start = start + wrapIndex;
      } else {
        list.add(temp.substring(0, splitIndex + 1));
        start = start + splitIndex + 1;
      }
    }
  }

  public static boolean stringArrayContains(String[] stringArray, String value) {
    if (StringUtil.isNullOrEmpty(stringArray)) {
      return false;
    }

    for (String string : stringArray) {
      if ((string == null && value == null) || (string != null && string.equals(value))) {
        return true;
      }
    }

    return false;
  }

  public static List<String> subStrings(String str, String delimiter) {
    ArrayList<String> array = new ArrayList<String>();

    if (str != null) {
      int next = 0;
      int index = str.indexOf(delimiter);

      while (index != -1) {
        array.add(str.substring(next, index));
        next = index + 1;
        index = str.indexOf(delimiter, next);
      }
      array.add(str.substring(next, str.length()));
      array.trimToSize();
    }

    return array;
  }

  public static String toCSVString(String str) {
    String localStr = str;
    if (null == localStr) {
      localStr = "";
    }
    if (localStr.contains(",") || localStr.contains("\n")) {
      return "\"" + localStr.replaceAll("\"", "\"\"") + "\"";
    } else {
      return "=\"" + localStr.replaceAll("\"", "\"\"") + "\"";
    }
  }

  public static String toDateStr(Date date, String formatStr, TimeZone timeZone) {
    if (date == null) {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
    sdf.setTimeZone(timeZone);
    return sdf.format(date);
  }

  static public String toDecimalStringFormat(double stringValue) {
    return new DecimalFormat("#,##0.00").format(stringValue);
  }

  static public String toIDStringFormat(double stringValue) {
    return new DecimalFormat("###").format(stringValue);
  }

  public static List<String> toListFromArray(String[] stringArray) {
    if (null != stringArray) {
      List<String> stringList = new ArrayList<String>();
      for (String stringVar : stringArray) {
        stringList.add(stringVar);
      }
      return stringList;
    }
    return null;
  }

  public static String toSQLStringFormat(String value) {
    return isNotNullNorEmpty(value) ? replace(value.trim(), SINGLE_QUOTE, DOUBLE_QUOTE) : EMPTY_STRING;
  }

  /**
   * <p>
   * Removes control characters (char &lt;= 32) from both ends of this String, handling <code>null</code> by returning <code>null</code>.
   * </p>
   *
   * <p>
   * The String is trimmed using {@link String#trim()}. Trim removes start and end characters &lt;= 32. To strip whitespace use {@link #strip(String)}
   * .
   * </p>
   *
   *
   * <pre>
   * StringUtil.trim(null)          = null
   * StringUtil.trim(&quot;&quot;)            = &quot;&quot;
   * StringUtil.trim(&quot;     &quot;)       = &quot;&quot;
   * StringUtil.trim(&quot;abc&quot;)         = &quot;abc&quot;
   * StringUtil.trim(&quot;    abc    &quot;) = &quot;abc&quot;
   * </pre>
   *
   * @param str
   *          the String to be trimmed, may be null
   * @return the trimmed string, <code>null</code> if null String input
   * @see org.apache.commons.lang.StringUtils#trim(String)
   */
  public static String removeSpaceBesidesDelimiter(String str, String delimiter) {
    if (null == str || str.length() == 0) {
      return str;
    }
    if (!str.contains(delimiter)) {
      return str.trim();
    }
    List<String> subTrimString = new ArrayList<String>();
    for (String subString : subStrings(str, delimiter)) {
      subTrimString.add(trim(subString));
    }
    return convertListToStringWithSeperator(subTrimString, delimiter);
  }

  public static String trim(String str) {
    return (null == str) ? null : str.trim();
  }

  public static String trimHtmlSpace(String str) {
    if (null == str || str.length() == 0) {
      return str;
    }
    int len = str.length();
    int startIndex = 0;
    int endIndex = len;

    while (startIndex < len && (str.charAt(startIndex) == ' ' || str.charAt(startIndex) == HTML_SPACE_CHAR)) {
      startIndex++;
    }
    if (startIndex == endIndex) {
      return EMPTY_STRING;
    }
    while (endIndex > 0 && (str.charAt(endIndex - 1) == ' ' || str.charAt(endIndex - 1) == HTML_SPACE_CHAR)) {
      endIndex--;
    }

    return str.substring(startIndex, endIndex);
  }

  public static String unescape(String s) {
    if (isNullOrEmpty(s)) {
      return "";
    }
    StringBuffer sbuf = new StringBuffer();
    int i = 0;
    int len = s.length();
    while (i < len) {
      int ch = s.charAt(i);
      if (ch == '+') { // + : map to ' '
        sbuf.append('+');
      } else if ('A' <= ch && ch <= 'Z') { // 'A'..'Z' : as it was
        sbuf.append((char) ch);
      } else if ('a' <= ch && ch <= 'z') { // 'a'..'z' : as it was
        sbuf.append((char) ch);
      } else if ('0' <= ch && ch <= '9') { // '0'..'9' : as it was
        sbuf.append((char) ch);
      } else if ("-_!~*\\@().".indexOf(ch) >= 0) {
        // } else if (ch == '-'
        // || ch == '_' // unreserved : as it was
        // || ch == '.' || ch == '!' || ch == '~' || ch == '*' || ch == '\'' ||
        // ch == '@'
        // || ch == '(' || ch == ')') {
        sbuf.append((char) ch);
      } else if (ch == '%') {
        int cint = 0;
        if ('u' != s.charAt(i + 1)) { // %XX : map to ascii(XX)
          cint = (cint << 4) | val[s.charAt(i + 1)];
          cint = (cint << 4) | val[s.charAt(i + 2)];
          i += 2;
        } else { // %uXXXX : map to unicode(XXXX)
          cint = (cint << 4) | val[s.charAt(i + 2)];
          cint = (cint << 4) | val[s.charAt(i + 3)];
          cint = (cint << 4) | val[s.charAt(i + 4)];
          cint = (cint << 4) | val[s.charAt(i + 5)];
          i += 5;
        }
        sbuf.append((char) cint);
      }
      i++;
    }
    return sbuf.toString();
  }

  public static String updateStringKeepFirstBlankFilterOutOtherBlanks(String str) {
    if (StringUtil.isNullOrEmpty(str)) {
      return null;
    }
    boolean isFirstBlank = false;
    StringBuilder builder = new StringBuilder();
    char[] allChar = str.toCharArray();
    for (char ch : allChar) {
      if (ch == 0x20) {
        if (isFirstBlank) {
          continue;
        } else {
          isFirstBlank = true;
          builder.append(ch);
        }
      } else {
        builder.append(ch);
      }
    }
    return builder.toString();
  }

  public static List<String> upperCase(List<String> stringValue) {
    List<String> strings = new LinkedList<String>();
    for (String string : stringValue) {
      strings.add(upperCase(string));
    }
    return strings;
  }

  /**
   * <p>
   * Converts a String to upper case as per {@link String#toUpperCase()}.
   * </p>
   *
   * <p>
   * A <code>null</code> input String returns <code>null</code>.
   * </p>
   *
   * <pre>
   * StringUtil.upperCase(null)  = null
   * StringUtil.upperCase(&quot;&quot;)    = &quot;&quot;
   * StringUtil.upperCase(&quot;aBc&quot;) = &quot;ABC&quot;
   * </pre>
   *
   * @param str
   *          the String to upper case, may be null
   * @return the upper cased String, <code>null</code> if null String input
   * @see org.apache.commons.lang.StringUtils#upperCase(String)
   */
  public static String upperCase(String stringValue) {
    return isNullOrEmpty(stringValue) ? stringValue : stringValue.toUpperCase();
  }

  public static String lowerCase(String stringValue) {
    return stringValue == null ? null : stringValue.toLowerCase();
  }

  public static String upperCaseFirstCharacterInString(String str) {
    if (null == str)
      return null;
    String stringLowerCase = str.trim().toLowerCase();
    StringBuilder strBuilder = new StringBuilder();
    char charForAppend;
    if (stringLowerCase.length() > 0) {
      char[] chars = stringLowerCase.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        charForAppend = chars[i];
        if (i == 0) {
          strBuilder.append(Character.toUpperCase(charForAppend));
        } else {
          if (charForAppend == ' ') {
            strBuilder.append(charForAppend);
            strBuilder.append(Character.toUpperCase(chars[++i]));
          } else {
            strBuilder.append(charForAppend);
          }
        }
      }
      return strBuilder.toString();
    } else {
      return str;
    }
  }

  public static String convertStrListToSQLStr(List<String> strList) {
    StringBuilder strBuilder = new StringBuilder();
    String seperator = EMPTY_STRING;
    for (String str : strList) {
      if (StringUtil.isNotNullNorEmpty(str)) {
        strBuilder.append(seperator).append(SINGLE_QUOTE).append(str).append(SINGLE_QUOTE);
        seperator = COMMA;
      }
    }
    return strBuilder.length() > 0 ? strBuilder.toString() : EMPTY_STRING;
  }

  public static List<String> convertSQLStrToStrList(String sqlStr) {
    List<String> sqls = new ArrayList<String>();
    if (isNotNullNorEmpty(sqlStr)) {
      String sqlWithoutSingleQuote = sqlStr.replaceAll(SINGLE_QUOTE, "");
      sqls = Arrays.asList(sqlWithoutSingleQuote.split(COMMA));
    }
    return sqls;
  }

  public static String convertListToStringWithSeperator(List<String> strList, String seperator) {
    return convertListToStringWithSeperatorIncludeNullValue(strList, seperator, false);
  }

  public static String convertSetToStringWithSeperator(Set<String> strList, String seperator) {
    return convertListToStringWithSeperatorIncludeNullValue(strList, seperator, false);
  }

  public static String convertListToStringWithSeperatorIncludeNullValue(Collection<String> strList, String seperator, boolean isIncludeNullValue) {
    StringBuilder strBuilder = new StringBuilder();
    if (strList != null) {
      String tempSeperator = EMPTY_STRING;
      for (String str : strList) {
        if (isIncludeNullValue || StringUtil.isNotNullNorEmpty(str)) {
          strBuilder.append(tempSeperator).append(str);
          tempSeperator = seperator;
        }
      }
    }
    return strBuilder.toString();
  }

  public static double convertCurrencyStringToDouble(String amount) {
    double dAmount = 0.0;
    if (isNotNullNorEmpty(amount)) {
      dAmount = Double.parseDouble(amount.replaceAll(COMMA, ""));
    }
    return dAmount;
  }

  public static boolean matchQueryString(String value, String queryCriteria) {
    boolean isMatched = true;
    String localValue = StringUtil.upperCase(value);
    if (StringUtil.isNotNullNorEmpty(queryCriteria)) {
      isMatched = localValue == null ? false : (StringUtil.isWildcardString(queryCriteria) ? localValue.startsWith(
          StringUtil.upperCase(queryCriteria
          .substring(0, queryCriteria.length() - 1))) : localValue.equals(StringUtil.upperCase(queryCriteria)));
    }
    return isMatched;
  }

  public static String leftTrim(String str, String toTrim) {
    if (isNullOrEmpty(str)) {
      return null;
    }
    if (isNullOrEmpty(toTrim)) {
      return str;
    }
    return str.startsWith(toTrim) ? str.substring(toTrim.length()) : str;
  }

  public static int getFirstMatchStartIndex(String reg, String string) {
    Pattern pattern = Pattern.compile(reg);
    Matcher matcher = pattern.matcher(string);
    boolean isFound = matcher.find();
    return isFound ? matcher.start() : -1;
  }

  public static String replaceStarAndAndCharWithPercentChar(String text) {
    return replaceStarCharWithPercentChar(text).replace(AND_CHAR, PERCENTAGE_CHAR);
  }

  public static String replaceStarCharWithPercentChar(String text) {
    return text.replace(STAR_CHAR, PERCENTAGE_CHAR);
  }

  public static String removePercentChar(String text) {
    if(StringUtil.isNullOrEmpty(text)) {
      return text;
    }
    return text.replace(PERCENTAGE, EMPTY_STRING);
  }

  public static boolean isContainerNumberLengthLargeOrEqualThan10(String containerNumber) {
    return getStringLength(containerNumber) >= CONTAINER_NUMBER_LENGTH;
  }

  public static Set<String> convertStringToSet(String stringForSplit, String delimiter) {
    Set<String> stringSet = new HashSet<String>();
    if (StringUtil.isNullOrEmpty(stringForSplit)) {
      return stringSet;
    }
    for (String singleString : stringForSplit.split(delimiter)) {
      if (StringUtil.isNullOrEmpty(singleString)) {
        continue;
      }
      stringSet.add(singleString.trim());
    }
    return stringSet;
  }

  public static void trim(String[] array) {
    for (int i = 0; i < array.length; i++) {
      array[i] = array[i].trim();
    }
  }

  public static String[] splitStringToArray(String stringValue, String splitter){
    if(isNotNullNorEmpty(stringValue)){
      return stringValue.split(splitter);
    }
    return null;
  }

  public static boolean isStringInParentheses(String strValue) {
    if (StringUtil.isNullOrEmpty(strValue)) {
      return false;
    }
    return strValue.startsWith(LEFT_PARENTHESIS) && strValue.endsWith(RIGHT_PARENTHESIS);
  }

  public static String truncateMaxLength(String keyValue, int maxLength) {
    if (null == keyValue) {
      return "";
    }
    byte[] bstr = keyValue.getBytes();
    if(bstr.length <= maxLength) {
      return keyValue;
    }
    return new String(bstr, 0, maxLength);
  }

}
