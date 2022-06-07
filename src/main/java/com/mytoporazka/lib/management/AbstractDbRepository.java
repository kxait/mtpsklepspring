package com.mytoporazka.lib.management;

import com.mytoporazka.lib.Const;
import com.mytoporazka.lib.interfaces.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class AbstractDbRepository<T extends Serializable> {
    private final List<T> elems;
    private final String dbHeader;

    AbstractDbRepository(String dbHeader) {
        this.dbHeader = dbHeader;
        elems = new ArrayList<>();
    }

    public long exists(Predicate<T> predicate) {
        return elems.stream().filter(predicate).count();
    }

    public T getFirstByPredicate(Predicate<T> predicate) {
        var vals = getAllByPredicate(predicate);
        if(vals.isEmpty()) return null;
        return vals.get(0);
    }

    public List<T> getAllByPredicate(Predicate<T> predicate) {
        return elems.stream().filter(predicate).toList();
    }

    public boolean modifyByPredicate(Predicate<T> predicate, Consumer<T> modifier) {
        var customer = getAllByPredicate(predicate);
        if(customer.isEmpty()) {
            return false;
        }
        customer.forEach(modifier);
        return true;
    }

    public long deleteByPredicate(Predicate<T> predicate) {
        var filteredCustomers = getAllByPredicate(predicate);
        elems.removeAll(filteredCustomers);
        return filteredCustomers.size();
    }

    public void add(Object elem) {
        elems.add(cast(elem));
    }

    public void add(T elem) { elems.add(elem); }

    abstract T cast(Object elem);

    public List<List<String>> serialize() {
        return Stream.concat(
                Stream.of(List.of(Const.HEADER_TAG + dbHeader)),
                elems.stream()
                    .map(Serializable::serialize))
                    .collect(Collectors.toList());
    }
}
