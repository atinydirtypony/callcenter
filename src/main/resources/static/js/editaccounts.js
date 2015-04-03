app.controller("EditAccounts", function($scope, $http){
	
	$scope.account_id = null;
	$scope.account =null;
	var accountIsReal =false;
	
	
	$scope.accountLookUp = function(){
		
		$http.get("/accountByUsername",{params:{id:$scope.account_id}}).success(function(data){
			$scope.account = data;
			if($scope.account != null && $scope.account.length > 1){
				accountIsReal = true;
			}else{
				accountIsReal = false;
			}
			//console.log($scope.account);
		}).error(function(){
			$scope.account = null;
			accountIsReal = false;
		})
	}
	
	$scope.saveAccount = function(){
		console.log(accountIsReal);
		
		if(accountIsReal){
			clone= {username: $scope.account.username.toUpperCase(),password:$scope.account.password,enabled:$scope.account.enabled,firstName:$scope.account.firstName,lastName:$scope.account.lastName,callsCert:$scope.account.callsCert,dataEntryCert:$scope.account.dataEntryCert,
					reportCert:$scope.account.reportCert,role:$scope.account.role}
			$http.post("/saveAccount", clone);
			$scope.account = null;
			accountIsReal = false;
		}
		else{
			clone= {username: $scope.account_id.toUpperCase(),password:$scope.account_id.toUpperCase(),enabled:true,firstName:$scope.account.firstName,lastName:$scope.account.lastName,callsCert:false,dataEntryCert:false,
					reportCert:false,role: $scope.getRole()}
			$http.post("/newAccount", clone);
			$scope.account = null;
			accountIsReal = false;
		}
	}
	
	$scope.getRole = function(){
		//console.log($scope.account.role);
		if($scope.account && $scope.account.role){
			return $scope.account.role;
		}else{
			return "USER";
		}
	}
	
	$scope.typeClick= function(){
		if($scope.account.role){
			if($scope.account.role == "USER"){
				$scope.account.role = "LT";
			}else if($scope.account.role == "LT"){
				$scope.account.role = "RPH";
			}else if($scope.account.role == "RPH"){
				$scope.account.role = "USER";
			}
		}else{
			if($scope.account){
				$scope.account.role = "LT";
			}
		}
	}
	
	$scope.userClass = function() {
		var role = $scope.getRole();
		if(role == 'USER') {
			return 'btn-success';
		} else if (role == 'LT'){
			return 'btn-warning';
		} else {
			return 'btn-danger'
		}
	}
	
	$scope.resetPassword = function(){
		
		$http.get("/resetPassword",{params:{id:$scope.account_id}}).success(function(data){
			$scope.account =null;
			accountIsReal =false;
		}).error(function(){
			
		})
		
	}
	
});