package cn.evole.mods.mcbot.util.onebot;

import cn.evole.mods.mcbot.PlatformHelper;
import cn.evole.mods.mcbot.common.config.ModConfig;
import cn.evole.onebot.sdk.entity.ArrayMsg;
import cn.evole.onebot.sdk.event.message.MessageEvent;
import cn.evole.onebot.sdk.util.GsonUtils;
import com.google.gson.reflect.TypeToken;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static cn.evole.mods.mcbot.Constants.LOGGER;
import static cn.evole.mods.mcbot.Constants.cqExecutor;

/**
 * @Project: McBot
 * @Author: cnlimiter
 * @CreateTime: 2024/10/27 19:57
 * @Description:
 */
public class CQUtils {

    /**
     * @param timeout 超时时间（毫秒），超时后返回空字符串。
     */
    public static @NotNull String replace(@NotNull MessageEvent event, long timeout) {
        String back = "";
        val task = new FutureTask<>(() -> doReplace(event));
        try {
            cqExecutor.execute(task);
            back = task.get(timeout, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException | RejectedExecutionException e) {
            task.cancel(true);
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            LOGGER.error("解析 OneBot 消息失败", e);
        }
        return back;
    }

    private static @NotNull String doReplace(MessageEvent event) {
        val stringMsg = event.getMessage();
        if (stringMsg == null || stringMsg.isBlank()) return "";
        val message = new StringBuilder();
        List<ArrayMsg> msg = GsonUtils.fromJson(stringMsg, new TypeToken<List<ArrayMsg>>() {}.getType());
        if (msg == null) return stringMsg;
        for (ArrayMsg arrayMsg : msg){
            if (arrayMsg == null || arrayMsg.getData() == null) continue;
            if (arrayMsg.getType() == null) {
                message.append("[?]");
                continue;
            }
            switch (arrayMsg.getType()){
                case text -> {
                    String text = arrayMsg.getData().get("text");
                    if (text != null) message.append(text);
                }
                case image -> {
                    if (ModConfig.get().getCommon().getImageOn().getValue()
                            && PlatformHelper.isModLoaded("chatimage")
                            && arrayMsg.getData().get("url") != null
                    ) {
                        message.append(String.format("[[CICode,url=%s,name=来自QQ的图片]]",
                                arrayMsg.getData().get("url").replaceAll("&amp;", "&")//转义字符转义
                        ));
                    } else {
                        message.append("[图片]");
                    }
                }
                case at -> {
                    val qq = arrayMsg.getData().get("qq");
                    message.append(formatAt(qq, arrayMsg.getData().get("name")));
                }
                case reply -> {
                }
                default -> message.append("[?]");
            }
        }
        return message.toString();
    }

    static String formatAt(String qq, String name) {
        if ("all".equalsIgnoreCase(qq)) return "[@全体]";
        String display = name == null || name.isBlank() || "null".equalsIgnoreCase(name) ? qq : name;
        return display == null || display.isBlank() ? "[@]" : "[@" + display + "]";
    }
}
