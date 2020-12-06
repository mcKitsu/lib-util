package net.mckitsu.lib.util;

import java.util.concurrent.Executor;
import java.util.function.*;

public class EventHandler {
    private final Executor executor;

    /* **************************************************************************************
     *  Abstract method
     */

    /* **************************************************************************************
     *  Construct method
     */
    protected EventHandler(Executor executor){
        this.executor = executor;
    }

    protected EventHandler(){
        this.executor = null;
    }

    /* **************************************************************************************
     *  Override method
     */

    /* **************************************************************************************
     *  Public method
     */

    /* **************************************************************************************
     *  protected method
     */
    protected boolean execute(Supplier supplier){
        return this.execute(supplier, null);
    }

    protected boolean execute(Supplier supplier, Consumer finish){
        if(supplier == null)
            return false;

        Runnable exec = () -> {
            Object result = supplier.get();
            if(finish != null)
                finish.accept(result);
        };

        this.doExecute(exec);

        return true;
    }

    protected boolean execute(Function function, Object object){
        return this.execute(function, object, null);
    }

    protected boolean execute(Function function, Object object, Consumer finish){
        if(function == null)
            return false;

        Runnable exec = () -> {
            Object result = function.apply(object);
            if(finish!=null)
                finish.accept(result);
        };

        this.doExecute(exec);

        return true;
    }

    protected boolean execute(Predicate predicate, Object object){
        return this.execute(predicate, object, null);
    }

    protected boolean execute(Predicate predicate, Object object, Consumer<Boolean> finish){
        if(predicate == null)
            return false;

        Runnable exec = ()->{
            boolean result = predicate.test(object);
            if(finish!=null)
                finish.accept(result);
        };

        this.doExecute(exec);

        return true;
    }

    protected boolean execute(BiConsumer biConsumer, Object object, Object object2){
        return this.execute(biConsumer, object, object2, null);
    }

    protected boolean execute(BiConsumer biConsumer, Object object, Object object2, Runnable finish){
        if(biConsumer == null)
            return false;

        Runnable exec = ()->{
            biConsumer.accept(object, object2);
            if(finish!=null)
                finish.run();
        };

        this.doExecute(exec);

        return true;
    }

    protected boolean execute(Consumer consumer, Object object){
        return this.execute(consumer, object, null);
    }

    protected boolean execute(Consumer consumer, Object object, Runnable finish){
        if(consumer == null)
            return false;

        Runnable exec = ()->{
            consumer.accept(object);
            if(finish!=null)
                finish.run();
        };

        this.doExecute(exec);

        return true;
    }

    protected boolean execute(Runnable runnable){
        return execute(runnable, null);
    }

    protected boolean execute(Runnable runnable, Runnable finish){
        if(runnable == null)
            return false;

        Runnable exec = ()->{
            runnable.run();
            if (finish != null)
                finish.run();
        };

        this.doExecute(exec);

        return true;
    }
    /* **************************************************************************************
     *  Private method
     */
    private void doExecute(Runnable exec){
        if(this.executor != null)
            this.executor.execute(exec);

        else
            exec.run();
    }
}
