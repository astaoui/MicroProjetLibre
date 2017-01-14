(function() {
    'use strict';
    angular
        .module('projectMicroServicesApp')
        .factory('Meeting', Meeting);

    Meeting.$inject = ['$resource', 'DateUtils'];

    function Meeting ($resource, DateUtils) {
        var resourceUrl =  'api/meetings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
