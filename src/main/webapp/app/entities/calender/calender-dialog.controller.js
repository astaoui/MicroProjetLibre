(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('CalenderDialogController', CalenderDialogController);

    CalenderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Calender', 'Project', 'Meeting'];

    function CalenderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Calender, Project, Meeting) {
        var vm = this;

        vm.calender = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.projects = Project.query({filter: 'calender-is-null'});
        $q.all([vm.calender.$promise, vm.projects.$promise]).then(function() {
            if (!vm.calender.project || !vm.calender.project.id) {
                return $q.reject();
            }
            return Project.get({id : vm.calender.project.id}).$promise;
        }).then(function(project) {
            vm.projects.push(project);
        });
        vm.meetings = Meeting.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.calender.id !== null) {
                Calender.update(vm.calender, onSaveSuccess, onSaveError);
            } else {
                Calender.save(vm.calender, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectMicroServicesApp:calenderUpdate', result);
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
