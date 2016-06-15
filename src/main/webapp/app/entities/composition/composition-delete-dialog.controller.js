(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('CompositionDeleteController',CompositionDeleteController);

    CompositionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Composition'];

    function CompositionDeleteController($uibModalInstance, entity, Composition) {
        var vm = this;

        vm.composition = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Composition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
