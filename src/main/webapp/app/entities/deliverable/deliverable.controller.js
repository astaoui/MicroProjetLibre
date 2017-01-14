(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('DeliverableController', DeliverableController);

    DeliverableController.$inject = ['$scope', '$state', 'Deliverable'];

    function DeliverableController ($scope, $state, Deliverable) {
        var vm = this;

        vm.deliverables = [];

        loadAll();

        function loadAll() {
            Deliverable.query(function(result) {
                vm.deliverables = result;
                vm.searchQuery = null;
            });
        }
    }
})();
