package gestionPeluqueria.services;

import gestionPeluqueria.entities.Reward;

import java.util.List;

public interface IRewardService {

    public List<Reward> findAll();
    public Reward findById(long idReward);
    public Reward createReward(Reward reward);
    public Reward updateReward(long idReward, Reward reward);
    public void deleteReward(long idReward);
}
