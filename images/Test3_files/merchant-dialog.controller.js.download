(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('MerchantDialogController', MerchantDialogController);

    MerchantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Merchant', 'Review', 'Service'];

    function MerchantDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Merchant, Review, Service) {
        var vm = this;

        vm.merchant = entity;
        vm.clear = clear;
        vm.save = save;
        vm.reviews = Review.query();
        vm.services = Service.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.merchant.id !== null) {
                Merchant.update(vm.merchant, onSaveSuccess, onSaveError);
            } else {
                Merchant.save(vm.merchant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('test3App:merchantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
