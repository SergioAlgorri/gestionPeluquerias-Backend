package gestionPeluqueria.services.impl;

import gestionPeluqueria.entities.Reward;
import gestionPeluqueria.repositories.RewardRepository;
import gestionPeluqueria.services.IRewardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardServiceImpl implements IRewardService {

    private final RewardRepository rewardRepository;

    public RewardServiceImpl(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    @Override
    public List<Reward> findAll() {
        return rewardRepository.findAll();
    }

    @Override
    public Reward findById(long idReward) {
        Reward reward = rewardRepository.findById(idReward);

        if (reward == null) {
            return null;
        }

        return reward;
    }

    @Override
    public Reward createReward(Reward reward) {
        for (Reward r: this.findAll()) {
            if (r.equals(reward)) {
                return null;
            }
        }

        return rewardRepository.save(reward);
    }

    @Override
    public Reward updateReward(long idReward, Reward reward) {
        Reward rewardFound = this.findById(idReward);

        if (rewardFound ==  null) {
            return null;
        }

        // Update Reward
        if (reward.getName() != null) {
            rewardFound.setName(reward.getName());
        }

        if (reward.getPoints() != null) {
            rewardFound.setPoints(reward.getPoints());
        }

        if (reward.getDiscountAmount() != null) {
            rewardFound.setDiscountAmount(reward.getDiscountAmount());
        }

        if (reward.getExpirationDate() != null) {
            rewardFound.setExpirationDate(reward.getExpirationDate());
        }

        return rewardRepository.save(rewardFound);
    }

    @Override
    public void deleteReward(long idReward) {
        Reward reward = this.findById(idReward);

        if (reward == null) {
            return;
        }

        rewardRepository.delete(reward);
    }
}
