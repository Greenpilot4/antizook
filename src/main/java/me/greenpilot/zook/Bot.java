package me.greenpilot.zook;

import me.greenpilot.zook.helpers.Config;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Bot {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(Config.get("TOKEN"));

        builder.addEventListeners(new Listener());
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);
        builder.enableCache(CacheFlag.VOICE_STATE);
        builder.setActivity(Activity.watching("you"));

        builder.build();
    }
}

