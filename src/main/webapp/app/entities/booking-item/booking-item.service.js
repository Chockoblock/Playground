(function() {
    'use strict';
    angular
        .module('test3App')
        .factory('BookingItem', BookingItem);

    BookingItem.$inject = ['$resource', 'DateUtils'];

    function BookingItem ($resource, DateUtils) {
        var resourceUrl =  'api/booking-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startTime = DateUtils.convertDateTimeFromServer(data.startTime);
                        data.endTime = DateUtils.convertDateTimeFromServer(data.endTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
