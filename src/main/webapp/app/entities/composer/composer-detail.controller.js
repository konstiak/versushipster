(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('ComposerDetailController', ComposerDetailController);

    ComposerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Composer', 'Composition', 'TranslationKey'];

    function ComposerDetailController($scope, $rootScope, $stateParams, entity, Composer, Composition, TranslationKey) {
        var vm = this;

        vm.composer = entity;

        var unsubscribe = $rootScope.$on('versushipsterApp:composerUpdate', function(event, result) {
            vm.composer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
