package com.jtspringproject.JtSpringProject.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.services.cartService;
import com.jtspringproject.JtSpringProject.services.productService;
import com.jtspringproject.JtSpringProject.services.userService;

@WebMvcTest(controllers = CartController.class)
class CartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private cartService cartService;

	@MockBean
	private productService productService;

	@MockBean
	private userService userService;

	private User mockUser() {
		User user = new User();
		user.setId(1);
		user.setUsername("lisa");
		return user;
	}

	@Test
	@WithMockUser(username = "lisa")
	void viewCart_showsProductsInCart() throws Exception {
		Product product = new Product();
		product.setId(100);
		product.setName("Apple");

		when(userService.getUserByUsername("lisa")).thenReturn(mockUser());
		when(cartService.getProductsInCart(any(User.class))).thenReturn(List.of(product));

		mockMvc.perform(get("/cart"))
				.andExpect(status().isOk())
				.andExpect(view().name("cartproduct"))
				.andExpect(model().attribute("cartProducts", List.of(product)));
	}

	@Test
	@WithMockUser(username = "lisa")
	void viewCart_showsEmptyMessage_whenCartHasNoProducts() throws Exception {
		when(userService.getUserByUsername("lisa")).thenReturn(mockUser());
		when(cartService.getProductsInCart(any(User.class))).thenReturn(List.of());

		mockMvc.perform(get("/cart"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("msg", "Your cart is empty."));
	}

	@Test
	@WithMockUser(username = "lisa")
	void addToCart_addsProductAndRedirectsToCart() throws Exception {
		Product product = new Product();
		product.setId(100);

		when(userService.getUserByUsername("lisa")).thenReturn(mockUser());
		when(productService.getProduct(100)).thenReturn(product);

		mockMvc.perform(post("/cart/add")
						.param("productId", "100")
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/cart"));

		verify(cartService, times(1)).addProductToCart(any(User.class), any(Product.class));
	}

	@Test
	@WithMockUser(username = "lisa")
	void addToCart_doesNothing_whenProductDoesNotExist() throws Exception {
		when(userService.getUserByUsername("lisa")).thenReturn(mockUser());
		when(productService.getProduct(anyInt())).thenReturn(null);

		mockMvc.perform(post("/cart/add")
						.param("productId", "999")
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(status().is3xxRedirection());

		verify(cartService, times(0)).addProductToCart(any(User.class), any(Product.class));
	}

	@Test
	@WithMockUser(username = "lisa")
	void removeFromCart_removesProductAndRedirectsToCart() throws Exception {
		Product product = new Product();
		product.setId(100);

		when(userService.getUserByUsername("lisa")).thenReturn(mockUser());
		when(productService.getProduct(100)).thenReturn(product);

		mockMvc.perform(post("/cart/remove")
						.param("productId", "100")
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/cart"));

		verify(cartService, times(1)).removeProductFromCart(any(User.class), any(Product.class));
	}

	@Test
	void addToCart_withoutCsrfToken_isRejected() throws Exception {
		mockMvc.perform(post("/cart/add").param("productId", "100"))
				.andExpect(status().isForbidden());
	}
}
