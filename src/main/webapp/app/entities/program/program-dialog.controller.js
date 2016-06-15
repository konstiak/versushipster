(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('ProgramDialogController', ProgramDialogController);

    ProgramDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Program', 'Composition', 'TranslationKey'];

    function ProgramDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Program, Composition, TranslationKey) {
        var vm = this;

        vm.program = entity;
        vm.clear = clear;
        vm.save = save;
        vm.compositions = Composition.query();
        vm.names = TranslationKey.query({filter: 'program-is-null'});
        $q.all([vm.program.$promise, vm.names.$promise]).then(function() {
            if (!vm.program.name || !vm.program.name.id) {
                return $q.reject();
            }
            return TranslationKey.get({id : vm.program.name.id}).$promise;
        }).then(function(name) {
            vm.names.push(name);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.program.id !== null) {
                Program.update(vm.program, onSaveSuccess, onSaveError);
            } else {
                Program.save(vm.program, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('versushipsterApp:programUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
