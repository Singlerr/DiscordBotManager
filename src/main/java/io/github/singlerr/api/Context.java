package io.github.singlerr.api;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;

public interface Context {
    DiscordClient getClient();
    GatewayDiscordClient getGateway();


}
