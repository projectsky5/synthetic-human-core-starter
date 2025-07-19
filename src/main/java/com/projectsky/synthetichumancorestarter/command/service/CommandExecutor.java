package com.projectsky.synthetichumancorestarter.command.service;

import com.projectsky.synthetichumancorestarter.command.model.Command;
import com.projectsky.synthetichumancorestarter.metrics.CommandMetricsPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CommandExecutor {

    private final CommandMetricsPublisher metricsPublisher;

    public void execute(Command command) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String author = command.getAuthor();

        log.info("Информация о команде: Описание={}, Приоритет={}, Автор={}, Время назначения={}",
                command.getDescription(),
                command.getPriority(),
                author,
                command.getTime());

        metricsPublisher.incrementAuthorCount(author);
    }
}
