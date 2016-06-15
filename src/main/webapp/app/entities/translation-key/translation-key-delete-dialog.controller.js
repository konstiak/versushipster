(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('TranslationKeyDeleteController',TranslationKeyDeleteController);

    TranslationKeyDeleteController.$inject = ['$uibModalInstance', 'entity', 'TranslationKey'];

    function TranslationKeyDeleteController($uibModalInstance, entity, TranslationKey) {
        var vm = this;

        vm.translationKey = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TranslationKey.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
