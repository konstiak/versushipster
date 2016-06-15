(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('ComposerDialogController', ComposerDialogController);

    ComposerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Composer', 'Composition', 'TranslationKey'];

    function ComposerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Composer, Composition, TranslationKey) {
        var vm = this;

        vm.composer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.compositions = Composition.query();
        vm.descriptions = TranslationKey.query({filter: 'composer-is-null'});
        $q.all([vm.composer.$promise, vm.descriptions.$promise]).then(function() {
            if (!vm.composer.description || !vm.composer.description.id) {
                return $q.reject();
            }
            return TranslationKey.get({id : vm.composer.description.id}).$promise;
        }).then(function(description) {
            vm.descriptions.push(description);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.composer.id !== null) {
                Composer.update(vm.composer, onSaveSuccess, onSaveError);
            } else {
                Composer.save(vm.composer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('versushipsterApp:composerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
