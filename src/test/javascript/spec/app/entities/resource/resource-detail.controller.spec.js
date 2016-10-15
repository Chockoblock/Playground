'use strict';

describe('Controller Tests', function() {

    describe('Resource Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockResource, MockService, MockEmployee, MockBookingItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockResource = jasmine.createSpy('MockResource');
            MockService = jasmine.createSpy('MockService');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockBookingItem = jasmine.createSpy('MockBookingItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Resource': MockResource,
                'Service': MockService,
                'Employee': MockEmployee,
                'BookingItem': MockBookingItem
            };
            createController = function() {
                $injector.get('$controller')("ResourceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'test3App:resourceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
