package net.mckitsu.lib.util;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.concurrent.Executor;
import java.util.function.*;

public class EventHandler {
    private final Executor executor;

    protected EventHandler(Executor executor){
        this.executor = executor;
    }

    protected EventHandler(){
        this.executor = null;
    }

    protected boolean execute(Supplier supplier){
        return this.execute(supplier, null);
    }

    protected boolean execute(Supplier supplier, Consumer finish){
        if(supplier == null)
            return false;

        if(this.executor != null){
            this.executor.execute(() -> {
                Object result = supplier.get();
                if(finish!=null)
                    finish.accept(result);
            });
        } else{
            Object result = supplier.get();
            if(finish != null)
                finish.accept(result);
        }
        return true;
    }

    protected boolean execute(Function function, Object object){
        return this.execute(function, object, null);
    }

    protected boolean execute(Function function, Object object, Consumer finish){
        if(function == null)
            return false;

        if(this.executor != null){
            this.executor.execute(() -> {
                Object result = function.apply(object);
                if(finish!=null)
                    finish.accept(result);
            });
        } else{
            Object result = function.apply(object);
            if(finish != null)
                finish.accept(result);
        }
        return true;
    }

    protected boolean execute(Predicate predicate, Object object){
        return this.execute(predicate, object, null);
    }

    protected boolean execute(Predicate predicate, Object object, Consumer<Boolean> finish){
        if(predicate == null)
            return false;

        if(this.executor != null){
            this.executor.execute(() -> {
                boolean result = predicate.test(object);
                if(finish!=null)
                    finish.accept(result);
            });
        } else{
            boolean result = predicate.test(object);
            if(finish != null)
                finish.accept(result);
        }
        return true;
    }

    protected boolean execute(BiConsumer biConsumer, Object object, Object object2){
        return this.execute(biConsumer, object, object2, null);
    }

    protected boolean execute(BiConsumer biConsumer, Object object, Object object2, Runnable finish){
        if(biConsumer == null)
            return false;

        if(this.executor != null){
            this.executor.execute(() -> {
                biConsumer.accept(object, object2);
                if(finish!=null)
                    finish.run();
            });
        } else{
            biConsumer.accept(object, object2);
            if(finish != null)
                finish.run();
        }
        return true;
    }

    protected boolean execute(Consumer consumer, Object object){
        return this.execute(consumer, object, null);
    }

    protected boolean execute(Consumer consumer, Object object, Runnable finish){
        if(consumer == null)
            return false;

        if(this.executor != null){
            this.executor.execute(() -> {
                consumer.accept(object);
                if(finish!=null)
                    finish.run();
            });
        } else{
            consumer.accept(object);
            if(finish != null)
                finish.run();
        }
        return true;
    }

    protected boolean execute(Runnable runnable){
        return execute(runnable, null);
    }

    protected boolean execute(Runnable runnable, Runnable finish){
        if(runnable != null){
            if(this.executor != null) {
                this.executor.execute(() -> {
                    runnable.run();
                    if (finish != null)
                        finish.run();
                });


            }else {
                runnable.run();
                if(finish != null)
                    finish.run();
            }

            return true;
        }else{
            return false;
        }
    }
}
