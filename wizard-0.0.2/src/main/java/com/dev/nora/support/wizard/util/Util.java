package com.dev.nora.support.wizard.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/15/2016
 */
public class Util {
    public Util() {
    }

    public static String encodeMD5(String link) {
        if(link != null && !"".equals(link)) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] array = md.digest(link.getBytes());
                StringBuilder sb = new StringBuilder();
                byte[] var4 = array;
                int var5 = array.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    byte anArray = var4[var6];
                    sb.append(Integer.toHexString(anArray & 255 | 256).substring(1, 3));
                }

                return sb.toString();
            } catch (NoSuchAlgorithmException var8) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static boolean isListValid(List<?> src) {
        return src != null && src.size() > 0;
    }

    public static int findItem(List<? extends Object> objs, Object obj) {
        if(obj == null) {
            return -1;
        } else {
            if(isListValid(objs)) {
                int index = 0;

                for(Iterator var3 = objs.iterator(); var3.hasNext(); ++index) {
                    Object o = var3.next();
                    if(obj.equals(o)) {
                        return index;
                    }
                }
            }

            return -1;
        }
    }

    public static int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException var2) {
            return 0;
        }
    }
}
