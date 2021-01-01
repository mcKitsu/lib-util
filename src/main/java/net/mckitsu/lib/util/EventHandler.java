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

    public <R> boolean execute(Supplier<R> supplier){
        return this.execute(supplier, null);
    }

    public <R> boolean execute(Supplier<R> supplier, Consumer<R> finish){
        if(supplier == null)
            return false;

        Runnable exec = () -> {
            R result = supplier.get();
            if(finish != null)
                finish.accept(result);
        };

        this.doExecute(exec);

        return true;
    }

    public <A, R> boolean executeFunction(Function<A, R> function, A attachment){
        return this.execute(function, attachment, null);
    }

    public <A, R> boolean executeFunction(Function<A, R> function, A attachment, Consumer<R> finish){
        return this.execute(function, attachment, finish);
    }

    public <A, R> boolean execute(Function<A, R> function, A attachment){
        return this.execute(function, attachment, null);
    }

    public <A, R> boolean execute(Function<A, R> function, A attachment, Consumer<R> finish){
        if(function == null)
            return false;

        Runnable exec = () -> {
            R result = function.apply(attachment);
            if(finish!=null)
                finish.accept(result);
        };

        this.doExecute(exec);

        return true;
    }

    public <A> boolean execute(Predicate<? super A> predicate, A attachment){
        return this.execute(predicate, attachment, null);
    }

    public <A> boolean execute(Predicate<? super A> predicate, A attachment, Consumer<Boolean> finish){
        if(predicate == null)
            return false;

        Runnable exec = ()->{
            boolean result = predicate.test(attachment);
            if(finish!=null)
                finish.accept(result);
        };

        this.doExecute(exec);

        return true;
    }

    public <A, B> boolean execute(BiConsumer<? super A, ?super B> biConsumer, A attachment, B attachment2){
        return this.execute(biConsumer, attachment, attachment2, null);
    }

    public <A, B> boolean execute(BiConsumer<? super A, ?super B> biConsumer, A attachment, B attachment2, Runnable finish){
        if(biConsumer == null)
            return false;

        Runnable exec = ()->{
            biConsumer.accept(attachment, attachment2);
            if(finish!=null)
                finish.run();
        };

        this.doExecute(exec);

        return true;
    }

    public <A> boolean execute(Consumer<? super A> consumer, A attachment){
        return this.execute(consumer, attachment, null);
    }

    public <A> boolean execute(Consumer<? super A> consumer, A attachment, Runnable finish){
        if(consumer == null)
            return false;

        Runnable exec = ()->{
            consumer.accept(attachment);
            if(finish!=null)
                finish.run();
        };

        this.doExecute(exec);

        return true;
    }

    public <A> void executeWait(Consumer<? super A> consumer, A attachment, Runnable finish){
        if(consumer == null)
            return;

        if(finish == null)
            return;

        Runnable exec = ()->{
            consumer.accept(attachment);
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

    public <R> R executeWait(Supplier<R> supplier){
        if(supplier == null)
            return null;

        Object finish = new Object();
        final Object[] result = new Object[1];

        Runnable exec = () -> {
            result[0] = supplier.get();
            synchronized (finish){
                finish.notify();
            }
        };

        this.doExecuteWait(exec, finish);

        return (R) result[0];
    }

    public <A, R> R executeWait(Function<A, R> function, A attachment){
        return executeWaitFunction(function, attachment);
    }

    public <A, R> R executeWaitFunction(Function<A, R> function, A attachment){
        if(function == null)
            return null;

        Object finish = new Object();
        final Object[] result = new Object[1];

        Runnable exec = () -> {
            result[0] = function.apply(attachment);
            synchronized (finish){
                finish.notify();
            }
        };

        this.doExecuteWait(exec, finish);

        return (R)result[0];
    }

    public <A> boolean executeWait(Predicate<? super A> predicate, A attachment){
        if(predicate == null)
            return false;

        final boolean[] result = new boolean[1];

        Object finish = new Object();

        Runnable exec = ()->{
            result[0] = predicate.test(attachment);
            synchronized (finish){
                finish.notify();
            }
        };

        this.doExecuteWait(exec, finish);

        return result[0];
    }

    public <A, B> void executeWait(BiConsumer<? super A, ? super B> biConsumer, A attachment, B attachment2){
        if(biConsumer == null)
            return;

        Object finish = new Object();

        Runnable exec = ()->{
            biConsumer.accept(attachment, attachment2);
            synchronized (finish){
                finish.notify();
            }
        };

        this.doExecuteWait(exec, finish);
    }

    public <A> void executeWait(Consumer<? super A> consumer, A attachment){
        if(consumer == null)
            return;

        Object finish = new Object();

        Runnable exec = ()->{
            consumer.accept(attachment);
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
