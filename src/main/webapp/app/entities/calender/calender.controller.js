(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('CalenderController', CalenderController);

    CalenderController.$inject = ['$scope', '$state', 'Calender'];

    function CalenderController ($scope, $state, Calender) {
        var vm = this;

        vm.calenders = [];

        loadAll();

        function loadAll() {
            Calender.query(function(result) {
                vm.calenders = result;
                vm.searchQuery = null;
            });
        }
    }
})();
