app.controller("RPHController", function($scope, $http, $interval) {
	$scope.yourRequests=[];
	$scope.RPHList=[];
	
	$scope.getYourRequests =function(){
			$http.get("/getActiveRPHRequests").success(function(data) {
				$scope.yourRequests=data;
				console.log($scope.yourRequests);
			}).error(function() {
				alert("Request Failed");
			});
	}
	
	$scope.getRPHList = function(){
		$http.get("/getRPHrequests").success(function(data) {
			$scope.RPHList=data;
			//console.log($scope.RPHList);
			$scope.getYourRequests();
		}).error(function() {
			alert("List Failed");
		})
	}
	
	$scope.updater=function(){
		$scope.getRPHList();
		$scope.getYourRequests();
	}
	
	$scope.takeRequest=function(){
		$http.get("/takeRPHRequest").success(function() {
			$scope.updater();
		}).error(function() {
			alert("Request Failed");
		})
	}
	
	$scope.finishRPHRequest = function(id){
		$http.get("/finishRPHRequest", {params: {id :id} }).success(function() {
			
		}).error(function() {
			alert("Request Failed");
		})
	}
	
	$scope.displayRequest=function(request){
		return request.stamps[0].seat;
		
	}
	
	$interval($scope.updater, 2000)
	
});