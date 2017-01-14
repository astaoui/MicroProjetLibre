'use strict';

describe('Controller Tests', function() {

    describe('Calender Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCalender, MockProject, MockMeeting;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCalender = jasmine.createSpy('MockCalender');
            MockProject = jasmine.createSpy('MockProject');
            MockMeeting = jasmine.createSpy('MockMeeting');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Calender': MockCalender,
                'Project': MockProject,
                'Meeting': MockMeeting
            };
            createController = function() {
                $injector.get('$controller')("CalenderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projectMicroServicesApp:calenderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
