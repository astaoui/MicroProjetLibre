(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('TagDetailController', TagDetailController);

    TagDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tag', 'Project', 'Documentation'];

    function TagDetailController($scope, $rootScope, $stateParams, previousState, entity, Tag, Project, Documentation) {
        var vm = this;

        vm.tag = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('projectMicroServicesApp:tagUpdate', function(event, result) {
            vm.tag = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
