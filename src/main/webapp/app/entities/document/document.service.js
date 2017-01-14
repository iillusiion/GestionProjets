(function() {
    'use strict';
    angular
        .module('gestionProjetApp')
        .factory('Document', Document);

    Document.$inject = ['$resource', 'DateUtils'];

    function Document ($resource, DateUtils) {
        var resourceUrl =  'api/documents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateDeposition = DateUtils.convertLocalDateFromServer(data.dateDeposition);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateDeposition = DateUtils.convertLocalDateToServer(copy.dateDeposition);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateDeposition = DateUtils.convertLocalDateToServer(copy.dateDeposition);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
