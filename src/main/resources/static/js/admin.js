app.controller("AdminController", function($scope, $http, $interval) {
	$scope.store_num =null;
	$scope.set_cutOff =null;
	
	$scope.changeGroups = function(groups){
		$http.get("/changeGroup",{
			params: { group: groups}
		}).success(function(data){
			$scope.getAdvisoryLevel();
		}).error(function(){
			alert("Group Change Failed");
		})
	}
	
	$scope.setCutOff = function(num){
		console.log(num);
		$http.get("/setCutOff",{
			params: { number: num}
		}).success(function(data){
			$scope.set_cutOff =null;
			$scope.getAdvisoryLevel();
		}).error(function(){
			alert("Group Change Failed");
		})
	}
	
	$scope.putOnList = function(store) {
		// console.log(realSeat);
		$http.get("/addRequest",{
			params : {seat : "ADMIN" ,store: store, C35: false}
				
		}).success(function(data) {
			//alert("Store: "+store+ " ==> Requested");
			$scope.getList();
			$scope.store_num= null;
		}).error(function() {
			alert("Request Failed");
		})
		
	}
	
});