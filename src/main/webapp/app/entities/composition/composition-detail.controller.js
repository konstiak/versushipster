(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('CompositionDetailController', CompositionDetailController);

    CompositionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Composition', 'Video', 'TranslationKey', 'Composer', 'Program', 'Event'];

    function CompositionDetailController($scope, $rootScope, $stateParams, entity, Composition, Video, TranslationKey, Composer, Program, Event) {
        var vm = this;

        vm.composition = entity;

        var unsubscribe = $rootScope.$on('versushipsterApp:compositionUpdate', function(event, result) {
            vm.composition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
