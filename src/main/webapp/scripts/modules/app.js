
var app = angular.module("AnswerKing", ["ngRoute", "ui.router"]);

app.config(["$stateProvider", "$urlRouterProvider", "$locationProvider",
	function ($stateProvider, $urlRouterProvider, $locationProvider) {

		$urlRouterProvider.otherwise("/order/menu");

		$stateProvider
			.state("menu", {
				url: "/order/menu",
				views: {
					main: {
						templateUrl: "views/menuView.html",
						controller: "OrderController"
					}
				}
			})
			.state("pay", {
				url: "/order/pay",
				views: {
					main: {
						templateUrl: "views/cartView.html",
						controller: "PayController"
					}
				}
			});

		$locationProvider.hashPrefix();
		$locationProvider.html5Mode(true);
}]);