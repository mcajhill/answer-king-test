
var app = angular.module("AnswerKing", ["ngRoute"]);
app.config(["$routeProvider", "$locationProvider", function ($routeProvider, $locationProvider) {

	$routeProvider
		.when("/order/menu", {
			templateUrl: "views/menuView.html",
			controller: "OrderController"
		})
		.when("/order/pay", {
			templateUrl: "views/cartView.html",
			controller: "PayController"
		})
		.otherwise({
			redirectTo: "/order/menu"
		});

	$locationProvider.hashPrefix();
	$locationProvider.html5Mode(true);
}]);