package com.projectsky.synthetichumancorestarter.command.service;

import com.projectsky.synthetichumancorestarter.command.enums.Priority;
import com.projectsky.synthetichumancorestarter.command.model.Command;
import com.projectsky.synthetichumancorestarter.exception.exceptions.QueueOverflowException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

@RequiredArgsConstructor
@Slf4j
public class CommandService {

    private final ExecutorService executorService;
    private final CommandExecutor executor;

    public void process(Command command) {
        Priority priority = command.getPriority();
        switch (priority) {
            case CRITICAL -> executor.execute(command);
            case COMMON -> {
                try{
                    log.info("Команда отправлена в очередь");
                    executorService.submit(() -> executor.execute(command));
                } catch (RejectedExecutionException e) {
                    throw new QueueOverflowException();
                }

            }
        }
    }
}
