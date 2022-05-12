package com.fuyouj.sword.concurrent.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import com.fuyouj.sword.concurrent.AtomicCommand;
import com.fuyouj.sword.concurrent.CommandFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class AtomicRunnerFactoryTest {
    @Test
    public void should_create_main_thread_runner() throws InterruptedException {
        final int total = 1000;
        AtomicRunner<String> atomicRunner = AtomicRunnerFactory.noneThreadPoolSingleCommandRunner();
        List<String> strings = new ArrayList<>();

        AtomicCommand<String, Object> command1 = CommandFactory.stateCommand("key1", () -> {
            strings.add("cmd1");
            strings.add("cmd2");
            return null;
        });

        AtomicCommand<String, Object> command2 = CommandFactory.stateCommand("key1", () -> {
            strings.add("cmd3");
            strings.add("cmd4");
            return null;
        });

        ArrayList<Callable<Object>> runnableList = new ArrayList<>();
        for (int index = 0; index < total; index++) {
            runnableList.add(() -> {
                atomicRunner.run(command1);
                return null;
            });

            runnableList.add(() -> {
                atomicRunner.run(command2);
                return null;
            });
        }

        Executors.newWorkStealingPool().invokeAll(runnableList);

        assertThat(strings.size(), Is.is(total * 4));

        for (int index = 0; index < 4000; index += 2) {
            String key = strings.get(index);

            if (Objects.equals(key, "cmd1")) {
                assertThat(strings.get(index + 1), Is.is("cmd2"));
                continue;
            }

            if (Objects.equals(key, "cmd3")) {
                assertThat(strings.get(index + 1), Is.is("cmd4"));
                continue;
            }

            fail("");
        }
    }

    @Test
    public void should_create_main_thread_runner_and_run_with_different_key() throws InterruptedException {
        final int total = 1000;
        AtomicRunner<String> atomicRunner = AtomicRunnerFactory.noneThreadPoolSingleCommandRunner();
        Map<String, List<String>> strings = new ConcurrentHashMap<>();
        strings.put("key1", new ArrayList<>());
        strings.put("key3", new ArrayList<>());

        AtomicCommand<String, Object> command1 = CommandFactory.stateCommand("key1", () -> {
            strings.get("key1").add("cmd1");
            strings.get("key1").add("cmd2");
            return null;
        });

        AtomicCommand<String, Object> command2 = CommandFactory.stateCommand("key1", () -> {
            strings.get("key1").add("cmd1.1");
            strings.get("key1").add("cmd2.2");
            return null;
        });

        AtomicCommand<String, Object> command3 = CommandFactory.stateCommand("key3", () -> {
            strings.get("key3").add("cmd3");
            strings.get("key3").add("cmd4");
            return null;
        });

        AtomicCommand<String, Object> command4 = CommandFactory.stateCommand("key3", () -> {
            strings.get("key3").add("cmd3.3");
            strings.get("key3").add("cmd4.4");
            return null;
        });

        ArrayList<Callable<Object>> runnableList = new ArrayList<>();
        for (int index = 0; index < total; index++) {
            runnableList.add(() -> {
                atomicRunner.run(command1);
                return null;
            });

            runnableList.add(() -> {
                atomicRunner.run(command2);
                return null;
            });

            runnableList.add(() -> {
                atomicRunner.run(command3);
                return null;
            });

            runnableList.add(() -> {
                atomicRunner.run(command4);
                return null;
            });
        }

        Executors.newWorkStealingPool().invokeAll(runnableList);

        List<String> key1Strings = strings.get("key1");
        List<String> key3Strings = strings.get("key3");

        for (int index = 0; index < 4000; index += 2) {
            String key = key1Strings.get(index);

            if (Objects.equals(key, "cmd1")) {
                assertThat(key1Strings.get(index + 1), Is.is("cmd2"));
                continue;
            }

            if (Objects.equals(key, "cmd1.1")) {
                assertThat(key1Strings.get(index + 1), Is.is("cmd2.2"));
                continue;
            }

            fail("");
        }

        for (int index = 0; index < 4000; index += 2) {
            String key = key3Strings.get(index);

            if (Objects.equals(key, "cmd3")) {
                assertThat(key3Strings.get(index + 1), Is.is("cmd4"));
                continue;
            }

            if (Objects.equals(key, "cmd3.3")) {
                assertThat(key3Strings.get(index + 1), Is.is("cmd4.4"));
                continue;
            }

            fail("");
        }
    }
}