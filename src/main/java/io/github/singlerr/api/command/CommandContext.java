package io.github.singlerr.api.command;

import discord4j.core.object.entity.Message;
import io.github.singlerr.api.Context;

public abstract class CommandContext implements Context {
    public abstract Message getMessage();
}
