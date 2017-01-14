(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('MeetingController', MeetingController);

    MeetingController.$inject = ['$scope', '$state', 'Meeting'];

    function MeetingController ($scope, $state, Meeting) {
        var vm = this;

        vm.meetings = [];

        loadAll();

        function loadAll() {
            Meeting.query(function(result) {
                vm.meetings = result;
                vm.searchQuery = null;
            });
        }
    }
})();
