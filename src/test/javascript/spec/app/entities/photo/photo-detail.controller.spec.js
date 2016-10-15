'use strict';

describe('Controller Tests', function() {

    describe('Photo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPhoto, MockReview, MockMerchant, MockCustomer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPhoto = jasmine.createSpy('MockPhoto');
            MockReview = jasmine.createSpy('MockReview');
            MockMerchant = jasmine.createSpy('MockMerchant');
            MockCustomer = jasmine.createSpy('MockCustomer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Photo': MockPhoto,
                'Review': MockReview,
                'Merchant': MockMerchant,
                'Customer': MockCustomer
            };
            createController = function() {
                $injector.get('$controller')("PhotoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'test3App:photoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});