package me.greenpilot.zook.commands.admin;

import me.greenpilot.zook.helpers.Command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.ChannelManager;

import java.util.List;

public class Nuke implements Command {

    @Override
    public void run(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE)){
            int channelPos = event.getChannel().getPosition();

            event.getChannel().createCopy().queue(channel -> {
                ChannelManager manager = channel.getManager();
                manager.setPosition(channelPos).queue();
                channel.sendMessage(":warning:Nuked channel:warning:\nhttps://imgur.com/a/93vq9R8").queue();
            });

            event.getChannel().delete().queue();
        }
    }

    @Override
    public String getCommand() {
        return "nuke";
    }

    @Override
    public String getHelp() {
        return "Deletes and remakes the channel!\n" +
                "Usage: `" + getCommand() + "`";
    }
}