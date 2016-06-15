(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('TranslationKeyDialogController', TranslationKeyDialogController);

    TranslationKeyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TranslationKey', 'Translation'];

    function TranslationKeyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TranslationKey, Translation) {
        var vm = this;

        vm.translationKey = entity;
        vm.clear = clear;
        vm.save = save;
        vm.translations = Translation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.translationKey.id !== null) {
                TranslationKey.update(vm.translationKey, onSaveSuccess, onSaveError);
            } else {
                TranslationKey.save(vm.translationKey, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('versushipsterApp:translationKeyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
