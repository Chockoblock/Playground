(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('ResourceController', ResourceController);

    ResourceController.$inject = ['$scope', '$state', 'Resource', 'ResourceSearch'];

    function ResourceController ($scope, $state, Resource, ResourceSearch) {
        var vm = this;
        
        vm.resources = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Resource.query(function(result) {
                vm.resources = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ResourceSearch.query({query: vm.searchQuery}, function(result) {
                vm.resources = result;
            });
        }    }
})();
