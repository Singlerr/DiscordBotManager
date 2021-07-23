package io.github.singlerr;

import io.github.singlerr.api.module.ModuleManager;
import io.github.singlerr.handler.DiscordManager;

import java.io.File;

public class Main {

    public static void main(String[] args){
        if(args.length > 0){
            String token = args[0];
            System.out.println(token);
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
