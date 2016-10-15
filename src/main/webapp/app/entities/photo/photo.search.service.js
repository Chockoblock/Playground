(function() {
    'use strict';

    angular
        .module('test3App')
        .factory('PhotoSearch', PhotoSearch);

    PhotoSearch.$inject = ['$resource'];

    function PhotoSearch($resource) {
        var resourceUrl =  'api/_search/photos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
