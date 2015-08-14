(function () {

	var OrderService = function ($http) {

		var order = null;

		var createOrder = function () {
			return $http.post("/order", null)
				.then(function (response) {
					order = response.data;
					order.items = [];
					return order;
				});
		};

		var getOrder = function () {
			return $http.get("/order/" + order.id)
				.then(function (response) {
					return response.data;
				});
		};

		var addToCart = function (item) {
			var url = "/order/" + order.id + "/addItem/" + item.id;
			return $http.put(url, item.quantity);
		};

		var pay = function (payment) {
			var url = "/order/" + order.id + "/pay";

			return $http.put(url, payment)
				.then(function (response) {
					return response.data;
				});
		};

		return {
			createOrder: createOrder,
			getOrder: getOrder,
			addToCart: addToCart,
			pay: pay
		}
	};

	var module = angular.module("AnswerKing");
	module.factory("OrderService", OrderService);
}());