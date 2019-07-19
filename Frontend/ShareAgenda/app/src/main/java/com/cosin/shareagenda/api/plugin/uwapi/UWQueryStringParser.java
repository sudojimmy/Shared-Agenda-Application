package com.cosin.shareagenda.api.plugin.uwapi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UWQueryStringParser {
    public static UWCourseQueryInfo parseQueryString(String str) {
        // "+UW" + (at least one space) + (alpha+) + (optional space) + (course number) + (optional space + term)
        String regex = "\\+UW\\p{Space}+(\\p{Alpha}+)\\p{Space}*(\\d+\\p{Alpha}?)\\p{Space}*(\\d+)?";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(str);
        if (!m.matches()) {
            return null;
        }
        UWCourseQueryInfo info = new UWCourseQueryInfo();
        if (m.group(3) == null) {
            info.setTerm(UWEventReader.defaultTerm());
        } else {
            info.setTerm(m.group(3));
        }
        info.setCatalog_number(m.group(2));
        info.setSubject(m.group(1));
        return info;
    }
}
