(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('MeetingDetailController', MeetingDetailController);

    MeetingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Meeting', 'Calender'];

    function MeetingDetailController($scope, $rootScope, $stateParams, previousState, entity, Meeting, Calender) {
        var vm = this;

        vm.meeting = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('projectMicroServicesApp:meetingUpdate', function(event, result) {
            vm.meeting = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
