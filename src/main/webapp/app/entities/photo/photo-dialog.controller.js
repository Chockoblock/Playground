(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('PhotoDialogController', PhotoDialogController);

    PhotoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Photo', 'Review', 'Merchant', 'Customer'];

    function PhotoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Photo, Review, Merchant, Customer) {
        var vm = this;

        vm.photo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.reviews = Review.query();
        vm.merchants = Merchant.query();
        vm.customers = Customer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.photo.id !== null) {
                Photo.update(vm.photo, onSaveSuccess, onSaveError);
            } else {
                Photo.save(vm.photo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('test3App:photoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
