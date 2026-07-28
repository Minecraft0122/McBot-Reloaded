package cn.evolvefield.mods.botapi.util.onebot;

import cn.evolvefield.mods.botapi.init.handler.ConfigHandler;
import cn.evolvefield.onebot.sdk.util.BotUtils;
import cn.evolvefield.onebot.sdk.util.RegexUtils;
import lombok.val;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Project: Bot-Connect-fabric-1.18
 * Author: cnlimiter
 * Date: 2023/2/10 1:11
 * Description:
 */
public class CQUtils {

    private final static String CQ_CODE_SPLIT = "(?<=\\[CQ:[^]]{1,99999}])|(?=\\[CQ:[^]]{1,99999}])";

    private final static String CQ_CODE_REGEX = "\\[CQ:([^,\\[\\]]+)((?:,[^,=\\[\\]]+=[^,\\[\\]]*)*)]";


    public static boolean hasImg(String msg) {
        String regex = "\\[CQ:image,[(\\s\\S)]*\\]";
        val p = Pattern.compile(regex);
        val m = p.matcher(msg);
        return m.find();
    }

    public static String replace(String msg) {
        if (msg == null || msg.isEmpty()) return "";
        StringBuilder message = new StringBuilder();
        val matcher = RegexUtils.regexMatcher(CQ_CODE_REGEX, msg);
        int previousEnd = 0;
        while (matcher.find()) {
            message.append(BotUtils.unescape(msg.substring(previousEnd, matcher.start())));
            message.append(replacement(matcher.group(1), matcher.group(2)));
            previousEnd = matcher.end();
        }
        message.append(BotUtils.unescape(msg.substring(previousEnd)));
        return message.toString();
    }

    private static String replacement(String type, String arguments) {
        if ("image".equals(type)) {
            if (!ConfigHandler.cached().getCommon().isImageOn()) return "[图片]";
            val url = Arrays.stream(arguments.split(","))
                    .filter(it -> it.startsWith("url="))
                    .map(it -> BotUtils.unescape(it.substring(it.indexOf('=') + 1)))
                    .findFirst();
            return url.isPresent()
                    ? String.format("[[CICode,url=%s,name=来自QQ的图片]]", url.get())
                    : "[图片]";
        }
        if ("reply".equals(type)) return "[回复]";
        if ("at".equals(type)) return "[@]";
        if ("record".equals(type)) return "[语音]";
        if ("forward".equals(type)) return "[合并转发]";
        if ("video".equals(type)) return "[视频]";
        if ("music".equals(type)) return "[音乐]";
        if ("redbag".equals(type)) return "[红包]";
        if ("poke".equals(type)) return "[戳一戳]";
        if ("face".equals(type)) return "[表情]";
        return "[?]";
    }
}
