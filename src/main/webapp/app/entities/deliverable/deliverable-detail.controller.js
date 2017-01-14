(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('DeliverableDetailController', DeliverableDetailController);

    DeliverableDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Deliverable', 'Documentation', 'Project'];

    function DeliverableDetailController($scope, $rootScope, $stateParams, previousState, entity, Deliverable, Documentation, Project) {
        var vm = this;

        vm.deliverable = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('projectMicroServicesApp:deliverableUpdate', function(event, result) {
            vm.deliverable = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
