(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Employee', 'Resource', 'Holiday'];

    function EmployeeDetailController($scope, $rootScope, $stateParams, previousState, entity, Employee, Resource, Holiday) {
        var vm = this;

        vm.employee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('test3App:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
