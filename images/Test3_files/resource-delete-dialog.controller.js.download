(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('ResourceDeleteController',ResourceDeleteController);

    ResourceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Resource'];

    function ResourceDeleteController($uibModalInstance, entity, Resource) {
        var vm = this;

        vm.resource = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Resource.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
