app.controller("SeatController", function($scope, $http, $interval) {
	$scope.seat_lookup = null;
	$scope.yourStores = [];
	$scope.isRFR = false;
	$scope.yourRequests =[];
	$scope.workingSeat = null;
	$scope.realSeat = null;
	$scope.storesNeedChecked=null;
	$scope.allStores=[];
	$scope.callStoreNum=null;
	
	$scope.callClick = false;

	
	$scope.allSeats = null;
	$scope.groups = null;
	
	$scope.getGroups = function(){
		$http.get("/getGroups")
		.success(function(data) {
			$scope.groups = data;
			$scope.groups.push("List Keeper");
			$scope.groups.push("RFR");
		}).error(function() {
			alert("Failed to get groups;");
		})
		
		$http.get("/getSeats")
		.success(function(data) {
			$scope.allSeats = data;;
		}).error(function() {
			alert("Failed to get groups;");
		})
	}
	
	$scope.defaultGroup = function(){
		console.log($scope.realSeat);
		if($scope.realSeat.indexOf("RF") >=0){
			tempGroup = $scope.realSeat.slice(2);
			//console.log(tempGroup);
			if($scope.groups.indexOf(tempGroup)>=0){
				$scope.workingSeat = $scope.groups[$scope.groups.indexOf(tempGroup)];
			}else{
				$scope.workingSeat = $scope.groups[$scope.groups.length-1];
			}
		}else{
			$scope.workingSeat = $scope.groups[$scope.groups.length-1];
		}
		$scope.getStores();
	}
	
	$scope.getSeat = function(){
		tempGroup=null;
		if($scope.realSeat.indexOf("RF") >=0){
			tempGroup = $scope.realSeat.slice(2);
		}
		if($scope.workingSeat == null || $scope.realSeat ==null){
			return null;
		}else if($scope.workingSeat == tempGroup || $scope.realSeat == "RFR"){
			return $scope.realSeat;
		}else{
			return $scope.workingSeat +"@"+$scope.realSeat;
		}
	}
	
	/*
	 * $scope.setSeat = function(look_up){ // console.log("***");
	 * $scope.seat_lookup = look_up; // console.log("***");
	 * 
	 * var i=$scope.seat_lookup.indexOf("@"); console.log($scope.seat_lookup);
	 * if(i>=0){ $scope.realSeat=parseInt($scope.seat_lookup.slice(i+1));
	 * $scope.workingSeat=parseInt($scope.seat_lookup.slice(0,i));
	 * $scope.seat_lookup=null; // console.log(workingSeat+"+"+realSeat); }else{
	 * $scope.workingSeat=parseInt($scope.seat_lookup);
	 * $scope.realSeat=parseInt($scope.seat_lookup);
	 * console.log($scope.workingSeat+"+"+$scope.realSeat); } }
	 */
	

	
	$scope.getStores = function() {
		if($scope.workingSeat != null){
			$http.get("/seatStores", {
				params : {
					seat : $scope.workingSeat
				}
			}).success(function(data) {
				$scope.yourStores = data;
				if($scope.yourStores.length == 0){
					$scope.isRFR = true;
				}else{
					$scope.isRFR = false;
				}
				console.log("is RFR: "+ $scope.isRFR);
				console.log("working seat: "+ $scope.workingSeat);
			}).error(function() {
				$scope.yourStore = [];
			})
		}

	}
	
	// puts a store request on list takes and INT!!!!
	$scope.putOnList = function(store) {
		// console.log(realSeat);
		$http.get("/addRequest",{
			params : {seat : $scope.realSeat ,store: store, C35: false}
				
		}).success(function(data) {
			// alert("Store: "+store+ " ==> Requested");
			$scope.getList();
		}).error(function() {
			alert("Request Failed");
		})
		
	}
	$scope.getYourRequests =function(){
		if($scope.realSeat !=null){
			$http.get("/getActiveRequests").success(function(data) {
				$scope.yourRequests=data;
			}).error(function() {
				alert("Request Failed");
			})
		}
	}
	
	$scope.rfCall = function(){
		$http.get("/getRequest", {params: {seat :$scope.realSeat} }).success(function(data) {
			$scope.getYourRequests();
			$scope.getList();
		}).error(function() {
			alert("Request Failed");
		})
		
	}
	
	$scope.displayRequest =function(request){
		if(request.type == "C35"){
			return "C35 @ "+request.stamps[0].seat;
		}else{
			return "Store: "+ request.store.idNumber;
		}
	}
	
	
	$scope.C35 = function(){
		// console.log(realSeat);
		$http.get("/addRequest",{
			params: {seat : $scope.realSeat, store: 0, C35: true}
		}).success(function(data) {
			$scope.getList();
			// alert("C35 Requested");
		}).error(function() {
			alert("Request Failed");
		})
		
	}
	
	$scope.finishRequest = function(request){
		$http.get("/finishRequest",{
			params: {seat : $scope.realSeat, id: request.id}
		}).success(function(data) {
			$scope.getYourRequests();
			// alert("TaskCompleted");
		}).error(function() {
			alert("Request Failed");
		})
		
	}
	
	$scope.putBackRequest = function(request){
		console.log(request.store)
		$http.get("/putBackRequest",{
			params: { requestSeat: $scope.realSeat, id: request.id}
		}).success(function(){
			$scope.getYourRequests();
			$scope.getList();
			// alert("Task Returned");
		}).error(function(){
			alert("Request failed");
		})
		
		
	}
	
	$scope.calledRequest = function(){
		console.log("SCOPE", $scope)
		console.log($scope.callStoreNum);
		$http.get("/calledRequest",{
			params: {store: $scope.callStoreNum, seat: $scope.realSeat}
		}).success(function(){
			$scope.getYourRequests();
			$scope.callClick = false;
			// alert("Call Added");
		}).error(function(){
			alert("Request failed");
		})
	}
	
	$scope.storeCallClick=function(){
		if(!$scope.callClick){
			$http.get("/getAllStores")
			.success(function(data){
				$scope.allStores=data;
				$scope.callClick = true;
				console.log($scope.allStores);
			});
		}else{
			$scope.callClick = false;
		}
	}
	
	$scope.seatUpdater = function(){
		if($scope.isRFR){
			$scope.getYourRequests();
		}else{
			$scope.getStores();
		}
	}
	$scope.getHelp = function(request){
		// console.log(request.store)
		$http.get("/helpRequest",{
			params: {id: request.id}
		}).success(function(){
			$scope.getYourRequests();
			$scope.getList();
			// alert("Task Returned");
		}).error(function(){
			alert("Request failed");
		})
	}
	
	$scope.helpCheck = function(request){
		// xconsole.log(request);
		if(!request.helpRequest && !request.beingHelped && request.type != "C35"){
			return true;
		}else{
			return false;
		}
	}
	
	$scope.getStoresNeedChecked =function(){
		$http.get("/lastCheckedList").success(function(data){
			$scope.storesNeedChecked = data.slice(0,5);
		}).error(function(){
			$scope.storesNeedChecked = null;
		})
	}
	
	$scope.storesChecked = function(){
		$http.post("/saveLastChecks", $scope.storesNeedChecked).success(function(){
			$scope.getStoresNeedChecked();
		});
		
	}
	
	$scope.setCutOff = function(num){
		$http.get("/setCutOff",{
			params: { number: num}
		}).success(function(data){
			$scope.getAdvisoryLevel();
		}).error(function(){
			alert("Group Change Failed");
		})
	}
	
	$scope.requestRPH=function(){
		$http.get("/addRPHRequest",{
			params: { seat: $scope.realSeat}
		}).success(function(data){
		}).error(function(){
			alert("Group Change Failed");
		})
	}
	
	$scope.seatUpdater();
	$interval($scope.seatUpdater, 10000)
	

});