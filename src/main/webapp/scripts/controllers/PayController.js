
var module = angular.module("AnswerKing");
module.controller("PayController", function ($scope, OrderService) {

	$scope.getOrderTotalPrice = function () {
		var order = $scope.order;
		var orderTotal = 0;

		if (order) {
			order.items.forEach(function (item) {
				orderTotal += (item.price * item.quantity);
			});
		}

		return orderTotal.toFixed(2);
	};

	$scope.getItemTotalPrice = function (item) {
		var total = item.price * item.quantity;
		return total.toFixed(2);
	};

	$scope.pay = function () {
		OrderService.pay($scope.order.reciept.payment)
			.then(onPaySuccess, onPayFailure);
	};

	var getOrder = function () {
		OrderService.getOrder().then(onOrderCreateUpdateSuccess, onGenericFailure);
	};

	var onPaySuccess = function () {
		getOrder();
		$scope.paymentSuccess = true;
	};

	var onPayFailure = function (reason) {
		console.log(reason);
		$scope.paymentError = reason;
		$scope.paymentSuccess = false;
	};

	var onOrderCreateUpdateSuccess = function (order) {
		$scope.order = order;
	};

	var onGenericFailure = function (reason) {
		console.log(reason);
	};

	getOrder();
});