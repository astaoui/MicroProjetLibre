(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('MeetingDialogController', MeetingDialogController);

    MeetingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Meeting', 'Calender'];

    function MeetingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Meeting, Calender) {
        var vm = this;

        vm.meeting = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.calenders = Calender.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.meeting.id !== null) {
                Meeting.update(vm.meeting, onSaveSuccess, onSaveError);
            } else {
                Meeting.save(vm.meeting, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectMicroServicesApp:meetingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
