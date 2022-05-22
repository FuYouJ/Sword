package com.fuyouj.sword.scabard.funtion;

import java.util.function.Consumer;

public class ConsumerChain implements InvokeChain {
    private Closure closure;

    public ConsumerChain(final Closure closure) {
        this.closure = closure;
    }

    public void call() {
        try {
            this.closure.call();
        } catch (Throwable t) {
            //ignored
        }
    }

    public void call(final Consumer<Throwable> fallback) {
        try {
            this.closure.call();
        } catch (Throwable t) {
            try {
                if (fallback != null) {
                    fallback.accept(t);
                }
            } catch (Throwable _t) {
                //ignored
            }
        }
    }
}
