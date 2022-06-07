package com.mytoporazka.mtsklepspring.components;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Cart {

    //                userId              product  quantity
    private final Map<Integer, List<Tuple<Integer, Integer>>> carts;

    public Cart() {
        carts = new HashMap<>();
    }

    public void addToCart(int userId, int productId, int quantity) {
        if(!carts.containsKey(userId)) {
            carts.put(userId, new ArrayList<>(999));
        }

        var userCart = carts.get(userId);
        if(userCart.stream().anyMatch(c -> c.left  == productId)) {
            var cartEntry = userCart
                    .stream()
                    .filter(c -> c.left == productId)
                    .findFirst()
                    .orElse(new Tuple<>(0, 0));

            cartEntry.right += quantity;
            return;
        }

        userCart.add(new Tuple<>(productId, quantity));
    }

    public void removeFromCart(int userId, int productId, int quantity) {
        if(!carts.containsKey(userId)) {
            return;
        }

        var userCart = carts.get(userId);

        if(quantity == -1 || quantity == userCart.size()) {
            var newCart = userCart
                    .stream()
                    .filter(c -> c.left != productId)
                    .toList();
            carts.put(userId, newCart);
            return;
        }

        var cartEntry = userCart
                .stream()
                .filter(c -> c.left == productId)
                .findFirst()
                .orElse(new Tuple<>(0, 0));

        cartEntry.right -= quantity;
    }

    public void clearCart(int userId) {
        if(!carts.containsKey(userId)) {
            return;
        }

        carts.remove(userId, carts.get(userId));
        carts.put(userId, new ArrayList<>(999));
    }

    public int cartQuantity(int userId) {
        if(!carts.containsKey(userId)) {
            return 0;
        }

        return carts.get(userId)
                .stream()
                .map(c -> c.right)
                .reduce(0, Integer::sum);
    }

    public List<Tuple<Integer, Integer>> getCartForUserId(int userId) {
        if(!carts.containsKey(userId)) {
            return List.of();
        }

        return carts.get(userId);
    }

    public static class Tuple<K, V> {
        public K left;
        public V right;

        public Tuple(K left, V right) {
            this.left = left;
            this.right = right;
        }
    }
}
