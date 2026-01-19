package com.remake.poki.utils;

public final class WsTopics {

    public static final String APP_PREFIX = "/app";
    public static final String TOPIC_PREFIX = "/topic";
    public static final String QUEUE_PREFIX = "/queue";
    public static final String SLASH = "/";

    public static final String ONLINE_USERS = QUEUE_PREFIX + "/online-users";
    public static final String CHAT = TOPIC_PREFIX + "/chat";

    public static String room(String roomId) {
        return TOPIC_PREFIX + "/rooms/" + roomId;
    }
}
