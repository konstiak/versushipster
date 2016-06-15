(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('ArticleDialogController', ArticleDialogController);

    ArticleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Article', 'TranslationKey'];

    function ArticleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Article, TranslationKey) {
        var vm = this;

        vm.article = entity;
        vm.clear = clear;
        vm.save = save;
        vm.titles = TranslationKey.query({filter: 'article-is-null'});
        $q.all([vm.article.$promise, vm.titles.$promise]).then(function() {
            if (!vm.article.title || !vm.article.title.id) {
                return $q.reject();
            }
            return TranslationKey.get({id : vm.article.title.id}).$promise;
        }).then(function(title) {
            vm.titles.push(title);
        });
        vm.contents = TranslationKey.query({filter: 'article-is-null'});
        $q.all([vm.article.$promise, vm.contents.$promise]).then(function() {
            if (!vm.article.content || !vm.article.content.id) {
                return $q.reject();
            }
            return TranslationKey.get({id : vm.article.content.id}).$promise;
        }).then(function(content) {
            vm.contents.push(content);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.article.id !== null) {
                Article.update(vm.article, onSaveSuccess, onSaveError);
            } else {
                Article.save(vm.article, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('versushipsterApp:articleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
