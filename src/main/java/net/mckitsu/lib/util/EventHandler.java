package net.mckitsu.lib.util;

import java.util.concurrent.Executor;
import java.util.function.*;

public abstract class EventHandler {
    /* **************************************************************************************
     *  Abstract method
     */
    protected abstract Executor getExecutor();
    /* **************************************************************************************
     *  Construct method
     */
    protected EventHandler(){}

    /* **************************************************************************************
     *  Override method
     */

    /* **************************************************************************************
     *  Public method
     */

    public boolean execute(Supplier supplier){
        return this.execute(supplier, null);
    }

    public boolean execute(Supplier supplier, Consumer finish){
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

    public boolean execute(Function function, Object object){
        return this.execute(function, object, null);
    }

    public boolean execute(Function function, Object object, Consumer finish){
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

    public boolean execute(Predicate predicate, Object object){
        return this.execute(predicate, object, null);
    }

    public boolean execute(Predicate predicate, Object object, Consumer<Boolean> finish){
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

    public boolean execute(BiConsumer biConsumer, Object object, Object object2){
        return this.execute(biConsumer, object, object2, null);
    }

    public boolean execute(BiConsumer biConsumer, Object object, Object object2, Runnable finish){
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

    public boolean execute(Consumer consumer, Object object){
        return this.execute(consumer, object, null);
    }

    public boolean execute(Consumer consumer, Object object, Runnable finish){
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

    public void executeWait(Consumer consumer, Object object, Runnable finish){
        if(consumer == null)
            return;

        if(finish == null)
            return;

        Runnable exec = ()->{
            consumer.accept(object);
            finish.run();
            synchronized (finish){
                finish.notify();
            }
        };

        this.doExecuteWait(exec, finish);
    }

    public boolean execute(Runnable runnable){
        return execute(runnable, null);
    }

    public boolean execute(Runnable runnable, Runnable finish){
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

    public Object executeWait(Supplier supplier){
        if(supplier == null)
            return false;

        Object finish = new Object();
        final Object[] result = new Object[1];

        Runnable exec = () -> {
            result[0] = supplier.get();
            synchronized (finish){
                finish.notify();
            }
        };

        this.doExecuteWait(exec, finish);

        return result[0];
    }

    public Object executeWait(Function function, Object object){
        if(function == null)
            return false;

        Object finish = new Object();
        final Object[] result = new Object[1];

        Runnable exec = () -> {
            result[0] = function.apply(object);
            synchronized (finish){
                finish.notify();
            }
        };

        this.doExecuteWait(exec, finish);

        return result[0];
    }

    public boolean executeWait(Predicate predicate, Object object){
        if(predicate == null)
            return false;

        final boolean[] result = new boolean[1];

        Object finish = new Object();

        Runnable exec = ()->{
            result[0] = predicate.test(object);
            synchronized (finish){
                finish.notify();
            }
        };

        this.doExecuteWait(exec, finish);

        return result[0];
    }

    public void executeWait(BiConsumer biConsumer, Object object, Object object2){
        if(biConsumer == null)
            return;

        Object finish = new Object();

        Runnable exec = ()->{
            biConsumer.accept(object, object2);
            synchronized (finish){
                finish.notify();
            }
        };

        this.doExecuteWait(exec, finish);
    }

    public void executeWait(Consumer consumer, Object object){
        if(consumer == null)
            return;

        Object finish = new Object();

        Runnable exec = ()->{
            consumer.accept(object);
            synchronized (finish){
                finish.notify();
            }
        };

        this.doExecuteWait(exec, finish);
    }

    public void executeWait(Runnable runnable){
        if(runnable == null)
            return;

        Object finish = new Object();

        Runnable exec = ()->{
            runnable.run();
            synchronized (finish){
                finish.notify();
            }
        };

        this.doExecuteWait(exec, finish);
    }

    /* **************************************************************************************
     *  protected method
     */

    /* **************************************************************************************
     *  Private method
     */
    private void doExecute(Runnable exec){
        Executor executor = getExecutor();

        if(executor != null)
            executor.execute(exec);

        else
            exec.run();
    }

    private void doExecuteWait(Runnable exec, Object finish){
        Executor executor = getExecutor();

        if(executor != null) {
            executor.execute(exec);
            synchronized (finish){
                try {
                    finish.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else
            exec.run();
    }
}
