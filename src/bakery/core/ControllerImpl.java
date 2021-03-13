package bakery.core;

import bakery.core.interfaces.Controller;
import bakery.entities.bakedFoods.Bread;
import bakery.entities.bakedFoods.Cake;
import bakery.entities.bakedFoods.interfaces.BakedFood;
import bakery.entities.drinks.Tea;
import bakery.entities.drinks.Water;
import bakery.entities.drinks.interfaces.Drink;
import bakery.entities.tables.InsideTable;
import bakery.entities.tables.OutsideTable;
import bakery.entities.tables.interfaces.Table;
import bakery.repositories.interfaces.DrinkRepository;
import bakery.repositories.interfaces.FoodRepository;
import bakery.repositories.interfaces.TableRepository;

import static bakery.common.ExceptionMessages.*;
import static bakery.common.OutputMessages.*;

public class ControllerImpl implements Controller {
    private FoodRepository<BakedFood> foodRepository;
    private DrinkRepository<Drink> drinkRepository;
    private TableRepository<Table> tableRepository;

    public ControllerImpl(FoodRepository<BakedFood> foodRepository, DrinkRepository<Drink> drinkRepository, TableRepository<Table> tableRepository) {
        this.foodRepository = foodRepository;
        this.drinkRepository = drinkRepository;
        this.tableRepository = tableRepository;
    }

    @Override
    public String addFood(String type, String name, double price) {
        if (this.foodRepository.getByName(name) != null) {
            throw new IllegalArgumentException(String.format(FOOD_OR_DRINK_EXIST, type, name));
        }

        BakedFood food = null;

        if ("Bread".equals(type)) {
            food = new Bread(name, price);
        } else if ("Cake".equals(type)) {
            food = new Cake(name, price);
        }

        this.foodRepository.add(food);

        return String.format(FOOD_ADDED, name, type);
    }

    @Override
    public String addDrink(String type, String name, int portion, String brand) {
        if (this.drinkRepository.getByNameAndBrand(name, brand) != null) {
            throw new IllegalArgumentException(String.format(FOOD_OR_DRINK_EXIST, type, name));
        }

        Drink drink = null;

        if ("Tea".equals(type)) {
            drink = new Tea(name, portion, brand);
        } else if ("Water".equals(type)) {
            drink = new Water(name, portion, brand);
        }

        this.drinkRepository.add(drink);

        return String.format(DRINK_ADDED, name, brand);
    }

    @Override
    public String addTable(String type, int tableNumber, int capacity) {
        if (this.tableRepository.getByNumber(tableNumber) != null) {
            throw new IllegalArgumentException(String.format(TABLE_EXIST, tableNumber));
        }

        Table table = null;

        if ("InsideTable".equals(type)) {
            table = new InsideTable(tableNumber, capacity);
        } else if ("OutsideTable".equals(type)) {
            table = new OutsideTable(tableNumber, capacity);
        }

        this.tableRepository.add(table);

        return String.format(TABLE_ADDED, tableNumber);
    }

    @Override
    public String reserveTable(int numberOfPeople) {
        Table table = this.tableRepository.getAll().stream()
                .filter(currentTable -> !currentTable.isReserved() && currentTable.getCapacity() >= numberOfPeople)
                .findFirst().orElse(null);

        if (table == null) {
            return String.format(RESERVATION_NOT_POSSIBLE, numberOfPeople);
        } else {
            table.reserve(numberOfPeople);
        }

        return String.format(TABLE_RESERVED, table.getTableNumber(), numberOfPeople);
    }

    @Override
    public String orderFood(int tableNumber, String foodName) {
        Table table = this.tableRepository.getAll().stream()
                .filter(currentTable -> currentTable.getTableNumber() == tableNumber)
                .findFirst().orElse(null);

        BakedFood food = this.foodRepository.getAll().stream()
                .filter(bakedFood -> bakedFood.getName().equals(foodName))
                .findFirst().orElse(null);

        if (table == null) {
            return String.format(WRONG_TABLE_NUMBER, tableNumber);
        }

        if (food == null) {
            return String.format(NONE_EXISTENT_FOOD, foodName);
        }

        table.orderFood(food);

        return String.format(FOOD_ORDER_SUCCESSFUL, tableNumber, foodName);
    }

    @Override
    public String orderDrink(int tableNumber, String drinkName, String drinkBrand) {
        Table table = this.tableRepository.getAll().stream()
                .filter(currentTable -> currentTable.getTableNumber() == tableNumber)
                .findFirst().orElse(null);

        Drink drink = this.drinkRepository.getAll().stream()
                .filter(currentDrink -> currentDrink.getName().equals(drinkName) && currentDrink.getBrand().equals(drinkBrand))
                .findFirst().orElse(null);

        if (table == null) {
            return String.format(WRONG_TABLE_NUMBER, tableNumber);
        }

        if (drink == null) {
            return String.format(NON_EXISTENT_DRINK, drinkName, drinkBrand);
        }

        table.orderDrink(drink);

        return String.format(DRINK_ORDER_SUCCESSFUL, tableNumber, drinkName, drinkBrand);
    }

    @Override
    public String leaveTable(int tableNumber) {
        Table table = this.tableRepository.getAll().stream()
                .filter(currentTable -> currentTable.getTableNumber() == tableNumber)
                .findFirst().orElse(null);

        assert table != null;
        double tableBill = table.getBill();

        table.clear();

        return String.format(BILL, tableNumber, tableBill);
    }

    @Override
    public String getFreeTablesInfo() {
        StringBuilder builder = new StringBuilder();

        this.tableRepository.getAll().forEach(currentTable -> {
            if (!currentTable.isReserved()) {
                builder.append(currentTable.getFreeTableInfo()).append(System.lineSeparator());
            }
        });

        return builder.toString().trim();
    }

    @Override
    public String getTotalIncome() {
        double totalIncome = 0;
        return String.format(TOTAL_INCOME, totalIncome);
    }
}