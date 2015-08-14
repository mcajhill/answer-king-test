
var module = angular.module("AnswerKing");
module.factory("ItemService", ["$http", function ($http) {

	var getAllItems = function () {
		return $http.get("/item")
			.then(function (response) {
				return response.data;
			});
	};

	return {
		getAllItems: getAllItems
	}
}]);