(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('HolidayDetailController', HolidayDetailController);

    HolidayDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Holiday', 'Service', 'Employee'];

    function HolidayDetailController($scope, $rootScope, $stateParams, previousState, entity, Holiday, Service, Employee) {
        var vm = this;

        vm.holiday = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('test3App:holidayUpdate', function(event, result) {
            vm.holiday = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
