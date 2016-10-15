(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('BookingItemDetailController', BookingItemDetailController);

    BookingItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BookingItem', 'Customer', 'Resource'];

    function BookingItemDetailController($scope, $rootScope, $stateParams, previousState, entity, BookingItem, Customer, Resource) {
        var vm = this;

        vm.bookingItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('test3App:bookingItemUpdate', function(event, result) {
            vm.bookingItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
