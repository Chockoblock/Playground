(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('OfferDialogController', OfferDialogController);

    OfferDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Offer', 'Service'];

    function OfferDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Offer, Service) {
        var vm = this;

        vm.offer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.services = Service.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.offer.id !== null) {
                Offer.update(vm.offer, onSaveSuccess, onSaveError);
            } else {
                Offer.save(vm.offer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('test3App:offerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
