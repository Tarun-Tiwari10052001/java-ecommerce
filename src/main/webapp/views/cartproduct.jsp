<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">

<title>Your Cart</title>
</head>
<body class="bg-light">
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
		<div class="container-fluid">
			<a class="navbar-brand" href="/">Perishable Shop</a>
			<div class="collapse navbar-collapse">
				<ul class="navbar-nav ml-auto">
					<li class="nav-item"><a class="nav-link" href="/user/products">Continue Shopping</a></li>
					<li class="nav-item"><a class="nav-link" href="/logout">Logout</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container-fluid">
		<h2 class="mt-3">Your Cart</h2>

		<c:if test="${not empty msg}">
			<p class="alert alert-info">${msg}</p>
		</c:if>

		<c:if test="${not empty cartProducts}">
			<table class="table">
				<tr>
					<th scope="col">Product</th>
					<th scope="col">Price</th>
					<th scope="col">Description</th>
					<th scope="col">Remove</th>
				</tr>
				<tbody>
					<c:forEach var="product" items="${cartProducts}">
						<tr>
							<td>${product.name}</td>
							<td>${product.price}</td>
							<td>${product.description}</td>
							<td>
								<form action="/cart/remove" method="post">
									<input type="hidden" name="productId" value="${product.id}">
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
									<input type="submit" value="Remove" class="btn btn-danger">
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>

	<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
		integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
		integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
		crossorigin="anonymous"></script>
</body>
</html>
