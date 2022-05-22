package com.fuyouj.sword.scabard.funtion;

import java.util.function.Supplier;

public class Invoke {
    public static <T> ConsumerChain invoke(final Closure closure) {
        return new ConsumerChain(closure);
    }

    public static <R> SupplierChain<R> start(final Supplier<R> chain) {
        return new SupplierChain<>(chain);
    }
}
