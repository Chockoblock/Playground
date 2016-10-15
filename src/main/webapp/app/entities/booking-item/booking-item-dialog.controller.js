(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('BookingItemDialogController', BookingItemDialogController);

    BookingItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BookingItem', 'Customer', 'Resource'];

    function BookingItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BookingItem, Customer, Resource) {
        var vm = this;

        vm.bookingItem = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.customers = Customer.query();
        vm.resources = Resource.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bookingItem.id !== null) {
                BookingItem.update(vm.bookingItem, onSaveSuccess, onSaveError);
            } else {
                BookingItem.save(vm.bookingItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('test3App:bookingItemUpdate', result);
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
