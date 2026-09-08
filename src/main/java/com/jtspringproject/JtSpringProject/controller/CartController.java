package com.jtspringproject.JtSpringProject.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.services.cartService;
import com.jtspringproject.JtSpringProject.services.productService;
import com.jtspringproject.JtSpringProject.services.userService;

/**
 * This controller did not exist before - cartService/cartDao/cartProductDao
 * were fully implemented but never exposed over HTTP, so "Add To Cart" and
 * the cart page had nothing to call.
 */
@Controller
public class CartController {

	private final cartService cartService;
	private final productService productService;
	private final userService userService;

	public CartController(cartService cartService, productService productService, userService userService) {
		this.cartService = cartService;
		this.productService = productService;
		this.userService = userService;
	}

	@GetMapping("/cart")
	public ModelAndView viewCart() {
		User user = currentUser();
		ModelAndView mView = new ModelAndView("cartproduct");

		if (user == null) {
			mView.addObject("msg", "Please log in to view your cart.");
			return mView;
		}

		List<Product> products = this.cartService.getProductsInCart(user);
		if (products.isEmpty()) {
			mView.addObject("msg", "Your cart is empty.");
		} else {
			mView.addObject("cartProducts", products);
		}
		return mView;
	}

	@PostMapping("/cart/add")
	public String addToCart(@RequestParam("productId") int productId) {
		User user = currentUser();
		Product product = this.productService.getProduct(productId);

		if (user != null && product != null) {
			this.cartService.addProductToCart(user, product);
		}
		return "redirect:/cart";
	}

	@PostMapping("/cart/remove")
	public String removeFromCart(@RequestParam("productId") int productId) {
		User user = currentUser();
		Product product = this.productService.getProduct(productId);

		if (user != null && product != null) {
			this.cartService.removeProductFromCart(user, product);
		}
		return "redirect:/cart";
	}

	private User currentUser() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}
		return this.userService.getUserByUsername(authentication.getName());
	}
}
