package gestionPeluqueria.services;

import gestionPeluqueria.entities.Reward;

import java.util.List;

public interface IRewardService {

    List<Reward> findAll();
    Reward findById(long idReward);
    Reward createReward(Reward reward);
    Reward updateReward(long idReward, Reward reward);
    void deleteReward(long idReward);
}