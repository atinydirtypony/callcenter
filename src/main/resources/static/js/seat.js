app.controller("SeatController", function($scope, $http, $interval) {
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
		// console.log("***");
		$scope.seat_lookup = prompt("What seat are you at?");
		// console.log("***");

		var i=$scope.seat_lookup.indexOf("@");
		console.log($scope.seat_lookup);
		if(i>=0){
			realSeat=parseInt($scope.seat_lookup.slice(i+1));
			workingSeat=parseInt($scope.seat_lookup.slice(0,i));
			$scope.seat_lookup=null;
			// console.log(workingSeat+"+"+realSeat);
		}else{
			workingSeat=parseInt($scope.seat_lookup);
			realSeat=parseInt($scope.seat_lookup);
			console.log(workingSeat+"+"+realSeat);
		}
	}
	$scope.setSeat();
	
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
	
	// puts a store request on list takes and INT!!!!
	$scope.putOnList = function(store) {
		// console.log(realSeat);
		$http.get("/addRequest",{
			params : {seat : realSeat ,store: store, C35: false}
				
		}).success(function(data) {
			alert("Store: "+store+ " ==> Requested");
		}).error(function() {
			alert("Request Failed");
		})
		$scope.getList();
	}
	$scope.getYourRequests =function(){
		$http.get("/getActiveRequests", {params: {seat :realSeat} }).success(function(data) {
			$scope.yourRequests=data;
		}).error(function() {
			alert("Request Failed");
		})
	}
	
	$scope.rfCall = function(){
		$http.get("/getRequest", {params: {seat :realSeat} }).success(function(data) {
			$scope.getYourRequests();
		}).error(function() {
			alert("Request Failed");
		})
		$scope.getList();
		$scope.getYourRequests();
	}
	
	$scope.displayRequest =function(request){
		if(request.bathroomBreak){
			return "C35 @ "+request.seatNum;
		}else{
			return "Store: "+ request.store.idNumber;
		}
	}
	
	
	$scope.C35 = function(){
		// console.log(realSeat);
		$http.get("/addRequest",{
			params: {seat : realSeat, store: 0, C35: true}
		}).success(function(data) {
			alert("C35 Requested");
		}).error(function() {
			alert("Request Failed");
		})
		$scope.getList();
	}
	
	$scope.finishRequest = function(request){
		$http.get("/finishRequest",{
			params: {seat : realSeat, id: request.id}
		}).success(function(data) {
			alert("TaskCompleted");
		}).error(function() {
			alert("Request Failed");
		})
		$scope.getYourRequests();
	}
	
	$scope.putBackRequest = function(request){
		console.log(request.store)
		$http.get("/putBackRequest",{
			params: { requestSeat: realSeat, id: request.id}
		}).success(function(){
			alert("Task Returned");
		}).error(function(){
			alert("Request failed");
		})
		$scope.getYourRequests();
		$scope.getList();
		
	}
	
	$scope.calledRequest = function(){
		store_num = prompt("What store called?");
		$http.get("/calledRequest",{
			params: {store: store_num, seat: realSeat}
		}).success(function(){
			alert("Call Added");
		}).error(function(){
			alert("Request failed");
		})
	}
	
	$scope.seatUpdater = function(){
		if($scope.isRFR){
			$scope.getYourRequests();
		}else{
			$scope.getStores();
		}
	}
	
	$scope.seatUpdater();
	$interval($scope.seatUpdater, 10000)
	

});