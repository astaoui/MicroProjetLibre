(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('DeliverableDeleteController',DeliverableDeleteController);

    DeliverableDeleteController.$inject = ['$uibModalInstance', 'entity', 'Deliverable'];

    function DeliverableDeleteController($uibModalInstance, entity, Deliverable) {
        var vm = this;

        vm.deliverable = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Deliverable.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
