/* variables should be declared' */
'use strict';

var helloWorldControllers =
    angular.module('helloWorldControllers', []);

helloWorldControllers.controller('MainCtrl',
    ['$scope', '$location', '$http', function MainCtrl($scope, $location, $http){
        $scope.message = 'Hello world';
    }]);

helloWorldControllers.controller('ShowCtrl', ['$scope', '$location',
    '$http', function ShowCtrl($scope, $location, $http){
        $scope.message = "Show The World";
    }]);



helloWorldControllers.controller('CustomerCtrl', ['$scope',
    function CustomerCtrl($scope) {
        $scope.customerName = "Bob's Burgers";
        $scope.customerNumber = "44522";

        /* Method added to the scope*/
        /* Method called by ng-click */
        $scope.changeCustomer = function(){
            /* adding properties */
            $scope.customerName = $scope.cName;
            $scope.customerNumber = $scope.cNumber;
        };
    }]);

helloWorldControllers.controller('AddCustomerCtrl', ['$scope', '$location',
    function AddCustomerCtrl($scope, $location){
        $scope.submit = function(){
            /* change the path */
            $location.path('/addedCustomer/'+$scope.addC.cName+'/'+$scope.addC.cCity);
        };}]);

helloWorldControllers.controller('AddedCustomerCtrl',
    ['$scope', '$routeParams',
        function AddedCustomerCtrl($scope, $routeParams) {

            $scope.customerName = $routeParams.customer;
            $scope.customerCity = $routeParams.city;

        }]);/**
 * Created by marco on 08/01/16.
 */
