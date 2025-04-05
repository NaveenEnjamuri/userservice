package com.examportal.configuration;

import org.apache.commons.lang.StringUtils;

public enum Auth {
    WEB("WEB"), MOBILE("MOBILE"), OTHER("OTHER");

    private final String channel;

    Auth(String channel) {
        this.channel = channel;
    }

    public static boolean isWeb(String channel) {
        return !StringUtils.isBlank(channel) && channel.equalsIgnoreCase(Auth.WEB.channel);
    }

    public static boolean isMobile(String channel) {
        return !StringUtils.isBlank(channel) && channel.equalsIgnoreCase(Auth.MOBILE.channel);
    }

    public static boolean isOthers(String channel) {
        return !StringUtils.isBlank(channel) && channel.equalsIgnoreCase(Auth.OTHER.channel);
    }
}
