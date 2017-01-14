(function() {
    'use strict';
    angular
        .module('projectMicroServicesApp')
        .factory('Project', Project);

    Project.$inject = ['$resource', 'DateUtils'];

    function Project ($resource, DateUtils) {
        var resourceUrl =  'api/projects/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateBegining = DateUtils.convertLocalDateFromServer(data.dateBegining);
                        data.dateEnding = DateUtils.convertLocalDateFromServer(data.dateEnding);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateBegining = DateUtils.convertLocalDateToServer(copy.dateBegining);
                    copy.dateEnding = DateUtils.convertLocalDateToServer(copy.dateEnding);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateBegining = DateUtils.convertLocalDateToServer(copy.dateBegining);
                    copy.dateEnding = DateUtils.convertLocalDateToServer(copy.dateEnding);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
