(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('TranslationKeyDetailController', TranslationKeyDetailController);

    TranslationKeyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'TranslationKey', 'Translation'];

    function TranslationKeyDetailController($scope, $rootScope, $stateParams, entity, TranslationKey, Translation) {
        var vm = this;

        vm.translationKey = entity;

        var unsubscribe = $rootScope.$on('versushipsterApp:translationKeyUpdate', function(event, result) {
            vm.translationKey = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
