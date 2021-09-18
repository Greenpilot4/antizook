package me.greenpilot.zook.commands.admin;

import me.greenpilot.zook.helpers.Command.Command;
import me.greenpilot.zook.helpers.MongoDB;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SetRoles implements Command {
    @Override
    public void run(List<String> args, GuildMessageReceivedEvent event) {
        String guildId = event.getGuild().getId();
        String user = event.getAuthor().getId();
        String message = event.getMessage().getContentRaw();

        String member = message.substring(11);
        String[] rawId = null;

        if(message.contains("<")) {
            rawId = message.split("[<@!>]+");
            member = rawId[1];
        }

        List<String> roles = new ArrayList<String>();
        for(String arg: args) {
            char[] caMainArg = arg.toCharArray();
            String strMainArg = new String(caMainArg);

            String role = strMainArg.substring(11);
            String[] rawRole= null;

            if(strMainArg.contains("<")) {
                rawRole = strMainArg.split("[<@&!>]+");
                role = rawRole[1];
            }

            roles.add(role);
            if (role.equals(user)) roles.remove(role);
        }

        System.out.println(roles);
        MongoDB.setRoles(guildId, member, roles);
    }

    @Override
    public String getCommand() {
        return "setRoles";
    }

    @Override
    public String getHelp() {
        return "Gives you the gateway ping of the bot!\n" +
                "Usage: `" + getCommand() + "`";
    }
}
