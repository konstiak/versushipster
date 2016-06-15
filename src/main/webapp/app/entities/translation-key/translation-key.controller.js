(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('TranslationKeyController', TranslationKeyController);

    TranslationKeyController.$inject = ['$scope', '$state', 'TranslationKey'];

    function TranslationKeyController ($scope, $state, TranslationKey) {
        var vm = this;
        
        vm.translationKeys = [];

        loadAll();

        function loadAll() {
            TranslationKey.query(function(result) {
                vm.translationKeys = result;
            });
        }
    }
})();
