package me.greenpilot.zook.helpers.Command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.security.auth.login.LoginException;
import java.util.List;

public interface Command {
    void run(List<String> args, GuildMessageReceivedEvent event) throws LoginException;
    String getCommand();
    String getHelp();
}
