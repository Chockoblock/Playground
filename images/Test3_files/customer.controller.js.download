(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('CustomerController', CustomerController);

    CustomerController.$inject = ['$scope', '$state', 'Customer', 'CustomerSearch'];

    function CustomerController ($scope, $state, Customer, CustomerSearch) {
        var vm = this;
        
        vm.customers = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Customer.query(function(result) {
                vm.customers = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CustomerSearch.query({query: vm.searchQuery}, function(result) {
                vm.customers = result;
            });
        }    }
})();
