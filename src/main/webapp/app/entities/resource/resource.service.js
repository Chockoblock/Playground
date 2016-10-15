(function() {
    'use strict';
    angular
        .module('test3App')
        .factory('Resource', Resource);

    Resource.$inject = ['$resource'];

    function Resource ($resource) {
        var resourceUrl =  'api/resources/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
