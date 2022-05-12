package com.fuyouj.sword.concurrent.runner;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;

import com.fuyouj.sword.concurrent.CommandFactory;
import com.fuyouj.sword.concurrent.SingleCommandAtomicQueueMapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class AtomicRunnerTest {
    @Test
    public void should_get_or_throw() {
        CurrentThreadAtomicRunner<String> runner =
                new CurrentThreadAtomicRunner<>(new SingleCommandAtomicQueueMapper<String>());

        AtomicResult<Object> result = runner.run("exception", () -> {
            throw new RuntimeException("hello");
        });

        try {
            result.getOrThrow();
        } catch (RuntimeException e) {
            return;
        }
        fail("should throw RuntimeException");
    }

    @Test
    public void should_not_keep_atomic_with_different_key() throws InterruptedException {
        CurrentThreadAtomicRunner<String> runner =
                new CurrentThreadAtomicRunner<>(new SingleCommandAtomicQueueMapper<String>());
        Counter counter = new Counter();

        ArrayList<Callable<Object>> runnableList = new ArrayList<>();
        for (int index = 0; index < 1000; index++) {
            int i = index;
            runnableList.add(() -> {
                runner.run(CommandFactory.stateCommand("key" + i, () -> {
                    counter.incr();
                    return null;
                }));
                return null;
            })
            ;
        }

        Executors.newWorkStealingPool().invokeAll(runnableList);

        assertThat(counter.counter, IsNot.not(1000));
    }

    @Test
    public void should_run_command_with_same_key() throws InterruptedException {
        CurrentThreadAtomicRunner<String> runner =
                new CurrentThreadAtomicRunner<String>(new SingleCommandAtomicQueueMapper<String>());
        Counter counter = new Counter();

        ArrayList<Callable<Object>> runnableList = new ArrayList<>();
        for (int index = 0; index < 1000; index++) {
            runnableList.add(() -> {
                runner.run(CommandFactory.stateCommand("key1", () -> {
                    counter.incr();
                    return null;
                }));
                return null;
            })
            ;
        }

        Executors.newWorkStealingPool().invokeAll(runnableList);

        assertThat(counter.counter, Is.is(1000));
    }

    @Test
    public void should_run_simple_command() {
        CurrentThreadAtomicRunner<String> runner =
                new CurrentThreadAtomicRunner<String>(new SingleCommandAtomicQueueMapper<String>());

        AtomicResult<Integer> atomicResult = runner.run(CommandFactory.stateCommand("key1", () -> 1));
        assertThat(atomicResult.getResult(), Is.is(1));

        atomicResult = runner.run(CommandFactory.stateCommand("key1", () -> 2));
        assertThat(atomicResult.getResult(), Is.is(2));
    }

    static class Counter {
        private int counter;

        void incr() {
            this.counter += 1;
        }
    }
}