app.controller("SeatController", function($scope, $http) {
	$scope.seat_lookup = null;
	$scope.yourStores = [];
	$scope.isRFR = false;
	$scope.yourRequests =[];
	var workingSeat = null;
	var realSeat = null;
	
	$scope.getSeat = function(){
		if(workingSeat == null || realSeat ==null){
			return null;
		}else if(workingSeat == realSeat){
			return workingSeat;
		}else{
			return workingSeat +"@"+realSeat;
		}
	}
	
	$scope.setSeat = function(){
		//console.log("***");
		$scope.seat_lookup = prompt("What seat are you at?");
		//console.log("***");

		var i=$scope.seat_lookup.indexOf("@");
		console.log($scope.seat_lookup);
		if(i>=0){
			realSeat=parseInt($scope.seat_lookup.slice(i+1));
			workingSeat=parseInt($scope.seat_lookup.slice(0,i));
			$scope.seat_lookup=null;
			//console.log(workingSeat+"+"+realSeat);
		}else{
			workingSeat=parseInt($scope.seat_lookup);
			realSeat=parseInt($scope.seat_lookup);
			console.log(workingSeat+"+"+realSeat);
		}
	}
	
	$scope.getStores = function() {

		$http.get("/seatStores", {
			params : {
				seat : workingSeat
			}
		}).success(function(data) {
			$scope.yourStores = data;
			if($scope.yourStores.length == 0){
				$scope.isRFR = true;
			}else{
				$scope.isRFR = false;
			}
			console.log($scope.isRFR);
			console.log($scope.workingSeat);
		}).error(function() {
			$scope.yourStore = [];
		})
		

	}
	
	//puts a store request on list takes and INT!!!!
	$scope.putOnList = function(store) {
		//console.log(realSeat);
		$http.get("/addRequest",{
			params : {seat : realSeat ,store: store, C35: false}
				
		}).success(function(data) {
			alert("Store: "+store+ " ==> Requested");
		}).error(function() {
			alert("Request Failed");
		})
		$scope.getList();
	}
	
	$scope.rfCall = function(){
		$http.get("/getRequest").success(function(data) {
			$scope.yourRequests.push(data);
		}).error(function() {
			alert("Request Failed");
		})
		$scope.getList();
	}
	
	$scope.displayRequest =function(request){
		if(request.bathroomBreak){
			return "C35 @ "+request.seatNum;
		}else{
			return "Store: "+ request.store.idNumber;
		}
	}
	
	$scope.rfFinish = function(){
		$scope.getList();
	}
	
	$scope.C35 = function(){
		//console.log(realSeat);
		$http.get("/addRequest",{
			params: {seat : realSeat, store: 0, C35: true}
		}).success(function(data) {
			alert("C35 Requested");
		}).error(function() {
			alert("Request Failed");
		})
		$scope.getList();
	}

});