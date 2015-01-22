app.controller("HomeController", function($scope, $http, $interval) {
	$scope.bulletins=[];
	
	$scope.getBulletins=function(){
		$http.get("/getBulletins").success(function(data){
			$scope.bulletins=data;
			$scope.bulletins.sort(function(a,b){return (b.alert-a.alert)})
			console.log($scope.bulletins)
			for(i=0; i<$scope.bulletins.length; i++){
				console.log($scope.bulletins[i].posted);
				$scope.bulletins[i].posted = new Date ($scope.bulletins[i].posted);
				console.log($scope.bulletins[i].posted);
			}
		}).error(function(){
			alert("Request failed");
		})
	}
	
	$scope.bulletinDate=function(bullet){
		d="";
		d=d+(bullet.posted.getMonth()+1)+"/"+bullet.posted.getDate()+"/"+bullet.posted.getFullYear()
		return d;
	}
	
});