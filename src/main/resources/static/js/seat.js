app.controller("SeatController", function($scope, $http) {
	$scope.seat_lookup = null;
	$scope.yourStores = [];
	$scope.isRFR = false;
	var workingSeat = null;
	var realSeat = null;

	$scope.getStores = function() {
		
		var i=$scope.seat_lookup.indexOf("@");
		//console.log($scope.seat_lookup);
		if(i>=0){
			realSeat=parseInt($scope.seat_lookup.slice(i+1));
			workingSeat=parseInt($scope.seat_lookup.slice(0,i));
			$scope.seat_lookup=null;
			//console.log(workingSeat+"+"+realSeat);
		}else{
			workingSeat=parseInt($scope.seat_lookup);
			realSeat=parseInt($scope.seat_lookup);
			//console.log(workingSeat+"+"+realSeat);
		}

		$http.get("/seatStores", {
			params : {
				seat : workingSeat
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
		console.log(workingSeat);
		$http.get("/addRequest",{
			params : {seat : workingSeat ,store: store, C35: false}
				
		}).success(function(data) {
			alert("Store: "+store+ " ==> Requested");
		}).error(function() {
			alert("Request Failed");
		})
	}
	
	$scope.rfCall = function(){
		
	}
	
	$scope.rfFinish = function(){
		
	}
	
	$scope.C35 = function(){
		console.log(workingSeat);
		$http.get("/addRequest",{
			params: {seat : workingSeat, store: 0, C35: true}
		}).success(function(data) {
			alert("C35 Requested");
		}).error(function() {
			alert("Request Failed");
		})
	}

});