var app = angular.module('CCApp', []);

app.controller("MainController", function($scope){
	$scope.view = "home";
	$scope.advisoryLevel = "C"; //what group people are in A, B, C, and Sat default is C
	
	if(new Date().getDay() == 6){
		$scope.advisoryLevel = "Sat"; // sets group to sat if it's sat
	}

	
});