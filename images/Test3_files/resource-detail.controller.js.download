(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('ResourceDetailController', ResourceDetailController);

    ResourceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Resource', 'Service', 'Employee', 'BookingItem'];

    function ResourceDetailController($scope, $rootScope, $stateParams, previousState, entity, Resource, Service, Employee, BookingItem) {
        var vm = this;

        vm.resource = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('test3App:resourceUpdate', function(event, result) {
            vm.resource = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
