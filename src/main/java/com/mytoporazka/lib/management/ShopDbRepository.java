package com.mytoporazka.lib.management;

import com.mytoporazka.lib.Const;
import com.mytoporazka.lib.data.TextReader;
import com.mytoporazka.lib.data.TextWriter;
import com.mytoporazka.lib.domain.*;
import com.mytoporazka.lib.interfaces.Serializable;
import com.mytoporazka.lib.serialization.CsvRowHelper;
import com.mytoporazka.lib.serialization.Deserializer;

import java.util.List;
import java.util.stream.Stream;

public class ShopDbRepository {
    public final BillRepository bill;
    public final UserRepository user;
    public final OrderRepository order;
    public final ProductRepository product;
    public final OrderEntryRepository orderEntry;
    public final AddressRepository address;

    public final String dbFileName;

    public ShopDbRepository(String dbFileName) {
        bill = new BillRepository();
        user = new UserRepository();
        order = new OrderRepository();
        product = new ProductRepository();
        orderEntry = new OrderEntryRepository();
        address = new AddressRepository();

        this.dbFileName = dbFileName;
    }

    public void saveToDb() {
        synchronized(this) {
            var allRepos = allRepositories();
            var output = allRepos
                    .map(AbstractDbRepository::serialize)
                    .flatMap(List::stream);

            var rows = output
                    .map(CsvRowHelper::RowToCsv)
                    .toList();

            var writer = new TextWriter(dbFileName);
            writer.writeRows(rows);
        }
    }

    public void loadFromDb() {
        var reader = new TextReader(dbFileName);
        var rows = reader.readFromFile();

        // TODO: cleanup
        AbstractDbRepository<? extends Serializable> repo = null;
        Deserializer deserializer = null;
        Class<? extends Serializable> $class;
        for(var row : rows) {
            if(row.startsWith(Const.HEADER_TAG)) {
                var repoTag = row.substring(Const.HEADER_TAG.length());
                repo = getRepoByTag(repoTag);

                $class = getClassByTag(repoTag);
                deserializer = new Deserializer($class);
                continue;
            }
            if(repo == null) continue;

            var data = deserializer.deserialize(row);
            repo.add(data);
        }
    }

    AbstractDbRepository<? extends Serializable> getRepoByTag(String tag) {
        return switch (tag) {
            case Const.ORDER_ENTRY_REPO_TAG -> orderEntry;
            case Const.BILL_REPO_TAG -> bill;
            case Const.USER_REPO_TAG -> user;
            case Const.ORDER_REPO_TAG -> order;
            case Const.PRODUCT_REPO_TAG -> product;
            case Const.ADDRESS_REPO_TAG -> address;
            default -> null;
        };
    }

    private Class<? extends Serializable> getClassByTag(String tag) {
        return switch (tag) {
            case Const.ORDER_ENTRY_REPO_TAG -> OrderEntry.class;
            case Const.BILL_REPO_TAG -> Bill.class;
            case Const.USER_REPO_TAG -> User.class;
            case Const.ORDER_REPO_TAG -> Order.class;
            case Const.PRODUCT_REPO_TAG -> Product.class;
            case Const.ADDRESS_REPO_TAG -> Address.class;
            default -> null;
        };
    }

    private Stream<AbstractDbRepository<? extends Serializable>> allRepositories() {
        return Stream.of(
                bill,
                user,
                order,
                product,
                orderEntry,
                address
        );
    }

}
