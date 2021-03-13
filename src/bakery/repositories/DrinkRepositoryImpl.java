package bakery.repositories;

import bakery.entities.drinks.interfaces.Drink;
import bakery.repositories.interfaces.DrinkRepository;
import bakery.repositories.interfaces.Repository;

import java.util.ArrayList;
import java.util.Collection;

public class DrinkRepositoryImpl implements DrinkRepository<Drink> {
    private Collection<Drink> models;

    public DrinkRepositoryImpl() {
        this.models = new ArrayList<>();
    }

    @Override
    public void add(Drink drink) {
        this.models.add(drink);
    }

    @Override
    public Collection<Drink> getAll() {
        return this.models;
    }

    @Override
    public Drink getByNameAndBrand(String drinkName, String drinkBrand) {
        return this.models.stream()
                .filter(drink -> drink.getName().equals(drinkName) && drink.getBrand().equals(drinkBrand))
                .findFirst().orElse(null);
    }
}
