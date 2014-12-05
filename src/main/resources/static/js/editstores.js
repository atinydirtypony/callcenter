app.controller("EditStores", function($scope, $http){
	
	$scope.store_id = null;
	$scope.realSeat = null;
	$scope.workingSeat = null;
	$scope.store =null;
	var storeIsReal =false;
	
	$scope.storeLookUp = function(){
		
		$http.get("/storeById",{params:{id:$scope.store_id}}).success(function(data){
			$scope.store = data;
			storeIsReal = true;
		}).error(function(){
			$scope.store = null;
			storeIsReal = false;
		})
	}
	
	$scope.saveStore = function(){
		$http.post("/save", $scope.store);
	}
	
});