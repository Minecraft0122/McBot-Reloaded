package cn.evole.mods.mcbot.common.command;

import cn.evole.mods.mcbot.common.config.ModConfig;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.val;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class DelGroupIDCommand {


    public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        val id = context.getArgument("GroupId", Long.class);
        if (ModConfig.get().getCommon().getGroupIdList().getValue().contains(String.valueOf(id))) {
            ModConfig.get().getCommon().removeGroupId(id);
            context.getSource().sendSuccess(() -> Component.literal("已成功删除QQ群号：" + id + "！"), true);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("QQ群号 " + id + " 不在互通列表中！"), true);
        }
        
        return 1;
    }


}
