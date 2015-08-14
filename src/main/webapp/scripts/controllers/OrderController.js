
var module = angular.module("AnswerKing");
module.controller("OrderController", function ($scope, $route, $location, OrderService, ItemService) {

	$scope.reloadPage = function () {
		$location.path('/');
		$route.reload();
	};

	$scope.checkout = function () {
		$location.path("/pay");
	};

	$scope.addToCart = function (item) {
		if (item.quantity) {
			OrderService.addToCart(item).then(getOrder, onGenericFailure);
		}
	};

	$scope.orderContainsItem = function (item) {
		return getItemIndexFromCart(item) !== -1;
	};

	$scope.getTotalQtyForItem = function (item) {
		var index = getItemIndexFromCart(item);

		if (index !== -1) {
			return $scope.order.items[index].quantity;
		}
		else {
			return 0;
		}
	};

	var getItemIndexFromCart = function (item) {
		if (!$scope.order || !$scope.order.items) {
			return -1;
		}

		var items = $scope.order.items;

		for (var i = 0; i < items.length; i++) {
			var sameName = (items[i].name === item.name);
			var samePrice = (items[i].price === item.price);

			if (sameName && samePrice) {
				return i;
			}
		}
		return -1;
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
});
