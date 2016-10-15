(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('MerchantDetailController', MerchantDetailController);

    MerchantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Merchant', 'Review', 'Service'];

    function MerchantDetailController($scope, $rootScope, $stateParams, previousState, entity, Merchant, Review, Service) {
        var vm = this;

        vm.merchant = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('test3App:merchantUpdate', function(event, result) {
            vm.merchant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
