package com.yjpapp.stockportfolio.util;

import android.util.Log;

public class ChoSungSearchQueryUtil {
    public static final int EVENT_CODE_LENGTH = 6;

    public static final int DIGIT_BEGIN_UNICODE = 0x30; //0
    public static final int DIGIT_END_UNICODE = 0x3A; //9

    public static final int QUERY_DELIM = 39;//
    public static final int LARGE_ALPHA_BEGIN_UNICODE = 0;

    public static final int HANGUL_BEGIN_UNICODE = 0xAC00; // 가
    public static final int HANGUL_END_UNICODE = 0xD7A3; //
    public static final int HANGUL_CHO_UNIT = 588; //한글 초성글자간 간격
    public static final int HANGUL_JUNG_UNIT = 28; //한글 중성글자간 간격

    public static final char[] CHO_SUNG_LIST = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ',
            'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };
    public static final boolean[] CHO_SUNG_SEARCH_LIST = { true, false, true, true, false, true,
            true, true, false, true, false, true, true, false, true, true, true, true, true };

    /**
     * 문자를 유니코드(10진수)로 변환 후 반환한다.
     * @param ch 문자
     * @return 10진수 유니코드
     */
    public static int convertCharToUnicode(char ch) {
        return (int) ch;
    }

    /**
     * 10진수를 16진수 문자열로 변환한다.
     * @param decimal 10진수 숫자
     * @return 16진수 문자열
     */
    private static String toHexString(int decimal) {
        Long intDec = Long.valueOf(decimal);
        return Long.toHexString(intDec);
    }

    /**
     * 유니코드(16진수)를 문자로 변환 후 반환한다.
     * @param hexUnicode Unicode Hex String
     * @return 문자값
     */
    public static char convertUnicodeToChar(String hexUnicode) {
        return (char) Integer.parseInt(hexUnicode, 16);
    }

    /**
     * 유니코드(10진수)를 문자로 변환 후 반환한다.
     * @param unicode
     * @return 문자값
     */
    public static char convertUnicodeToChar(int unicode) {
        return convertUnicodeToChar(toHexString(unicode));
    }

    /**
     * 검색 문자열을 파싱해서 SQL Query 조건 문자열을 만든다.
     * @param strSearch 검색 문자열
     * @return SQL Query 조건 문자열
     */
    public static String makeQuery(String strSearch, String columnName){
        strSearch = strSearch == null ? "null" : strSearch.trim();

        StringBuilder retQuery = new StringBuilder();

        int nChoPosition;
        int nNextChoPosition;
        int StartUnicode;
        int EndUnicode;

        int nQueryIndex = 0;
//            boolean bChosung = false;
        StringBuilder query = new StringBuilder();
        for( int nIndex = 0 ; nIndex < strSearch.length() ; nIndex++ ){
            nChoPosition = -1;
            nNextChoPosition = -1;
            StartUnicode = -1;
            EndUnicode = -1;

            if( strSearch.charAt(nIndex) == QUERY_DELIM )
                continue;

            if( nQueryIndex != 0 ){
                query.append(" AND ");
            }

            for(int nChoIndex = 0; nChoIndex < CHO_SUNG_LIST.length ; nChoIndex++ ){
                if( strSearch.charAt(nIndex) == CHO_SUNG_LIST[nChoIndex] ){
                    nChoPosition = nChoIndex;
                    nNextChoPosition = nChoPosition+1;
                    for(; nNextChoPosition < CHO_SUNG_SEARCH_LIST.length ; nNextChoPosition++ ){
                        if( CHO_SUNG_SEARCH_LIST[nNextChoPosition] )
                            break;
                    }
                    break;
                }
            }

            if( nChoPosition >= 0 ){ //초성이 있을 경우
//                    bChosung = true;
                StartUnicode = HANGUL_BEGIN_UNICODE + nChoPosition*HANGUL_CHO_UNIT;
                EndUnicode = HANGUL_BEGIN_UNICODE + nNextChoPosition*HANGUL_CHO_UNIT;
            }
            else{
                int Unicode = convertCharToUnicode(strSearch.charAt(nIndex));
                if( Unicode >= HANGUL_BEGIN_UNICODE && Unicode <= HANGUL_END_UNICODE){
                    int Jong = ((Unicode-HANGUL_BEGIN_UNICODE)%HANGUL_CHO_UNIT)%HANGUL_JUNG_UNIT;

                    if( Jong == 0 ){// 초성+중성으로 되어 있는 경우
                        StartUnicode = Unicode;
                        EndUnicode = Unicode+HANGUL_JUNG_UNIT;
                    }
                    else{
                        StartUnicode = Unicode;
                        EndUnicode = Unicode;
                    }
                }
            }

            //Log.d("SearchQuery","query "+strSearch.codePointAt(nIndex));
            if( StartUnicode > 0 && EndUnicode > 0 ){
                if( StartUnicode == EndUnicode ){
                    query.append("substr(" + columnName + ","+(nIndex+1)+",1)='"+strSearch.charAt(nIndex)+"'");
//                    query.append("substr(name,"+(nIndex+1)+",1)='"+strSearch.charAt(nIndex)+"'");
                }
                else{
                    query.append("(substr("+columnName+","+(nIndex+1)+",1)>='"+convertUnicodeToChar(StartUnicode)
                            +"' AND substr("+columnName+","+(nIndex+1)+",1)<'"+convertUnicodeToChar(EndUnicode)+"')");
//                    query.append("(substr(name,"+(nIndex+1)+",1)>='"+convertUnicodeToChar(StartUnicode)
//                            +"' AND substr(name,"+(nIndex+1)+",1)<'"+convertUnicodeToChar(EndUnicode)+"')");
                }
            }
            else{
                if( Character.isLowerCase(strSearch.charAt(nIndex))){ //영문 소문자
//                    query.append("(substr(name,"+(nIndex+1)+",1)='"+strSearch.charAt(nIndex)+"'"
//                            + " OR substr(name,"+(nIndex+1)+",1)='"+Character.toUpperCase(strSearch.charAt(nIndex))+"')");
                    query.append("(substr("+columnName+","+(nIndex+1)+",1)='"+strSearch.charAt(nIndex)+"'"
                            + " OR substr("+columnName+","+(nIndex+1)+",1)='"+Character.toUpperCase(strSearch.charAt(nIndex))+"')");
                }
                else if( Character.isUpperCase(strSearch.charAt(nIndex))){ //영문 대문자
                    query.append("(substr("+columnName+","+(nIndex+1)+",1)='"+strSearch.charAt(nIndex)+"'"
                            + " OR substr("+columnName+","+(nIndex+1)+",1)='"+Character.toLowerCase(strSearch.charAt(nIndex))+"')");
                }
                else //기타 문자
                    query.append("substr("+columnName+","+(nIndex+1)+",1)='"+strSearch.charAt(nIndex)+"'");
            }
            nQueryIndex++;
        }

        if(query.length() > 0 && strSearch != null && strSearch.trim().length() > 0) {
            retQuery.append("("+query.toString()+")");

            if(strSearch.contains(" ")) {
                // 공백 구분 단어에 대해 단어 모두 포함 검색
                String[] tokens = strSearch.split(" ");
                retQuery.append(" OR (");
                for(int i=0, isize=tokens.length; i<isize; i++) {

                    String token = tokens[i];
                    Log.d("token",tokens[i]);
                    if(i != 0) {
                        retQuery.append(" AND ");
                    }
                    retQuery.append(columnName+" LIKE '%"+token+"%'");
                }
                retQuery.append(")");
            } else {
                retQuery.append(" OR "+columnName+" LIKE '%"+strSearch+"%'");
            }
        } else {
            retQuery.append(query.toString());
        }
//        }
        //Log.d("SearchQuery","query "+query.toString());
        return retQuery.toString();
    }

}