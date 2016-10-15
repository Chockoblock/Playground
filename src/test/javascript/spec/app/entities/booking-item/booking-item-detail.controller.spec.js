'use strict';

describe('Controller Tests', function() {

    describe('BookingItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBookingItem, MockCustomer, MockResource;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBookingItem = jasmine.createSpy('MockBookingItem');
            MockCustomer = jasmine.createSpy('MockCustomer');
            MockResource = jasmine.createSpy('MockResource');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'BookingItem': MockBookingItem,
                'Customer': MockCustomer,
                'Resource': MockResource
            };
            createController = function() {
                $injector.get('$controller')("BookingItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'test3App:bookingItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
