(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('ServiceDialogController', ServiceDialogController);

    ServiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Service', 'Merchant', 'Holiday', 'Offer', 'Resource'];

    function ServiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Service, Merchant, Holiday, Offer, Resource) {
        var vm = this;

        vm.service = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.merchants = Merchant.query();
        vm.holidays = Holiday.query();
        vm.offers = Offer.query();
        vm.resources = Resource.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.service.id !== null) {
                Service.update(vm.service, onSaveSuccess, onSaveError);
            } else {
                Service.save(vm.service, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('test3App:serviceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startTime = false;
        vm.datePickerOpenStatus.endTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
