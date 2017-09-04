/*
 * Copyright (c) 2011-2015. ShenZhen iBOXPAY Information Technology Co.,Ltd.
 *
 * All right reserved.
 *
 * This software is the confidential and proprietary
 * information of iBoxPay Company of China.
 * ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only
 * in accordance with the terms of the contract agreement
 * you entered into with iBoxpay inc.
 * StringUtil.java ,Created by: pengjianbo ,2015-03-14 10:46:54 ,lastModified:2015-03-14 10:46:54
 */

package com.iboxpay.hackandroid;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil {
    /**
     * 判断一个字符串是否为null,空（“”），或者字符的“null”
     *
     * @param str 要判断的字符串
     * @return true表示正常 false表示是三个空中的某一个
     */
    public static boolean checkString(String str) {
        return (null != str && !TextUtils.isEmpty(str) && !str
                .equalsIgnoreCase("null"));
    }

    public static String toYuanByFen(String moneyStr) {
        if (moneyStr == null) {
            return "0";
        }

        moneyStr = moneyStr.replaceAll(",", "");
        if (StringUtil.checkString(moneyStr) && checkMoneyValid(moneyStr)) {
            try {
                double money = new BigDecimal(moneyStr)
                        .divide(new BigDecimal("100"))
                        .setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();

                DecimalFormat df = new DecimalFormat("#,###,##0.00");
                String st = df.format(money);
                return st;
            } catch (NumberFormatException e) {
                return ((Integer.parseInt(moneyStr)) / 100) + "";
            }
        } else {
            return moneyStr;
        }
    }

    public static String toYuanByFen2(String moneyStr) {
        if (moneyStr == null) {
            return "0";
        }

        moneyStr = moneyStr.replaceAll(",", "");
        if (StringUtil.checkString(moneyStr) && checkMoneyValid(moneyStr)) {
            try {
                double money = new BigDecimal(moneyStr)
                        .divide(new BigDecimal("100"))
                        .setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();

                DecimalFormat df = new DecimalFormat("#0.00");
                String st = df.format(money);
                return st;
            } catch (NumberFormatException e) {
                return ((Integer.parseInt(moneyStr)) / 100) + "";
            }
        } else {
            return moneyStr;
        }
    }

    public static String toFenByYuan(String moneyStr) {
        moneyStr = moneyStr.replaceAll(",", "");
        if (moneyStr.endsWith(".")) {
            moneyStr = moneyStr.substring(0, moneyStr.length() - 1);
        }
        if (StringUtil.checkString(moneyStr) && checkMoneyValid(moneyStr)) {
            try {
                return (new BigDecimal(moneyStr)
                        .multiply(new BigDecimal("100")).setScale(0,
                                BigDecimal.ROUND_FLOOR) + "");
            } catch (NumberFormatException e) {

                return ((Integer.parseInt(moneyStr)) * 100) + "";
            }
        } else {
            return moneyStr;
        }
    }

    public static String getMoneyUnit(float yuan) {
        DecimalFormat fnum = new DecimalFormat("##0.0");
        if (yuan >= 10000.f) {
            return fnum.format(yuan / 10000) + "万元";
        }
        return yuan + "元";
    }

    public static String replaceAllBlank(String s) {
        if (checkString(s)) {
            s = s.replaceAll(" ", "");
        }
        return s;
    }

    public static boolean isZeroOnTopOfSrting(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str.substring(0, 1).equals("0")
                    || str.substring(0, 1).equals(".");
        } else {
            return true;
        }
    }

    public static String convertCNum2Asterisk(String cNum) {
        String num = replaceAllBlank(cNum);
        if (checkCreditNum(num)) {
            String firstSix = num.substring(0, 6);
            String lastFour = num.substring(num.length() - 4, num.length());
            return firstSix + "******" + lastFour;
        }
        return num;
    }

    public static String getDateTime(String timeFormat) {
        String result = "NaN";
        if (!checkString(timeFormat)) {
            timeFormat = "yyyy-MM-dd";
        }
        try {
            Date curDate = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
            result = sdf.format(curDate);
        } catch (Exception e) {

        }
        return result;
    }

    public static String separatorCredit(String value) {
        try {
            if (TextUtils.isEmpty(value)) {
                return value;
            }
            String clearValue = clearSeparator(value);
            StringBuffer result = new StringBuffer(clearValue);
            for (int i = 1; i <= (clearValue.length() - 1) / 4; i++) {
                result.insert(i * 4 + i - 1, "-");
            }
            return result.toString();
        } catch (Exception e) {

            return value;
        }
    }

    public static String clearSeparator(String value) {
        try {
            if (TextUtils.isEmpty(value)) {
                return value;
            }
            value = value.replace("-", "");
            return value;
        } catch (Exception e) {

            return value;
        }
    }


    public static String separatorMobile(String mobile) {
        mobile = clearSeparator(mobile);
        try {
            if (TextUtils.isEmpty(mobile)) {
                return mobile;
            }
            if (mobile.length() == 11) {
                mobile = clearSeparator(mobile);
                mobile = String.format("%s-%s-%s", mobile.substring(0, 3),
                        mobile.substring(3, 7), mobile.substring(7));
            }
            return mobile;
        } catch (Exception e) {

            return mobile;
        }
    }

    public static boolean checkChinesePattern(String s) {
        Pattern p = Pattern.compile("[a-zA-Z0-9\u4e00-\u9fa5]{0,}");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * check if String is alphanumeric without punctuation
     *
     * @param s String input
     * @return true if s matches the pattern
     */
    public static boolean isAlphaNumeric(String s) {
        Pattern p = Pattern.compile("[a-zA-Z0-9]+");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static boolean checkMobile(String mobile) {
        try {
            if (mobile == null || mobile.length() != 11) {
                return false;
            } else {
                Pattern p = Pattern.compile("^[1][0-9]{10}$");
                Matcher m = p.matcher(mobile);
                return (m.matches());
            }
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    public static boolean checkMoneyValid(String money) {
        if (StringUtil.checkString(money)) {
            Pattern p = Pattern.compile("^((\\d+)|0|)(\\.(\\d+)$)?");
            Matcher m = p.matcher(money);
            return (m.matches());
        }
        return false;
    }

    public static boolean checkCreditNum(String num) {
        boolean result = false;
        try {
            if (StringUtil.checkString(num) && !StringUtil.isZeroOnTopOfSrting(num)) {
                if (num.length() >= 15 && num.length() <= 19
                        && !StringUtil.isZeroOnTopOfSrting(num)) {
                    result = true;
                }
            }
        } catch (Exception e) {

        }
        return result;
    }


    public static boolean checkPWD(String pwd) {
        try {
            return !(pwd.length() < 6 || pwd.length() > 20);
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    public static boolean checkEmail(String email) {
        try {
            if (email.length() < 1 || email.length() > 30) {
                return false;
            } else {
                Pattern p = Pattern
                        .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
                Matcher m = p.matcher(email);
                return (m.matches());
            }
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    public static boolean checkLoginAccount(String account) {
        //            int index = account.indexOf("#");
//            if (index > 0) {
//                account = account.substring(0, index);
//            }
        return checkString(account) && account.length() <= 30;
    }

    public static String replaceUserNameWithStar(String userName) {
        try {
            if (!checkString(userName) || userName.length() < 2) {
                return userName;
            }
            String star = "";
            int userNameLen = (userName.length() % 2 == 0) ? (userName.length())
                    : (userName.length() - 1);
            int starLen = userNameLen / 2;
            for (int i = 0; i < starLen; ++i) {
                star += "*";
            }
            return (star + userName.substring(starLen));
        } catch (Exception e) {

            return userName;
        }
    }

    public static String replaceUserPhoneNumberCenter(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return mobile;
        }
        if (mobile.length() == 11) {
            char[] m = separatorMobile(mobile).toCharArray();
            String s = "";
            for (int i = 0; i < m.length; i++) {
                if (i > 3 && i < 8) {
                    s += "*";
                } else {
                    s += m[i];
                }
            }

            return s;
        }

        return mobile;
    }


    public static boolean checkPassword(String password) {
        return checkString(password) && password.length() >= 6;
    }

    /**
     * 检测银行卡号是否符合编码规则
     */
    public static boolean checkBankCardLuhm(String cardId) {
        if (checkString(cardId)) {
            char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
            if (bit == 'N') {
                return false;
            }
            return cardId.charAt(cardId.length() - 1) == bit;
        } else
            return false;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     */
    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static String appZero(int num) {

        if (num == -1) {
            return "00";
        }

        if (num < 10) {
            return "0" + num;
        }

        return num + "";
    }


    public static String replaceUserPhoneNumberCenterBase(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return mobile;
        }
        if (mobile.length() == 11) {
            char[] m = mobile.toCharArray();
            String s = "";
            for (int i = 0; i < m.length; i++) {
                if (i > 2 && i < 7) {
                    s += "*";
                } else {
                    s += m[i];
                }
            }

            return s;
        }

        return mobile;
    }

    public static SpannableString formatStringStyle(Context context, String text, int style, int start, int end) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context, style), start, end, 0);
        return spannableString;
    }


    public static String getCallbackUrl(String callbackHost, String paramString) {
        try {
            if (checkString(callbackHost) && checkString(paramString)) {
                StringBuilder callbackUrl = new StringBuilder();
                callbackUrl.append(callbackHost);
                callbackUrl.append("?");
                callbackUrl.append(paramString);
                return callbackUrl.toString();
            }
        } catch (Exception e) {

        }
        return "";
    }

    public static String getParamString(TreeMap<String, String> treeMap) {
        try {
            if (!treeMap.isEmpty()) {
                StringBuilder paramString = new StringBuilder();
                for (Map.Entry<String, String> map : treeMap.entrySet()) {
                    paramString.append(map.getKey());
                    paramString.append("=");
                    paramString.append(map.getValue());
                    paramString.append("&");
                }
                paramString.delete(paramString.length() - 1, paramString.length());
                return paramString.toString();
            }
        } catch (Exception e) {

        }
        return "";
    }

    public static boolean checkURL(String url) {
        if (!checkString(url)) {
            return false;
        }
        String regex = "[a-zA-z]+://[^\\s]*";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(url);
        boolean isMatch = matcher.matches();
        return isMatch;
    }


}
