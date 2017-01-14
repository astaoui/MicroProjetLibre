(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('DocumentationDeleteController',DocumentationDeleteController);

    DocumentationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Documentation'];

    function DocumentationDeleteController($uibModalInstance, entity, Documentation) {
        var vm = this;

        vm.documentation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Documentation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
