(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('PhotoDetailController', PhotoDetailController);

    PhotoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Photo', 'Review', 'Merchant', 'Customer'];

    function PhotoDetailController($scope, $rootScope, $stateParams, previousState, entity, Photo, Review, Merchant, Customer) {
        var vm = this;

        vm.photo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('test3App:photoUpdate', function(event, result) {
            vm.photo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
