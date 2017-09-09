package com.apabi.center.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by atom on 17-9-9.
 */
public class CertUtil {
    /**
     * get Cert from cookie
     * @param request
     * @return
     */
    public static String getCertFromCookie(HttpServletRequest request) {
        Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = request.getCookies();
        String cert = null;

        if(null != cookies){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
            if (cookieMap.get("cert") != null) {
                cert = cookieMap.get("cert").getValue();
            }
        }

        return cert;
    }
}
