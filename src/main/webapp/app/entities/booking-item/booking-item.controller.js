(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('BookingItemController', BookingItemController);

    BookingItemController.$inject = ['$scope', '$state', 'BookingItem', 'BookingItemSearch'];

    function BookingItemController ($scope, $state, BookingItem, BookingItemSearch) {
        var vm = this;
        
        vm.bookingItems = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            BookingItem.query(function(result) {
                vm.bookingItems = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BookingItemSearch.query({query: vm.searchQuery}, function(result) {
                vm.bookingItems = result;
            });
        }    }
})();
