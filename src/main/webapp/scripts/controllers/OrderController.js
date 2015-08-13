(function () {

	var OrderController = function ($scope, $route, $location, OrderService, ItemService) {

		$scope.reloadPage = function () {
			$location.path('/');
			$route.reload();
		};

		$scope.addToCart = function (item) {
			OrderService.addToCart(item)
				.then(onAddToCartSuccess(item), onAddToCartFailure);
		};

		$scope.checkout = function () {
			$location.path("/pay");
		};

		$scope.orderContainsItem = function (items, item) {
			var found = false;
			for (var i = 0; i < items.length && !found; i++) {
				found = (items[i].id === item.id);
			}
			return found;
		};

		var onOrderCreateSuccess = function (order) {
			$scope.order = order;
		};

		var onOrderCreateFailure = function (reason) {
			console.log(reason);
			$scope.createOrderError = reason;
		};

		var onGetAllItemsSuccess = function (items) {
			$scope.menu = items;
		};

		var onGetAllItemsFailure = function (reason) {
			console.log(reason);
			$scope.getAllItemsError = reason;
		};

		var onAddToCartSuccess = function (item) {
			$scope.order.items.push(item);
		};

		var onAddToCartFailure = function (reason) {
			console.log(reason);
		};

		// create a new order
		OrderService.createOrder().then(onOrderCreateSuccess, onOrderCreateFailure);
		ItemService.getAllItems().then(onGetAllItemsSuccess, onGetAllItemsFailure);
	};

	var module = angular.module("AnswerKing");
	module.controller("OrderController", OrderController);
}());