(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('EventDetailController', EventDetailController);

    EventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Event', 'Composition', 'TranslationKey'];

    function EventDetailController($scope, $rootScope, $stateParams, entity, Event, Composition, TranslationKey) {
        var vm = this;

        vm.event = entity;

        var unsubscribe = $rootScope.$on('versushipsterApp:eventUpdate', function(event, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
