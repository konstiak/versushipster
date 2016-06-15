(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('VideoDetailController', VideoDetailController);

    VideoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Video', 'Composition'];

    function VideoDetailController($scope, $rootScope, $stateParams, entity, Video, Composition) {
        var vm = this;

        vm.video = entity;

        var unsubscribe = $rootScope.$on('versushipsterApp:videoUpdate', function(event, result) {
            vm.video = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
