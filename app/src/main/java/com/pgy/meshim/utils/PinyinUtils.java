package com.pgy.meshim.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * pinyin4j.jar工具的封装类
 * 解析汉字首字母
 *
 * @author yeliangliang
 * @ClassName: PinyinUtils
 * @date 2015-7-27 下午5:15:01
 */
public class PinyinUtils {
//	public static String getAlpha(String chines) {
//		String pinyinName = "";
//		char[] nameChar = chines.toCharArray();
//		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//		for (int i = 0; i < nameChar.length; i++) {
//			if (nameChar[i] > 128) {
//				try {
//					pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
//				} catch (BadHanyuPinyinOutputFormatCombination e) {
//					e.printStackTrace();
//				}
//			} else {
//				pinyinName += nameChar[i];
//			}
//		}
//		return pinyinName;
//	}

    /**
     * 解析为首字母并且按照A-Z排序
     *
     * @param inputString
     * @return String
     * @author yeliangliang
     * @date 2015-7-27 下午5:44:18
     * @version V1.0
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input;
        String output = "";
        try {
            input = inputString.trim().toCharArray();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return output;
        }

        try {
            for (int i = 0; i < input.length; i++) {
                if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output += temp[0];
                } else
                    output += Character.toString(input[i]);
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * 获取首字母
     *
     * @param chines
     * @return String
     * @author yeliangliang
     * @date 2015-7-27 下午5:42:31
     * @version V1.0
     */
    public static String converterToFirstSpell(String chines) {
        String pinyinName = "";
        char[] nameChar;
        try {
            nameChar = chines.toCharArray();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return pinyinName.toUpperCase();
        }
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0]
                            .charAt(0);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else if (isLetter(nameChar[i])) {
                pinyinName += nameChar[i];
            } else {
                pinyinName += (char) 123;
            }
        }
        return pinyinName.toUpperCase();
    }

    private static boolean isLetter(char letter) {
        return (letter >= 65 && letter <= 90) || (letter >= 97 && letter <= 122);
    }
}
