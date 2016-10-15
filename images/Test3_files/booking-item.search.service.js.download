(function() {
    'use strict';

    angular
        .module('test3App')
        .factory('BookingItemSearch', BookingItemSearch);

    BookingItemSearch.$inject = ['$resource'];

    function BookingItemSearch($resource) {
        var resourceUrl =  'api/_search/booking-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
