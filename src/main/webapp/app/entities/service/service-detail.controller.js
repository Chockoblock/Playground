(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('ServiceDetailController', ServiceDetailController);

    ServiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Service', 'Merchant', 'Holiday', 'Offer', 'Resource'];

    function ServiceDetailController($scope, $rootScope, $stateParams, previousState, entity, Service, Merchant, Holiday, Offer, Resource) {
        var vm = this;

        vm.service = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('test3App:serviceUpdate', function(event, result) {
            vm.service = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
