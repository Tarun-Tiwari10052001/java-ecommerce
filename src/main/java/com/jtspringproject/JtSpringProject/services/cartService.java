package com.jtspringproject.JtSpringProject.services;

import java.util.List;

import com.jtspringproject.JtSpringProject.dao.cartDao;
import com.jtspringproject.JtSpringProject.dao.cartProductDao;
import com.jtspringproject.JtSpringProject.models.Cart;
import com.jtspringproject.JtSpringProject.models.CartProduct;
import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class cartService {
    private final cartDao cartDao;
    private final cartProductDao cartProductDao;

    @Autowired
    public cartService(cartDao cartDao, cartProductDao cartProductDao) {
        this.cartDao = cartDao;
        this.cartProductDao = cartProductDao;
    }

    public Cart addCart(Cart cart) {
        return cartDao.addCart(cart);
    }

    public List<Cart> getCarts() {
        return this.cartDao.getCarts();
    }

    public void updateCart(Cart cart) {
        cartDao.updateCart(cart);
    }

    public void deleteCart(Cart cart) {
        cartDao.deleteCart(cart);
    }

    /**
     * Every user gets exactly one cart, created lazily on first use.
     */
    public Cart getOrCreateCartForUser(User user) {
        Cart cart = cartDao.getCartByCustomerId(user.getId());
        if (cart != null) {
            return cart;
        }
        Cart newCart = new Cart();
        newCart.setCustomer(user);
        return cartDao.addCart(newCart);
    }

    public List<Product> getProductsInCart(User user) {
        Cart cart = getOrCreateCartForUser(user);
        return cartProductDao.getProductByCartID(cart.getId());
    }

    /**
     * Adds a product to the user's cart. A product can only be in the cart
     * once (the current schema links cart<->product by existence, it has no
     * per-line quantity column) - adding an already-present product is a
     * no-op rather than a duplicate-key error.
     */
    public void addProductToCart(User user, Product product) {
        Cart cart = getOrCreateCartForUser(user);
        boolean alreadyInCart = cartProductDao.getProductByCartID(cart.getId()).stream()
                .anyMatch(p -> p.getId() == product.getId());
        if (!alreadyInCart) {
            cartProductDao.addCartProduct(new CartProduct(cart, product));
        }
    }

    public void removeProductFromCart(User user, Product product) {
        Cart cart = getOrCreateCartForUser(user);
        cartProductDao.deleteCartProduct(new CartProduct(cart, product));
    }
}
