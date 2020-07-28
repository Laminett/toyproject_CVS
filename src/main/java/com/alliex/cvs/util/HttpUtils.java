package com.alliex.cvs.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class HttpUtils {

    public static String requestParametersToString(HttpServletRequest request) {

        StringBuilder sb = new StringBuilder();

        Set<Map.Entry<String, String[]>> entries = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entries) {
            sb.append(entry.getKey()).append("=");

            if (entry.getValue().length == 1) {
                sb.append(entry.getValue()[0]);
            } else {
                sb.append(Arrays.toString(entry.getValue()));
            }

            sb.append(", ");
        }

        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        return "HttpServletRequest[" + sb.toString() + "]";
    }
}