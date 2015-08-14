
var app = angular.module("AnswerKing", ['ngRoute']);
app.config(function ($routeProvider) {

	$routeProvider
		.when("/", {
			templateUrl: "views/menuView.html",
			controller: "OrderController"
		})
		.when("/pay", {
			templateUrl: "views/cartView.html",
			controller: "PayController"
		})
		.otherwise({
			redirectTo: "/"
		});
});