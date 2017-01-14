(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('CalenderDeleteController',CalenderDeleteController);

    CalenderDeleteController.$inject = ['$uibModalInstance', 'entity', 'Calender'];

    function CalenderDeleteController($uibModalInstance, entity, Calender) {
        var vm = this;

        vm.calender = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Calender.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
