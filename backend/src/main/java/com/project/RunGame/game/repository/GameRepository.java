package com.project.RunGame.game.repository;

import com.project.RunGame.game.model.GameModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameModel, Integer> {

}
