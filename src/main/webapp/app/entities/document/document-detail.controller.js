(function() {
    'use strict';

    angular
        .module('gestionProjetApp')
        .controller('DocumentDetailController', DocumentDetailController);

    DocumentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Document', 'Projet'];

    function DocumentDetailController($scope, $rootScope, $stateParams, previousState, entity, Document, Projet) {
        var vm = this;

        vm.document = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestionProjetApp:documentUpdate', function(event, result) {
            vm.document = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
