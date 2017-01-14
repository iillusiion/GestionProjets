(function() {
    'use strict';

    angular
        .module('gestionProjetApp')
        .controller('DocumentDialogController', DocumentDialogController);

    DocumentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Document', 'Projet'];

    function DocumentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Document, Projet) {
        var vm = this;

        vm.document = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.projets = Projet.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.document.id !== null) {
                Document.update(vm.document, onSaveSuccess, onSaveError);
            } else {
                Document.save(vm.document, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestionProjetApp:documentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateDeposition = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
