(function () {

	var PayController = function ($scope, OrderService) {

		$scope.getOrderTotal = function () {
			var order = $scope.order;
			var orderTotal = 0;

			if (order) {
				order.items.forEach(function (item) {
					orderTotal += (item.price * item.quantity);
				});
			}

			return orderTotal.toFixed(2);
		};

		$scope.pay = function () {
			OrderService.pay($scope.order.reciept.payment)
				.then(onPaySuccess, onPayFailure);
		};

		var onPaySuccess = function (reciept) {
			$scope.order.reciept = reciept;
			$scope.order.paid = true;
			$scope.paymentSuccess = true;
		};

		var onPayFailure = function (reason) {
			console.log(reason);
			$scope.paymentError = reason;
			$scope.paymentSuccess = false;
		};

		var onGetOrderSuccess = function (order) {
			$scope.order = order;
		};

		var onGetOrderFailure = function (reason) {
			console.log(reason);
		};

		OrderService.getOrder().then(onGetOrderSuccess, onGetOrderFailure);
	};

	var module = angular.module("AnswerKing");
	module.controller("PayController", PayController);
}());