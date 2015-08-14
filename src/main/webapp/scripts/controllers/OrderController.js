(function () {

	var OrderController = function ($scope, $route, $location, OrderService, ItemService) {

		$scope.reloadPage = function () {
			$location.path('/');
			$route.reload();
		};

		$scope.checkout = function () {
			$location.path("/pay");
		};

		$scope.addToCart = function (item) {
			OrderService.addToCart(item).then(getOrder, onGenericFailure);
		};

		$scope.orderContainsItem = function (item) {
			if (!$scope.order) {
				return;
			}

			var items = $scope.order.items;
			var found = false;

			if (items) {
				for (var i = 0; i < items.length && !found; i++) {
					found = (items[i].name === item.name) && (items[i].price === item.price);
				}
			}
			return found;
		};

		$scope.getTotalQtyForItem = function (item) {
			if (!$scope.order) {
				return;
			}

			var items = $scope.order.items;
			if (items) {
				for (var i = 0; i < items.length && !found; i++) {
					var found = (items[i].name === item.name) && (items[i].price === item.price);
					if (found) {
						return items[i].quantity;
					}
				}
			}
		};

		var getOrder = function () {
			OrderService.getOrder().then(onOrderCreateUpdateSuccess, onGenericFailure);
		};

		var onOrderCreateUpdateSuccess = function (order) {
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

		var onGenericFailure = function (reason) {
			console.log(reason);
		};

		// create a new order
		OrderService.createOrder().then(onOrderCreateUpdateSuccess, onOrderCreateFailure);
		ItemService.getAllItems().then(onGetAllItemsSuccess, onGetAllItemsFailure);
	};

	var module = angular.module("AnswerKing");
	module.controller("OrderController", OrderController);
}());