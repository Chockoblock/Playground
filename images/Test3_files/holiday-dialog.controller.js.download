(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('HolidayDialogController', HolidayDialogController);

    HolidayDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Holiday', 'Service', 'Employee'];

    function HolidayDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Holiday, Service, Employee) {
        var vm = this;

        vm.holiday = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.services = Service.query();
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.holiday.id !== null) {
                Holiday.update(vm.holiday, onSaveSuccess, onSaveError);
            } else {
                Holiday.save(vm.holiday, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('test3App:holidayUpdate', result);
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
