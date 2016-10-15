'use strict';

describe('Controller Tests', function() {

    describe('Service Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockService, MockMerchant, MockHoliday, MockOffer, MockResource;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockService = jasmine.createSpy('MockService');
            MockMerchant = jasmine.createSpy('MockMerchant');
            MockHoliday = jasmine.createSpy('MockHoliday');
            MockOffer = jasmine.createSpy('MockOffer');
            MockResource = jasmine.createSpy('MockResource');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Service': MockService,
                'Merchant': MockMerchant,
                'Holiday': MockHoliday,
                'Offer': MockOffer,
                'Resource': MockResource
            };
            createController = function() {
                $injector.get('$controller')("ServiceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'test3App:serviceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
