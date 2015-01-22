var app = angular.module('CCApp', []);

app.controller("MainController", function($scope, $http, $interval){
	$scope.view = "home";
	$scope.requestList =[];
	tempBB =[];
	$scope.bathroomRequest =[];
	$scope.advisoryLevel = null;
	
	$scope.pad = function(num, size) {
	    var s = num+"";
	    while (s.length < size) s = "0" + s;
	    return s;
	}
	
	$scope.getList = function(){
		$http.get("/getList").success(function(data){
			$scope.requestList = data;
			tempBB=[];
			for(i=0;i<$scope.requestList.length; i++){
				if($scope.requestList[i].type == "C35"){
					tempBB.push($scope.requestList.splice(i,1)[0]);
					i--;
				}
			}
			//console.log(tempBB);
			$scope.bathroomRequest = tempBB;
		}).error(function(){
			alert("List Failed")
		})
	}
	
	$scope.getAdvisoryLevel = function(){
		$http.get("/getAdvisory").success(function(data){
			$scope.advisoryLevel=data;
		}).error(function(){
			alert("Advisory Failed");
		})

	}
	
	$interval($scope.getList, 5000);
	$interval($scope.getAdvisoryLevel, 5000);
	
});