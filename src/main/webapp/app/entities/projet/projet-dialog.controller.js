(function() {
    'use strict';

    angular
        .module('gestionProjetApp')
        .controller('ProjetDialogController', ProjetDialogController);

    ProjetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Projet', 'User'];

    function ProjetDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Projet, User) {
        var vm = this;

        vm.projet = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projet.id !== null) {
                Projet.update(vm.projet, onSaveSuccess, onSaveError);
            } else {
                Projet.save(vm.projet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestionProjetApp:projetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateDebut = false;
        vm.datePickerOpenStatus.dateFin = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
