package com.mytoporazka.lib.serialization;

import com.mytoporazka.lib.Const;
import com.mytoporazka.lib.domain.*;
import com.mytoporazka.lib.interfaces.Serializable;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Deserializer {
    private final Class<? extends Serializable> $class;
    private static Map<String, DeserializerStrategy> strategyByTypeName;
    private final DeserializerStrategy strategy;

    public Deserializer(Class<? extends Serializable> $class) {
        this.$class = $class;
        if(strategyByTypeName == null)
            initializeStrategies();
        if(!strategyByTypeName.containsKey($class)) {
            // oh no! :(
        }
        strategy = strategyByTypeName.get($class);
    }

    private void initializeStrategies() {
        strategyByTypeName = new HashMap<>();
        strategyByTypeName.put(Address.class.getName(), new AddressDeserializerStrategy());
        strategyByTypeName.put(Bill.class.getName(), new BillDeserializerStrategy());
        strategyByTypeName.put(User.class.getName(), new UserDeserializerStrategy());
        strategyByTypeName.put(Order.class.getName(), new OrderDeserializerStrategy());
        strategyByTypeName.put(Product.class.getName(), new ProductDeserializerStrategy());
        strategyByTypeName.put(OrderEntry.class.getName(), new OrderEntryDeserializerStrategy());
    }

    public Serializable deserialize(String row) {
        var deserializer = strategyByTypeName.get($class.getName());
        return deserializer.deserializeRow(csvRowToCols(row));
    }

    public List<? extends Serializable> deserialize(List<String> rows) {
        var deserializer = strategyByTypeName.get($class.getName());
        return rows.stream().skip(1)
                .map(r -> deserializer.deserializeRow(csvRowToCols(r)))
                .collect(Collectors.toList());
    }

    public static List<String> csvRowToCols(String row) {
        return Arrays.stream(row.split(Const.CSV_DELIM, Integer.MAX_VALUE)).toList();
    }
}

interface DeserializerStrategy {
    Serializable deserializeRow(List<String> rows);
}

class AddressDeserializerStrategy implements DeserializerStrategy {
    @Override
    public Serializable deserializeRow(List<String> rows) {
        return new Address(
                Integer.parseInt(rows.get(0)),
                rows.get(1),
                rows.get(2),
                rows.get(3),
                rows.get(4),
                rows.get(5),
                rows.get(6));
    }
}

class BillDeserializerStrategy implements DeserializerStrategy {
    @Override
    public Serializable deserializeRow(List<String> rows) {
        return new Bill(
                Integer.parseInt(rows.get(0)),
                Instant.parse(rows.get(1)),
                Instant.parse(rows.get(2)),
                Double.parseDouble(rows.get(3))
        );
    }
}

class UserDeserializerStrategy implements DeserializerStrategy {
    @Override
    public Serializable deserializeRow(List<String> rows) {
        return new User(
                Integer.parseInt(rows.get(0)),
                rows.get(1),
                rows.get(2),
                UserStatus.valueOf(rows.get(3)),
                UserType.valueOf(rows.get(4))
        );
    }
}

class OrderDeserializerStrategy implements DeserializerStrategy {
    @Override
    public Serializable deserializeRow(List<String> rows) {
        return new Order(
                Integer.parseInt(rows.get(0)),
                Integer.parseInt(rows.get(1)),
                Instant.parse(rows.get(2)),
                OrderStatus.valueOf(rows.get(3))
        );
    }
}

class ProductDeserializerStrategy implements DeserializerStrategy {
    @Override
    public Serializable deserializeRow(List<String> rows) {
        return new Product(
                rows.get(0),
                Integer.parseInt(rows.get(1)),
                rows.get(2),
                Double.parseDouble(rows.get(3)),
                rows.get(4),
                Boolean.parseBoolean(rows.get(5))
        );
    }
}

class OrderEntryDeserializerStrategy implements DeserializerStrategy {
    @Override
    public Serializable deserializeRow(List<String> rows) {
        return new OrderEntry(
                Integer.parseInt(rows.get(0)),
                Integer.parseInt(rows.get(1)),
                Integer.parseInt(rows.get(2))
        );
    }
}