package com.example.project_4t_tople.helper;

import java.util.regex.Pattern;

// ch03/9PatternCheckerExam
public class RegexHelper {
    // 싱글톤 패턴 시작
    private static RegexHelper instance = null;

    public static RegexHelper getInstance() {
        if(instance == null) instance = new RegexHelper();

        return instance;
    }
    public static void freeInstance() {
        instance = null;
    }
    // 생성자를 private으로 만들면, new에 의한 객체 생성이 불가능해진다.
    private RegexHelper() {}
    // 싱글톤 패턴 끝

    /**
     *  주어진 문자열이 공백이거나 null 인지 검사
     * @param str   : 검사할 문자열
     * @return boolean : 공백, null이 아닌 경우 - true 리턴
     */

    public boolean isValue(String str) {
        boolean result = false;
        if (str != null) {
            result = !str.trim().equals(""); // 공백, null이 아닌 경우
        }
        return result;
    }

    /**
     * 문자열이 숫자만으로 구성되었는지 검사
     * @param str : 검사할 문자열
     * @return boolean : 형식에 맞을 경우 - true, 맞지 않을 경우 - false
     */
    public boolean isNum(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^[0-9]*$", str); // 숫자만인지 검사
        }
        return result;
    }

    /**
     * 문자열이 알파벳만으로 구성되었는지 검사
     * @param str : 검사할 문자열
     * @return boolean : 형식에 맞을 경우 - true, 맞지 않을 경우 - false
     */
    public boolean isEng(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^[a-zA-Z]*$", str); // 숫자만인지 검사
        }
        return result;
    }

    /**
     * 문자열이 한글만으로 구성되었는지 검사
     * @param str : 검사할 문자열
     * @return boolean : 형식에 맞을 경우 - true, 맞지 않을 경우 - false
     */
    public boolean isKor(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^[ㄱ-ㅎ가-힣]*$", str); // 숫자만인지 검사
        }
        return result;
    }

    /**
     * 문자열이 숫자와 알파벳만으로 구성되었는지 검사
     * @param str : 검사할 문자열
     * @return boolean : 형식에 맞을 경우 - true, 맞지 않을 경우 - false
     */
    public boolean isEngNum(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^[a-zA-Z0-9]*$", str); // 숫자만인지 검사
        }
        return result;
    }
    /**
     * 문자열이 숫자와 한글만으로 구성되었는지 검사
     * @param str : 검사할 문자열
     * @return boolean : 형식에 맞을 경우 - true, 맞지 않을 경우 - false
     */
    public boolean isKorNum(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^[ㄱ-ㅎ가-힣0-9]*$", str); // 숫자만인지 검사
        }
        return result;
    }
    /**
     * 문자열이 이메일 형식에 맞는지 검사
     * @param str : 검사할 문자열
     * @return boolean : 형식에 맞을 경우 - true, 맞지 않을 경우 - false
     */
    public boolean isEmail(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", str); // 숫자만인지 검사
        }
        return result;
    }
    /**
     * 문자열이 "-" 없는 핸드폰 형식에 맞는지 검사
     * @param str : 검사할 문자열
     * @return boolean : 형식에 맞을 경우 - true, 맞지 않을 경우 - false
     */
    public boolean isCellPhone(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", str); // 숫자만인지 검사
        }
        return result;
    }

}
