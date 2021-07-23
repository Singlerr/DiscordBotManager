package io.github.singlerr.api.event;

import discord4j.core.event.domain.Event;

public abstract class EventListener<T extends Event> {

    public void preOn(Event e){
        on((T)e);
    }

    public abstract void on(T event);
    public abstract Class<T> getEventClass();
}
