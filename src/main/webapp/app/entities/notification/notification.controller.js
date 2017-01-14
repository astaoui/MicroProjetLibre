(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('NotificationController', NotificationController);

    NotificationController.$inject = ['$scope', '$state', 'Notification'];

    function NotificationController ($scope, $state, Notification) {
        var vm = this;

        vm.notifications = [];

        loadAll();

        function loadAll() {
            Notification.query(function(result) {
                vm.notifications = result;
                vm.searchQuery = null;
            });
        }
    }
})();
