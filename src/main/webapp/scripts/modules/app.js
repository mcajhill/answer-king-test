(function () {

	var app = angular.module("AnswerKing", ["ng-route"]);

	app.config(function ($routeProvider) {

		$routeProvider
			.when("/order", {
				templateUrl: "menuView.html",
				controller: "OrderController"
			})
			.when("/order/:orderId/pay", {
				templateUrl: "cartView.html",
				controller: "PayController"
			})
			.otherwise({
				redirectTo: "/order"
			});
	});
}());