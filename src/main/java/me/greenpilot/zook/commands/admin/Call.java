package me.greenpilot.zook.commands.admin;

import me.greenpilot.zook.helpers.Command.Command;
import me.greenpilot.zook.utils.EchoHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.security.auth.login.LoginException;
import java.util.List;

public class Call implements Command {
    @Override
    public void run(List<String> args, GuildMessageReceivedEvent event) throws LoginException {
        if (event.getMember().isOwner()){
            if (args.isEmpty()) return;

            Guild guild = event.getGuild();
            VoiceChannel channel = event.getMember().getVoiceState().getChannel();

            Guild guild2 = event.getJDA().getGuildById(args.get(0));
            VoiceChannel channel2 = guild2.getVoiceChannelById(args.get(1));

            AudioManager audioManager = guild.getAudioManager();
            AudioManager audioManager2 = guild2.getAudioManager();

            audioManager.setSelfDeafened(true);

            EchoHandler handler = new EchoHandler();
            EchoHandler handler2 = new EchoHandler();

            audioManager.setReceivingHandler(handler2);
            audioManager.setSendingHandler(handler);

            audioManager.openAudioConnection(channel);

            audioManager2.setReceivingHandler(handler);
            audioManager2.setSendingHandler(handler2);

            audioManager2.openAudioConnection(channel2);
        }
    }

    @Override
    public String getCommand() {
        return "call";
    }

    @Override
    public String getHelp() {
        return "Call's another server.\n" +
                "Usage: `" + getCommand() + "`";
    }
}
