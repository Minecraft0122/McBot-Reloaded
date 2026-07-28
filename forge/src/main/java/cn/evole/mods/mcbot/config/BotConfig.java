package cn.evole.mods.mcbot.config;

import cn.evole.config.toml.AutoLoadTomlConfig;
import cn.evole.config.toml.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import org.tomlj.TomlTable;

/**
 * OneBot 连接配置。
 *
 * @author cnlimiter
 * @author Minecraft0122
 */
@Getter
@Setter
public class BotConfig extends AutoLoadTomlConfig {

    @TableField(rightComment = "WebSocket 地址（支持域名和 IPv6）")
    private String url = "ws://127.0.0.1:8080";
    @TableField(rightComment = "鉴权令牌")
    private String token = "";
    @TableField(rightComment = "是否使用 Mirai 鉴权方式")
    private boolean mirai = false;
    @TableField(rightComment = "机器人 QQ 号")
    private long botId = 0L;
    @TableField(rightComment = "是否自动重连")
    private boolean reconnect = true;
    @TableField(rightComment = "自动重连最大次数")
    private int maxReconnectAttempts = 5;
    @TableField(rightComment = "超时补偿（毫秒）")
    private long timeoutCompensation = 5000;

    public BotConfig() {
        super(null);
    }

    public BotConfig(TomlTable source) {
        super(source);
        this.load(BotConfig.class);
    }

    public cn.evole.onebot.client.core.BotConfig build() {
        return new cn.evole.onebot.client.core.BotConfig(
                normalizeWebSocketUrl(url), token, botId, mirai, reconnect, 5, maxReconnectAttempts);
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
