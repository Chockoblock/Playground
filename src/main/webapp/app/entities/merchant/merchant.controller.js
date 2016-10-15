(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('MerchantController', MerchantController);

    MerchantController.$inject = ['$scope', '$state', 'Merchant', 'MerchantSearch'];

    function MerchantController ($scope, $state, Merchant, MerchantSearch) {
        var vm = this;
        
        vm.merchants = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Merchant.query(function(result) {
                vm.merchants = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MerchantSearch.query({query: vm.searchQuery}, function(result) {
                vm.merchants = result;
            });
        }    }
})();
