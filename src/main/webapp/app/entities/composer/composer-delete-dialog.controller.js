(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('ComposerDeleteController',ComposerDeleteController);

    ComposerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Composer'];

    function ComposerDeleteController($uibModalInstance, entity, Composer) {
        var vm = this;

        vm.composer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Composer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
