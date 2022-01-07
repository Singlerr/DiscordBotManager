package io.github.singlerr;

import io.github.singlerr.api.module.ModuleManager;
import io.github.singlerr.handler.DiscordManager;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscordBot {

    public static void main(String[] args){
        if(args.length > 0){
            String token = args[0];
            Logger.getGlobal().log(Level.INFO,String.format("Login bot as %s",token));
            File modulePath = new File(Env.MODULE_PATH);
            if(! modulePath.exists())
                modulePath.mkdir();
            ModuleManager.getInstance().loadModules(Env.MODULE_PATH);
            DiscordManager.createInstance(token).listen();
        }else{
            throw new IllegalArgumentException("A jar command must have token argument.");
        }
    }
}
