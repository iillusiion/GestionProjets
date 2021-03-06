(function() {
    'use strict';

    angular
        .module('gestionProjetApp')
        .controller('RendezVousDialogController', RendezVousDialogController);

    RendezVousDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RendezVous', 'User'];

    function RendezVousDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RendezVous, User) {
        var vm = this;

        vm.rendezVous = entity;
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
            if (vm.rendezVous.id !== null) {
                RendezVous.update(vm.rendezVous, onSaveSuccess, onSaveError);
            } else {
                RendezVous.save(vm.rendezVous, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestionProjetApp:rendezVousUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
