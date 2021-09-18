package me.greenpilot.zook.commands;

import me.greenpilot.zook.helpers.Command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.Random;

public class Coinflip implements Command {

    @Override
    public void run(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        String user = event.getAuthor().getAsMention();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Coinflip");
        embed.setDescription("**Flipping!**");
        embed.setThumbnail("https://s3-eu-west-1.amazonaws.com/onlinebingo/upload/coin-toss-YkQK9LE/coin-toss");

        channel.sendMessageEmbeds(embed.build()).queue(msg -> {
            String flip = flipCoin();

            if (flip.equals("**Heads!**")) embed.setThumbnail("https://cdn.discordapp.com/attachments/793704522265657345/886409035421741066/heads.png");
            else if (flip.equals("**Tails!**")) embed.setThumbnail("https://cdn.discordapp.com/attachments/793704522265657345/886409018187329587/tails.png");

            if (args.isEmpty()) embed.setDescription(flip);
            else if (args.get(0).equals("heads" ) & flip.equals("**Heads!**")) embed.setDescription(flip + " " + user + " You Win!");
            else if (args.get(0).equals("heads" ) & flip.equals("**Tails!**")) embed.setDescription(flip + " " + user + " You Lose!");
            else if (args.get(0).equals("tails" ) & flip.equals("**Tails!**")) embed.setDescription(flip + " " + user + " You Win!");
            else if (args.get(0).equals("tails" ) & flip.equals("**Heads!**")) embed.setDescription(flip + " " + user + " You Lose!");
            else embed.setDescription(flip);

            try {
                Thread.sleep(650);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            msg.editMessageEmbeds(embed.build()).queue();
        });
    }

    private static String flipCoin() {
        Random random = new Random();
        int flipNum = random.nextInt(2);

        if (flipNum == 0) return "**Heads!**";
        else if (flipNum == 1) return "**Tails!**";
        else return null;
    }

    @Override
    public String getCommand() {
        return "coinflip";
    }

    @Override
    public String getHelp() {
        return "Flips a coin!\n" +
                "Usage: `" + getCommand() + "`";
    }
}