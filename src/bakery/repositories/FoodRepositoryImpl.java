package bakery.repositories;

import bakery.entities.bakedFoods.interfaces.BakedFood;
import bakery.repositories.interfaces.FoodRepository;
import bakery.repositories.interfaces.Repository;

import java.util.ArrayList;
import java.util.Collection;

public class FoodRepositoryImpl implements FoodRepository<BakedFood> {
    private Collection<BakedFood> models;

    public FoodRepositoryImpl() {
        this.models = new ArrayList<>();
    }

    @Override
    public void add(BakedFood bakedFood) {
        this.models.add(bakedFood);
    }

    @Override
    public Collection<BakedFood> getAll() {
        return this.models;
    }

    @Override
    public BakedFood getByName(String name) {
        return this.models.stream()
                .filter(bakedFood -> bakedFood.getName().equals(name))
                .findFirst().orElse(null);
    }
}
