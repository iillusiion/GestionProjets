(function() {
    'use strict';
    angular
        .module('gestionProjetApp')
        .factory('Projet', Projet);

    Projet.$inject = ['$resource', 'DateUtils'];

    function Projet ($resource, DateUtils) {
        var resourceUrl =  'api/projets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateDebut = DateUtils.convertLocalDateFromServer(data.dateDebut);
                        data.dateFin = DateUtils.convertLocalDateFromServer(data.dateFin);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateDebut = DateUtils.convertLocalDateToServer(copy.dateDebut);
                    copy.dateFin = DateUtils.convertLocalDateToServer(copy.dateFin);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateDebut = DateUtils.convertLocalDateToServer(copy.dateDebut);
                    copy.dateFin = DateUtils.convertLocalDateToServer(copy.dateFin);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
