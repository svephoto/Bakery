package bakery.repositories;

import bakery.entities.tables.interfaces.Table;
import bakery.repositories.interfaces.Repository;
import bakery.repositories.interfaces.TableRepository;

import java.util.ArrayList;
import java.util.Collection;

public class TableRepositoryImpl implements TableRepository<Table> {
    private Collection<Table> models;

    public TableRepositoryImpl() {
        this.models = new ArrayList<>();
    }

    @Override
    public void add(Table table) {
        this.models.add(table);
    }

    @Override
    public Collection<Table> getAll() {
        return this.models;
    }

    @Override
    public Table getByNumber(int number) {
        return this.models.stream()
                .filter(table -> table.getTableNumber() == number)
                .findFirst().orElse(null);
    }
}
