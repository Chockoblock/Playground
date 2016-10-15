(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('CustomerDialogController', CustomerDialogController);

    CustomerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Customer', 'Photo', 'Review', 'BookingItem'];

    function CustomerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Customer, Photo, Review, BookingItem) {
        var vm = this;

        vm.customer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.photos = Photo.query();
        vm.reviews = Review.query();
        vm.bookingitems = BookingItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.customer.id !== null) {
                Customer.update(vm.customer, onSaveSuccess, onSaveError);
            } else {
                Customer.save(vm.customer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('test3App:customerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
