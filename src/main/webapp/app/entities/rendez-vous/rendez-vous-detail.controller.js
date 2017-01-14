(function() {
    'use strict';

    angular
        .module('gestionProjetApp')
        .controller('RendezVousDetailController', RendezVousDetailController);

    RendezVousDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RendezVous', 'User'];

    function RendezVousDetailController($scope, $rootScope, $stateParams, previousState, entity, RendezVous, User) {
        var vm = this;

        vm.rendezVous = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestionProjetApp:rendezVousUpdate', function(event, result) {
            vm.rendezVous = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
