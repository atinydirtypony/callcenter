app.controller("BulletinController", function($scope, $http, $interval) {
$scope.note=null;
$scope.life = 2;
$scope.alert = false;

$scope.postBulletin = function(){
	console.log($scope.note);
	$http.post("/postBulletin", $scope.note).success(function(data){
		
		$scope.note=null;
		if($scope.life != 2 || $scope.alert == true){
			console.log("life= "+$scope.life)
			$http.get("/updateBulletin",{
				params: {id: data, alert: $scope.alert, life: $scope.life}
			}).success(function(){
				$scope.life = 2;
				$scope.alert = false;
			}).error(function(){
				alert("Request failed");
			})
		//console.log(data);
		}
	});
}

});