(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('HolidayController', HolidayController);

    HolidayController.$inject = ['$scope', '$state', 'Holiday', 'HolidaySearch'];

    function HolidayController ($scope, $state, Holiday, HolidaySearch) {
        var vm = this;
        
        vm.holidays = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Holiday.query(function(result) {
                vm.holidays = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            HolidaySearch.query({query: vm.searchQuery}, function(result) {
                vm.holidays = result;
            });
        }    }
})();
