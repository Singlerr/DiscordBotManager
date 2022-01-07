package io.github.singlerr.handler;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.InviteCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import io.github.singlerr.api.Context;
import io.github.singlerr.api.command.CommandContext;
import io.github.singlerr.api.command.CommandExecutor;
import io.github.singlerr.api.command.CommandManager;
import io.github.singlerr.api.event.EventListener;
import io.github.singlerr.api.event.EventManager;
import lombok.Builder;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscordManager {
    private final String token;
    private static DiscordManager instance;
    private DiscordClient client;
    private GatewayDiscordClient gateway;

    private DiscordManager(String token){
        this.token = token;
    }
    public static DiscordManager createInstance(String token){
        return (instance = new DiscordManager(token) );
    }
    public void listen(){
        client = DiscordClientBuilder.create(token).build();
        gateway = client.login().block();
        for(EventListener<? extends Event> listener : EventManager.getManager().getRegisteredListeners()){
            gateway.getEventDispatcher().on(listener.getEventClass()).subscribe((event -> {
                listener.preOn(event);
            }));
        }
        gateway.getEventDispatcher().on(ReadyEvent.class).subscribe(event -> {
            Logger.getGlobal().log(Level.INFO,String.format(
                    "Logged in as %s#%s", event.getSelf().getUsername(), event.getSelf().getDiscriminator()
            ));
        });
        gateway.getEventDispatcher().on(MessageCreateEvent.class).subscribe(event -> {
            if(! event.getMessage().getContent().trim().startsWith("!"))
                return;

            Message msg = event.getMessage();
            String[] args = msg.getContent().substring(1).split(" ");
            String label = args[0];

            CommandExecutor command = CommandManager.getManager().getCommand(label);
            if(command != null){

                command.execute(new SimpleCommandContext(client,gateway,msg), Arrays.copyOfRange(args,1,args.length > 1 ? args.length - 1 : 1 ));
            }
        });
        gateway.onDisconnect().block();
    }

    @Builder
    private static class SimpleCommandContext extends CommandContext {
        private DiscordClient client;
        private GatewayDiscordClient gateway;
        private Message message;

        @Override
        public DiscordClient getClient() {
            return client;
        }

        @Override
        public GatewayDiscordClient getGateway() {
            return gateway;
        }
        @Override
        public Message getMessage() {
            return message;
        }

    }

}
