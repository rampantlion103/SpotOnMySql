// MODULE
var weatherApp = angular.module('weatherApp',['ngRoute','ngResource']);

// ROUTES
weatherApp.config(function($routeProvider) {
    
 $routeProvider
 
 .when('/', {
     templateUrl: 'pages/home.htm',
     controller: 'homeController'
 })
 
  .when('/forecast', {
     templateUrl: 'pages/forecast.htm',
     controller: 'forecastController'
 })
 
  .when('/forecast/:days', {
     templateUrl: 'pages/forecast.htm',
     controller: 'forecastController'
 })
 
});


// SERVICES
weatherApp.service('mainService', function() {
    
    this.showDevelopment = true;
    this.city = "New York, NY"
    
});

// CONTROLLERS
weatherApp.controller('homeController',['$scope','mainService', function($scope,mainService) {
    
    $scope.showDevelopment = mainService.showDevelopment;
    $scope.city = mainService.city;
    
    $scope.$watch('city', function() {
       mainService.city = $scope.city;
    });
    
    console.log($scope);
    
}]);

weatherApp.controller('forecastController',['$scope','$resource','$routeParams','mainService', function($scope,$resource,$routeParams,mainService) {
      
    $scope.showDevelopment = mainService.showDevelopment;
    $scope.city = mainService.city;
    $scope.days = $routeParams.days || '2';
    
      $scope.$watch('city', function() {
       mainService.city = $scope.city;
    });
    
    $scope.weatherAPI = $resource("http://api.openweathermap.org/data/2.5/forecast/daily",{callback: "JSON_CALLBACK"},{get: {method: "JSONP"}});
    
    $scope.weatherResult = $scope.weatherAPI.get({q: $scope.city, cnt: $scope.days, appid: "2de143494c0b295cca9337e1e96b00e0"});

    $scope.convertToFahrenheit = function(degK) {
        return Math.round((1.8 * (degK - 273)) + 32)};
    
    $scope.convertToDate = function(dt) {
        return new Date(dt * 1000);};
    
    console.log($scope.weatherResult);
    
    

}]);