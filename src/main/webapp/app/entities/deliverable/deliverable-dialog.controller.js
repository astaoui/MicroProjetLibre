(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('DeliverableDialogController', DeliverableDialogController);

    DeliverableDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Deliverable', 'Documentation', 'Project'];

    function DeliverableDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Deliverable, Documentation, Project) {
        var vm = this;

        vm.deliverable = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.documentations = Documentation.query();
        vm.projects = Project.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.deliverable.id !== null) {
                Deliverable.update(vm.deliverable, onSaveSuccess, onSaveError);
            } else {
                Deliverable.save(vm.deliverable, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectMicroServicesApp:deliverableUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateBegining = false;
        vm.datePickerOpenStatus.dateEnding = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
