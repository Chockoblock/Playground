(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('ServiceController', ServiceController);

    ServiceController.$inject = ['$scope', '$state', 'Service', 'ServiceSearch'];

    function ServiceController ($scope, $state, Service, ServiceSearch) {
        var vm = this;
        
        vm.services = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Service.query(function(result) {
                vm.services = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ServiceSearch.query({query: vm.searchQuery}, function(result) {
                vm.services = result;
            });
        }    }
})();
