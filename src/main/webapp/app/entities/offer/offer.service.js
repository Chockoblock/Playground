(function() {
    'use strict';
    angular
        .module('test3App')
        .factory('Offer', Offer);

    Offer.$inject = ['$resource', 'DateUtils'];

    function Offer ($resource, DateUtils) {
        var resourceUrl =  'api/offers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
