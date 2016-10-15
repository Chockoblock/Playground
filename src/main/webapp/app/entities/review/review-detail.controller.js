(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('ReviewDetailController', ReviewDetailController);

    ReviewDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Review', 'Photo', 'Merchant', 'Customer'];

    function ReviewDetailController($scope, $rootScope, $stateParams, previousState, entity, Review, Photo, Merchant, Customer) {
        var vm = this;

        vm.review = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('test3App:reviewUpdate', function(event, result) {
            vm.review = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
