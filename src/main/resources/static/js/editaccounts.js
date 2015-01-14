app.controller("EditAccounts", function($scope, $http){
	
	$scope.account_id = null;
	$scope.account =null;
	var accountIsReal =false;
	
	$scope.accountLookUp = function(){
		
		$http.get("/accountByUsername",{params:{id:$scope.account_id}}).success(function(data){
			$scope.account = data;
			accountIsReal = true;
			console.log($scope.account);
		}).error(function(){
			$scope.account = null;
			accountIsReal = false;
		})
	}
	
	$scope.saveAccount = function(){
		clone= {username: $scope.account.username,password:$scope.account.password,enabled:$scope.account.enabled,firstName:$scope.account.firstName,lastName:$scope.account.lastName,callsCert:$scope.account.callsCert,dataEntryCert:$scope.account.dataEntryCert,
						reportCert:$scope.account.reportCert,role:$scope.account.role}
		$http.post("/saveAccount", clone);
	}
	
});