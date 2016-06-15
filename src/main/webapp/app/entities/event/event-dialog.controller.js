(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('EventDialogController', EventDialogController);

    EventDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Event', 'Composition', 'TranslationKey'];

    function EventDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Event, Composition, TranslationKey) {
        var vm = this;

        vm.event = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.compositions = Composition.query();
        vm.titles = TranslationKey.query({filter: 'event-is-null'});
        $q.all([vm.event.$promise, vm.titles.$promise]).then(function() {
            if (!vm.event.title || !vm.event.title.id) {
                return $q.reject();
            }
            return TranslationKey.get({id : vm.event.title.id}).$promise;
        }).then(function(title) {
            vm.titles.push(title);
        });
        vm.places = TranslationKey.query({filter: 'event-is-null'});
        $q.all([vm.event.$promise, vm.places.$promise]).then(function() {
            if (!vm.event.place || !vm.event.place.id) {
                return $q.reject();
            }
            return TranslationKey.get({id : vm.event.place.id}).$promise;
        }).then(function(place) {
            vm.places.push(place);
        });
        vm.descriptions = TranslationKey.query({filter: 'event-is-null'});
        $q.all([vm.event.$promise, vm.descriptions.$promise]).then(function() {
            if (!vm.event.description || !vm.event.description.id) {
                return $q.reject();
            }
            return TranslationKey.get({id : vm.event.description.id}).$promise;
        }).then(function(description) {
            vm.descriptions.push(description);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.event.id !== null) {
                Event.update(vm.event, onSaveSuccess, onSaveError);
            } else {
                Event.save(vm.event, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('versushipsterApp:eventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
