package net.mckitsu.lib.util;

import java.util.concurrent.Executor;

public class EventHandlers {
    /* **************************************************************************************
     *  Static method
     */
    public static EventHandler newExecuteEventHandler(Executor executors){
        return new EventHandler() {
            protected final Executor executor = executors;
            @Override
            protected Executor getExecutor() {
                return this.executor;
            }
        };
    }

    public static EventHandler newEventHandler(){
        return new EventHandler() {
            @Override
            protected Executor getExecutor() {
                return null;
            }
        };
    }
}
