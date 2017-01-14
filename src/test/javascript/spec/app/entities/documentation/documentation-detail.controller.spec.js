'use strict';

describe('Controller Tests', function() {

    describe('Documentation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDocumentation, MockTag, MockProject, MockDeliverable;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDocumentation = jasmine.createSpy('MockDocumentation');
            MockTag = jasmine.createSpy('MockTag');
            MockProject = jasmine.createSpy('MockProject');
            MockDeliverable = jasmine.createSpy('MockDeliverable');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Documentation': MockDocumentation,
                'Tag': MockTag,
                'Project': MockProject,
                'Deliverable': MockDeliverable
            };
            createController = function() {
                $injector.get('$controller')("DocumentationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projectMicroServicesApp:documentationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
