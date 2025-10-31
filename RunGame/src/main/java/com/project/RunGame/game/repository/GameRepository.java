package com.project.RunGame.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.RunGame.game.model.GameModel;

public interface GameRepository extends JpaRepository<GameModel,Integer>{

}
