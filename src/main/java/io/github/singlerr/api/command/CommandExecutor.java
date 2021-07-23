package io.github.singlerr.api.command;

import io.github.singlerr.api.Context;
import reactor.util.annotation.NonNull;

public interface CommandExecutor {
    @NonNull
    String getLabel();
    void execute(Context ctx, String[] args);
}
