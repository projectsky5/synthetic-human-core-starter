package com.projectsky.synthetichumancorestarter.metrics;

import com.projectsky.synthetichumancorestarter.command.model.Command;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class CommandMetricsPublisher {

    private final Map<String, Counter> authorCounts = new ConcurrentHashMap<>();
    private final MeterRegistry registry;

    public CommandMetricsPublisher(BlockingQueue<Command> queue, MeterRegistry registry) {
        this.registry = registry;

        registry.gauge("synthetic.command.queue.size", queue, BlockingQueue::size);
    }

    public void incrementAuthorCount(String author){
        Counter counter = authorCounts.computeIfAbsent(author, au ->
                Counter.builder("synthetic.command.completed.total")
                    .tags("author", au)
                    .register(registry));

        counter.increment();
    }
}
