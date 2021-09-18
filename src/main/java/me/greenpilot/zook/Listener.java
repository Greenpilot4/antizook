package me.greenpilot.zook;

import me.greenpilot.zook.helpers.Command.CommandManager;
import me.greenpilot.zook.helpers.MongoDB;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.Objects;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    public final CommandManager commandManager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());

        for (Guild guild: event.getJDA().getGuilds()) {
            String guildId = guild.getId();
            if (!MongoDB.checkGuildStatus(guildId)) MongoDB.makeGuildDoc(guildId, guild.getName());
        }
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        String guild = event.getGuild().getId();
        String guild_name = event.getGuild().getName();

        MongoDB.makeGuildDoc(guild, guild_name);
    }

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        if(event.getMember().getUser().isBot())
            return;

        if(!MongoDB.checkTarget(event.getGuild().getId(), event.getMember().getId()))
            return;

        VoiceChannel connectedChannel = Objects.requireNonNull(event.getMember().getVoiceState()).getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        audioManager.openAudioConnection(connectedChannel);

        LOGGER.info("Joined channel with {}", event.getMember().getUser().getAsTag());
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        if(event.getMember().getUser().isBot())
            return;

        if(!MongoDB.checkTarget(event.getGuild().getId(), event.getMember().getId()))
            return;

        VoiceChannel connectedChannel = Objects.requireNonNull(event.getMember().getVoiceState()).getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        audioManager.openAudioConnection(connectedChannel);

        LOGGER.info("Joined channel with {}", event.getMember().getUser().getAsTag());
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        if(event.getMember().getUser().isBot())
            return;
        if(!MongoDB.checkTarget(event.getGuild().getId(), event.getMember().getId()))
            return;

        AudioManager audioManager = event.getGuild().getAudioManager();

        audioManager.closeAudioConnection();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        try {
            commandManager.run(event);
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}

