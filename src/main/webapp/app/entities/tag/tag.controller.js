(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('TagController', TagController);

    TagController.$inject = ['$scope', '$state', 'Tag'];

    function TagController ($scope, $state, Tag) {
        var vm = this;

        vm.tags = [];

        loadAll();

        function loadAll() {
            Tag.query(function(result) {
                vm.tags = result;
                vm.searchQuery = null;
            });
        }
    }
})();
