(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Project', 'Documentation', 'Deliverable', 'Tag', 'Calender', 'User'];

    function ProjectDetailController($scope, $rootScope, $stateParams, previousState, entity, Project, Documentation, Deliverable, Tag, Calender, User) {
        var vm = this;

        vm.project = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('projectMicroServicesApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
