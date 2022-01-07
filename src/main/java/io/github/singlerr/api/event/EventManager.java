package io.github.singlerr.api.event;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class EventManager {

    private static EventManager instance;
    private List<EventListener<? extends Event>> registeredListeners;

    private EventManager(){
        this.registeredListeners = new ArrayList<>();
    }

    public static EventManager getManager(){
        if(instance == null)
            return (instance = new EventManager());
        return instance;
    }

    public void registerEventListener(EventListener<? extends Event> listener){
        /*
        if(listener.getEventClass().equals(MessageCreateEvent.class)){
            throw new IllegalArgumentException("Cannot register MessageCreateEvent listener class. Please register command executor instead.");
        }
         */
        registeredListeners.add(listener);
    }

    public List<EventListener<? extends Event>> getRegisteredListeners() {
        return registeredListeners;
    }
    public List<EventListener<? extends Event>> findListener(Class<? extends Event> cls){
        return registeredListeners.stream().filter(l -> l.getEventClass().equals(cls)).collect(Collectors.toList());
    }
}
