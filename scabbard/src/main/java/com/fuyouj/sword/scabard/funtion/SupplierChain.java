package com.fuyouj.sword.scabard.funtion;

import java.util.function.Function;
import java.util.function.Supplier;

public class SupplierChain<R> implements InvokeChain {
    private Supplier<R> chain;

    public SupplierChain(final Supplier<R> chain) {
        this.chain = chain;
    }

    public R call() {
        try {
            return this.chain.get();
        } catch (Throwable e) {
            return null;
        }
    }

    public R fallback(final Function<Throwable, R> fallback) {
        try {
            return chain.get();
        } catch (Throwable e) {
            try {
                return fallback.apply(e);
            } catch (Throwable ex) {
                return null;
            }
        }
    }
}

