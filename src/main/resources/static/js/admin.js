app.controller("AdminController", function($scope, $http, $interval) {
	
	$scope.changeGroups = function(groups){
		$http.get("/changeGroup",{
			params: { group: groups}
		}).success(function(data){
		}).error(function(){
			alert("Group Change Failed");
		})
	}
	
}