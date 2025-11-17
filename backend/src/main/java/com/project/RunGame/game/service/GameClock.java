package com.project.RunGame.game.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class GameClock {
    private final int CLOCK_TICK = 200; //in Milliseconds
    private final GameTasks gameTasks;
    private final AtomicInteger tick = new AtomicInteger(0);
    String roomId;
    private ScheduledExecutorService scheduler;

    GameClock(String roomId) {
        this.gameTasks = new GameTasks();
        this.roomId = roomId;
    }

    public void startClock() {
        this.scheduler = Executors.newScheduledThreadPool(2);
        Runnable task = () -> {
            try {
                this.updateGameState();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        int delay = 0;
        scheduler.scheduleAtFixedRate(task, delay, this.CLOCK_TICK, TimeUnit.MILLISECONDS);
    }

    public void stopClock() {
        this.scheduler.shutdown();
    }

    private void updateGameState() {
        tick.incrementAndGet();
        if (tick.get() % 10 == 0) {
            this.gameTasks.generateMonsters(this.roomId);
        }
        this.gameTasks.moveMonsters(this.roomId);
        this.gameTasks.broadcastHeroesCoordinates(this.roomId);
    }
}
