var app = angular.module('CCApp', []);

app.controller("MainController", function($scope, $http, $interval){
	$scope.view = "home";
	$scope.requestList =[];
	tempBB =[];
	$scope.bathroomRequest =[];
	

	$scope.getList = function(){
		$http.get("/getList").success(function(data){
			$scope.requestList = data;
			tempBB=[];
			for(i=0;i<$scope.requestList.length; i++){
				if($scope.requestList[i].bathroomBreak){
					tempBB.push($scope.requestList.splice(i,1)[0]);
				}
			}
			//console.log(tempBB);
			$scope.bathroomRequest = tempBB;
		}).error(function(){
			alert("List Failed")
		})
	}
	
	$interval($scope.getList, 5000);
	
});