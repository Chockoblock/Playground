(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('CustomerDetailController', CustomerDetailController);

    CustomerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Customer', 'Photo', 'Review', 'BookingItem'];

    function CustomerDetailController($scope, $rootScope, $stateParams, previousState, entity, Customer, Photo, Review, BookingItem) {
        var vm = this;

        vm.customer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('test3App:customerUpdate', function(event, result) {
            vm.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
