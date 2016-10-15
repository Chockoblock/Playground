(function() {
    'use strict';

    angular
        .module('test3App')
        .controller('PhotoController', PhotoController);

    PhotoController.$inject = ['$scope', '$state', 'Photo', 'PhotoSearch'];

    function PhotoController ($scope, $state, Photo, PhotoSearch) {
        var vm = this;
        
        vm.photos = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Photo.query(function(result) {
                vm.photos = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PhotoSearch.query({query: vm.searchQuery}, function(result) {
                vm.photos = result;
            });
        }    }
})();
