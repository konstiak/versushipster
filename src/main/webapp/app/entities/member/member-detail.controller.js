(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('MemberDetailController', MemberDetailController);

    MemberDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Member', 'TranslationKey'];

    function MemberDetailController($scope, $rootScope, $stateParams, entity, Member, TranslationKey) {
        var vm = this;

        vm.member = entity;

        var unsubscribe = $rootScope.$on('versushipsterApp:memberUpdate', function(event, result) {
            vm.member = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
