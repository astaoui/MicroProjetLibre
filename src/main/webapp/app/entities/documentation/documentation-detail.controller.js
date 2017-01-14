(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('DocumentationDetailController', DocumentationDetailController);

    DocumentationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Documentation', 'Tag', 'Project', 'Deliverable'];

    function DocumentationDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Documentation, Tag, Project, Deliverable) {
        var vm = this;

        vm.documentation = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('projectMicroServicesApp:documentationUpdate', function(event, result) {
            vm.documentation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
