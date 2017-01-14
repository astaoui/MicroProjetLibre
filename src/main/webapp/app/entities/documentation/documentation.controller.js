(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('DocumentationController', DocumentationController);

    DocumentationController.$inject = ['$scope', '$state', 'DataUtils', 'Documentation'];

    function DocumentationController ($scope, $state, DataUtils, Documentation) {
        var vm = this;

        vm.documentations = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Documentation.query(function(result) {
                vm.documentations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
