package me.greenpilot.zook.helpers;

import com.mongodb.client.*;
import com.mongodb.client.model.Updates;

import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public abstract class MongoDB {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDB.class);

    private static final MongoClient mongoClient = MongoClients.create(Config.get("MONGODB"));
    protected static MongoDatabase database = mongoClient.getDatabase("antizook");
    protected static MongoCollection<Document> collection = database.getCollection("guild-data");

    public static void makeGuildDoc(String guild, String guild_name) {
        Document guildData = new Document("guildId", guild)
                .append("guildName", guild_name)
                .append("admins", Collections.emptyList());

        collection.insertOne(guildData);
    }

    public static boolean checkGuildStatus(String guild) {
        final boolean[] foundGuild = {false};
        Consumer<Document> printConsumer = new Consumer<Document>() {
            @Override
            public void accept(final Document document) {
                if(document.toJson() != null)
                    foundGuild[0] = true;
            }
        };
        collection.find(eq("guildId", guild))
                .forEach(printConsumer);
        return foundGuild[0];
    }

    public static void addAdmin(String guild, String uid) {
        collection.updateOne(
                eq("guildId",guild),
                addToSet("admins", uid)
        );
    }
    public static void removeAdmin(String guild, String uid) {
        Bson query =Filters.eq("guildId", guild);
        Bson delete = Updates.pull("admins", uid);

        collection.updateOne(query, delete);
    }
    public static Boolean checkAdmin(String guild, String uid) {
        final boolean[] isAdmin = {false};
        Consumer<Document> printConsumer = new Consumer<Document>() {
            @Override
            public void accept(final Document document) {
                if(document.toJson() != null)
                    isAdmin[0] = true;
            }
        };
        collection.find(and(eq("guildId", guild), eq("admins", uid)))
                .forEach(printConsumer);
        return isAdmin[0];
    }
    public static void setTarget(String guild, String uid) {
        collection.updateOne(
                eq("guildId",guild),
                set("target", uid)
        );
    }
    public static Boolean checkTarget(String guild, String uid) {
        final boolean[] isTarget = {false};
        Consumer<Document> printConsumer = new Consumer<Document>() {
            @Override
            public void accept(final Document document) {
                if(document.toJson() != null)
                    isTarget[0] = true;
            }
        };
        collection.find(and(eq("guildId", guild), eq("target", uid)))
                .forEach(printConsumer);
        return isTarget[0];
    }
    public static void setRoles(String guild, String member, List<String> roles) {
        collection.updateOne(
                eq("guildId",guild),
                addToSet("memberRoles", new Document(member, Collections.singletonList(roles)))
        );
    }
    public static List<String> checkRoles(String guild, String member) {
        FindIterable<Document> coll_roles = collection.find(eq("guildId", guild));
        List<String> roles = new ArrayList<String>();

        for(Document doc : coll_roles) {
            Document memberRoles = (Document) doc.get("memberRoles");
        }
        return roles;
    }
}