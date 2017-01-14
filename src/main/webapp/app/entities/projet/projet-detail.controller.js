(function() {
    'use strict';

    angular
        .module('gestionProjetApp')
        .controller('ProjetDetailController', ProjetDetailController);

    ProjetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Projet', 'User'];

    function ProjetDetailController($scope, $rootScope, $stateParams, previousState, entity, Projet, User) {
        var vm = this;

        vm.projet = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestionProjetApp:projetUpdate', function(event, result) {
            vm.projet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
