package com.jtspringproject.JtSpringProject.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jtspringproject.JtSpringProject.dao.cartDao;
import com.jtspringproject.JtSpringProject.dao.cartProductDao;
import com.jtspringproject.JtSpringProject.models.Cart;
import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

	@Mock
	private cartDao cartDao;

	@Mock
	private cartProductDao cartProductDao;

	private cartService cartService;

	private User user;
	private Cart existingCart;

	@BeforeEach
	void setUp() {
		cartService = new cartService(cartDao, cartProductDao);

		user = new User();
		user.setId(1);
		user.setUsername("lisa");

		existingCart = new Cart();
		existingCart.setId(10);
		existingCart.setCustomer(user);
	}

	@Test
	void getOrCreateCartForUser_returnsExistingCart_whenOneAlreadyExists() {
		when(cartDao.getCartByCustomerId(1)).thenReturn(existingCart);

		Cart result = cartService.getOrCreateCartForUser(user);

		assertEquals(existingCart, result);
		verify(cartDao, never()).addCart(any());
	}

	@Test
	void getOrCreateCartForUser_createsNewCart_whenNoneExists() {
		when(cartDao.getCartByCustomerId(1)).thenReturn(null);
		when(cartDao.addCart(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Cart result = cartService.getOrCreateCartForUser(user);

		assertEquals(user, result.getCustomer());
		verify(cartDao, times(1)).addCart(any(Cart.class));
	}

	@Test
	void addProductToCart_addsProduct_whenNotAlreadyInCart() {
		Product product = new Product();
		product.setId(100);

		when(cartDao.getCartByCustomerId(1)).thenReturn(existingCart);
		when(cartProductDao.getProductByCartID(10)).thenReturn(List.of());

		cartService.addProductToCart(user, product);

		verify(cartProductDao, times(1)).addCartProduct(any());
	}

	@Test
	void addProductToCart_isNoOp_whenProductAlreadyInCart() {
		Product product = new Product();
		product.setId(100);

		when(cartDao.getCartByCustomerId(1)).thenReturn(existingCart);
		when(cartProductDao.getProductByCartID(10)).thenReturn(List.of(product));

		cartService.addProductToCart(user, product);

		verify(cartProductDao, never()).addCartProduct(any());
	}

	@Test
	void removeProductFromCart_deletesTheLink() {
		Product product = new Product();
		product.setId(100);

		when(cartDao.getCartByCustomerId(1)).thenReturn(existingCart);

		cartService.removeProductFromCart(user, product);

		verify(cartProductDao, times(1)).deleteCartProduct(any());
	}

	@Test
	void getProductsInCart_returnsWhatTheDaoReturns() {
		Product product = new Product();
		product.setId(100);

		when(cartDao.getCartByCustomerId(1)).thenReturn(existingCart);
		when(cartProductDao.getProductByCartID(10)).thenReturn(List.of(product));

		List<Product> result = cartService.getProductsInCart(user);

		assertTrue(result.contains(product));
	}
}
