(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('BookingItemDeleteController',BookingItemDeleteController);

    BookingItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'BookingItem'];

    function BookingItemDeleteController($uibModalInstance, entity, BookingItem) {
        var vm = this;

        vm.bookingItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BookingItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
