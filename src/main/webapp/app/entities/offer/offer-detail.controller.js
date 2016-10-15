(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('OfferDetailController', OfferDetailController);

    OfferDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Offer', 'Service'];

    function OfferDetailController($scope, $rootScope, $stateParams, previousState, entity, Offer, Service) {
        var vm = this;

        vm.offer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('test3App:offerUpdate', function(event, result) {
            vm.offer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
