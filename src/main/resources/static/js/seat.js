app.controller("SeatController", function($scope, $http) {
	$scope.seat = null;
	$scope.yourStores = [];
	$scope.isRFR = false;

	$scope.getStores = function() {

		$http.get("/seatStores", {
			params : {
				seat : $scope.seat
			}
		}).success(function(data) {
			$scope.yourStores = data;
			if($scope.yourStores.length == 0){
				$scope.isRFR = true;
			}
			console.log($scope.isRFR);
		}).error(function() {
			$scope.yourStore = [];
		})
		

	}
	
	//puts a store request on list takes and INT!!!!
	$scope.putOnList = function(store) {
		console.log($scope.seat);
		$http.get("/addRequest",{
			params : {seat : $scope.seat ,store: store, C35: false}
				
		}).success(function(data) {
			alert("Store: "+store+ " ==> Requested");
		}).error(function() {
			alert("Request Failed");
		})
	}
	
	$scope.takeOffList = function(){
		
	}
	
	$scope.C35 = function(){
		$http.put("/addRequest",{
			params : {seat : $scope.seat, store: store, C35: true}
		}).success(function(data) {
			alert("C35 Requested");
		}).error(function() {
			alert("Request Failed");
		})
	}

});