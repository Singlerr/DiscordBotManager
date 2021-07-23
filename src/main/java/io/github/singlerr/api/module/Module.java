package io.github.singlerr.api.module;

public abstract class Module {

    private String name;

    protected Module(String name){
        this.name = name;
    }
    public Module(){}
    public abstract void onEnable();
    public abstract void onDisable();
    public String getName(){
        return name;
    }
    final void init(String name){
        this.name = name;
    }
}
