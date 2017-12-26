var app = angular.module("MyApp", []);

app.controller("PollsCtrl", function($scope, $http) {
 $scope.init = function() {
  //alert($scope.email);
  $http.get('json/polls?email='+encodeURIComponent($scope.email)+'&keyword='+encodeURIComponent($scope.keyword)+'&preset='+encodeURIComponent($scope.preset)).
    success(function(data, status, headers, config) {
      $scope.posts = data;
    }).
    error(function(data, status, headers, config) {
      // log error
    });
  	
 };
 
 $scope.email = 'mh+sample@doodle.com';
 $scope.keyword = 'badminton';
 $scope.preset_= 'this_year';
 
 $scope.init();
});
