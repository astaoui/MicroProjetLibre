(function() {
    'use strict';
    angular
        .module('projectMicroServicesApp')
        .factory('Documentation', Documentation);

    Documentation.$inject = ['$resource'];

    function Documentation ($resource) {
        var resourceUrl =  'api/documentations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
