package io.github.singlerr.api.command;

import io.github.singlerr.utils.ReflectionUtils;

import java.util.Collection;
import java.util.HashMap;

public final class CommandManager {
    private HashMap<String,CommandExecutor> commands;

    private static CommandManager instance;
    private CommandManager(){
        this.commands = new HashMap<>();
    }

    public static CommandManager getManager(){
        if(instance == null)
            return (instance = new CommandManager());
        return instance;
    }

    public boolean registerCommand(Class<? extends CommandExecutor> cls){
        try{
            CommandExecutor inst = ReflectionUtils.instantiateObject(cls);
            commands.put(inst.getLabel(),inst);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
    public boolean unregisterCommand(String label){
        if(commands.containsKey(label)){
            commands.remove(label);
            return true;
        }
        return false;
    }

    public Collection<CommandExecutor> getRegisteredCommands(){
        return commands.values();
    }

    public CommandExecutor getCommand(String label){
        if(commands.containsKey(label))
            return commands.get(label);
        return null;
    }
}
