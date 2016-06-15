(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('TranslationDialogController', TranslationDialogController);

    TranslationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Translation', 'TranslationKey'];

    function TranslationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Translation, TranslationKey) {
        var vm = this;

        vm.translation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.translationkeys = TranslationKey.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.translation.id !== null) {
                Translation.update(vm.translation, onSaveSuccess, onSaveError);
            } else {
                Translation.save(vm.translation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('versushipsterApp:translationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
