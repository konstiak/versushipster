(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('ProgramDetailController', ProgramDetailController);

    ProgramDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Program', 'Composition', 'TranslationKey'];

    function ProgramDetailController($scope, $rootScope, $stateParams, entity, Program, Composition, TranslationKey) {
        var vm = this;

        vm.program = entity;

        var unsubscribe = $rootScope.$on('versushipsterApp:programUpdate', function(event, result) {
            vm.program = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
