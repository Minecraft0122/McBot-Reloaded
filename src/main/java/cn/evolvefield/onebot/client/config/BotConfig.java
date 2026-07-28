package cn.evolvefield.onebot.client.config;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/1 17:05
 * Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotConfig {
    @Expose
    private String url = "ws://127.0.0.1:8080";//websocket地址
    @Expose
    private String token = "";//token或者verifyKey鉴权
    @Expose
    private long botId = 0;
    @Expose
    private boolean isAccessToken = false;//是否开启鉴权
    @Expose
    private boolean miraiHttp = false;//是否开启mirai-http,否则请使用onebot-mirai
    @Expose
    private boolean reconnect = true;//是否开启重连
    @Expose
    private int maxReconnectAttempts = 20;//重连间隔
    public BotConfig(String url){
        this(url, "", 0, false, false, true, 20);
    }

    public BotConfig(String url, long botId){
        this(url, "", botId, false, true, true, 20);
    }

    public static String normalizeWebSocketUrl(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("OneBot WebSocket 地址不能为空");
        }
        String url = value.trim();
        if (url.regionMatches(true, 0, "wss://", 0, 6)) return "wss://" + url.substring(6);
        if (url.regionMatches(true, 0, "ws://", 0, 5)) return "ws://" + url.substring(5);
        if (url.matches("^[A-Za-z][A-Za-z0-9+.-]*://.*$")) {
            throw new IllegalArgumentException("OneBot 地址只支持 ws:// 或 wss:// 协议");
        }
        return "ws://" + url;
    }
}
