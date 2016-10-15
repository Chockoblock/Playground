(function() {
    'use strict';

    angular
        .module('test3App')
        .factory('HolidaySearch', HolidaySearch);

    HolidaySearch.$inject = ['$resource'];

    function HolidaySearch($resource) {
        var resourceUrl =  'api/_search/holidays/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
