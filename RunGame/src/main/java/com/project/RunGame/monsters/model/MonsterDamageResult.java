package com.project.RunGame.monsters.model;

import java.util.Map;

public class MonsterDamageResult {
    private final Map<String, MonsterInMap> injuredMonsters;
    private final Map<String, MonsterInMap> killedMonsters;

    public MonsterDamageResult(Map<String, MonsterInMap> injuredMonsters, Map<String, MonsterInMap> killedMonsters) {
        this.injuredMonsters = injuredMonsters;
        this.killedMonsters = killedMonsters;
    }

    public Map<String, MonsterInMap> getInjuredMonsters() {
        return injuredMonsters;
    }

    public Map<String, MonsterInMap> getKilledMonsters() {
        return killedMonsters;
    }
}
