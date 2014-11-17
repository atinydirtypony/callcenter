var app = angular.module('CCApp', []);

app.controller("MainController", function($scope, $http, $interval){
	$scope.view = "home";
	$scope.requestList =[];

	$scope.getList = function(){
		$http.get("/getList").success(function(data){
			$scope.requestList = data;
			console.log("do");
		}).error(function(){
			alert("List Failed")
		})
	}
	
	$interval($scope.getList, 500);
	
});