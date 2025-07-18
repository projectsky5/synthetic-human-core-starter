package com.projectsky.synthetichumancorestarter.command.service;

import com.projectsky.synthetichumancorestarter.command.enums.Priority;
import com.projectsky.synthetichumancorestarter.command.model.Command;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

@RequiredArgsConstructor
public class CommandService {

    private final ExecutorService executorService;
    private final CommandExecutor executor;

    public void process(Command command) {
        Priority priority = command.getPriority();
        switch (priority) {
            case CRITICAL -> executor.execute(command);
            case COMMON -> {
                try{
                    executorService.submit(() -> executor.execute(command));
                } catch (RejectedExecutionException e) {
                    throw new IllegalStateException("Очередь задач переполнена", e);
                }

            }
        }
    }
}
