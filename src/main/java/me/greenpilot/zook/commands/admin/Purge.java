package me.greenpilot.zook.commands.admin;

import me.greenpilot.zook.helpers.Command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.ChannelManager;

import java.util.List;

public class Purge implements Command {

    @Override
    public void run(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE)){
            TextChannel channel = event.getChannel();

            List<Message> messages = channel.getHistory().retrievePast(100).complete();

            messages.removeIf(m -> !m.getContentRaw().startsWith("?"));
            messages.removeIf(m -> !m.getAuthor().isBot());

            for (Message message: messages) {
                message.delete().queue();
            }
            event.getMessage().delete().queue();
        }
    }

    @Override
    public String getCommand() {
        return "purge";
    }

    @Override
    public String getHelp() {
        return "Purges bot messages!\n" +
                "Usage: `" + getCommand() + "`";
    }
}