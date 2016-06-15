(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('ArticleDetailController', ArticleDetailController);

    ArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Article', 'TranslationKey'];

    function ArticleDetailController($scope, $rootScope, $stateParams, entity, Article, TranslationKey) {
        var vm = this;

        vm.article = entity;

        var unsubscribe = $rootScope.$on('versushipsterApp:articleUpdate', function(event, result) {
            vm.article = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
