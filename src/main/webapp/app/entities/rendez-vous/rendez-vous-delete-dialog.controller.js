(function() {
    'use strict';

    angular
        .module('gestionProjetApp')
        .controller('RendezVousDeleteController',RendezVousDeleteController);

    RendezVousDeleteController.$inject = ['$uibModalInstance', 'entity', 'RendezVous'];

    function RendezVousDeleteController($uibModalInstance, entity, RendezVous) {
        var vm = this;

        vm.rendezVous = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RendezVous.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
