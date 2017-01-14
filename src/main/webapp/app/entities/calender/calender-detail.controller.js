(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('CalenderDetailController', CalenderDetailController);

    CalenderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Calender', 'Project', 'Meeting'];

    function CalenderDetailController($scope, $rootScope, $stateParams, previousState, entity, Calender, Project, Meeting) {
        var vm = this;

        vm.calender = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('projectMicroServicesApp:calenderUpdate', function(event, result) {
            vm.calender = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
