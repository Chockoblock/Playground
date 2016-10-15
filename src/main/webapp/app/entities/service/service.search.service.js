(function() {
    'use strict';

    angular
        .module('test3App')
        .factory('ServiceSearch', ServiceSearch);

    ServiceSearch.$inject = ['$resource'];

    function ServiceSearch($resource) {
        var resourceUrl =  'api/_search/services/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
