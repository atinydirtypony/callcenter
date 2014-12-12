app.controller("AdminController", function($scope, $http, $interval) {
	
	$scope.changeGroups = function(groups){
		$http.get("/changeGroup",{
			params: { group: groups}
		}).success(function(data){
			$scope.getAdvisoryLevel();
		}).error(function(){
			alert("Group Change Failed");
		})
	}
	
	$scope.changeGroups = function(num){
		$http.get("/setCutOff",{
			params: { number: num}
		}).success(function(data){
			$scope.getAdvisoryLevel();
		}).error(function(){
			alert("Group Change Failed");
		})
	}
	
});