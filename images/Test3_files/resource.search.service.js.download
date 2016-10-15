(function() {
    'use strict';

    angular
        .module('test3App')
        .factory('ResourceSearch', ResourceSearch);

    ResourceSearch.$inject = ['$resource'];

    function ResourceSearch($resource) {
        var resourceUrl =  'api/_search/resources/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
