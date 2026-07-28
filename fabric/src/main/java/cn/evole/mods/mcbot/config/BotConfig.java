package cn.evole.mods.mcbot.config;

import lombok.Getter;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

/**
 * Name: McBot-fabric / BotConfig
 * Author: cnlimiter
 * CreateTime: 2023/11/7 20:28
 * Description:
 */

@Getter
@Setter
@ConfigSerializable
public class BotConfig{

    @Comment("地址（支持域名和ipv6）")
    private String url = "ws://127.0.0.1:8080";
    @Comment("鉴权")
    private String token = "";
    @Comment("mirai鉴权方式不一样")
    private boolean mirai = false;
    @Comment("机器人qq")
    private long botId = 0L;//机器人qq
    @Comment("自动重连")
    private boolean reconnect = false;
    @Comment("自动重连次数")
    private int maxReconnectAttempts = 3;
    @Comment("超时宽容度（毫秒）")
    private long timeoutCompensation = 1000;

    public cn.evole.onebot.client.core.BotConfig build(){
        return new cn.evole.onebot.client.core.BotConfig(normalizeWebSocketUrl(url), token, botId, mirai, reconnect, maxReconnectAttempts);
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
