package com.projectsky.synthetichumancorestarter.command.service;

import com.projectsky.synthetichumancorestarter.command.model.Command;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandExecutor {

    public void execute(Command command) {
        log.info("Информация о команде: Описание={}, Приоритет={}, Автор={}, Время назначения={}",
                command.getDescription(),
                command.getPriority(),
                command.getAuthor(),
                command.getTime());
    }
}
