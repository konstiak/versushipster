(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('TranslationDetailController', TranslationDetailController);

    TranslationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Translation', 'TranslationKey'];

    function TranslationDetailController($scope, $rootScope, $stateParams, entity, Translation, TranslationKey) {
        var vm = this;

        vm.translation = entity;

        var unsubscribe = $rootScope.$on('versushipsterApp:translationUpdate', function(event, result) {
            vm.translation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
