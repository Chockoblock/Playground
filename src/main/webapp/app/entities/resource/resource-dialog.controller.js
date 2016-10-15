(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('ResourceDialogController', ResourceDialogController);

    ResourceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Resource', 'Service', 'Employee', 'BookingItem'];

    function ResourceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Resource, Service, Employee, BookingItem) {
        var vm = this;

        vm.resource = entity;
        vm.clear = clear;
        vm.save = save;
        vm.services = Service.query();
        vm.employees = Employee.query();
        vm.bookingitems = BookingItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.resource.id !== null) {
                Resource.update(vm.resource, onSaveSuccess, onSaveError);
            } else {
                Resource.save(vm.resource, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('test3App:resourceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
