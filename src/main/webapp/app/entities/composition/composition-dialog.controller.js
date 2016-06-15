(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('CompositionDialogController', CompositionDialogController);

    CompositionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Composition', 'Video', 'TranslationKey', 'Composer', 'Program', 'Event'];

    function CompositionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Composition, Video, TranslationKey, Composer, Program, Event) {
        var vm = this;

        vm.composition = entity;
        vm.clear = clear;
        vm.save = save;
        vm.videos = Video.query();
        vm.lyricstranslations = TranslationKey.query({filter: 'composition-is-null'});
        $q.all([vm.composition.$promise, vm.lyricstranslations.$promise]).then(function() {
            if (!vm.composition.lyricsTranslation || !vm.composition.lyricsTranslation.id) {
                return $q.reject();
            }
            return TranslationKey.get({id : vm.composition.lyricsTranslation.id}).$promise;
        }).then(function(lyricsTranslation) {
            vm.lyricstranslations.push(lyricsTranslation);
        });
        vm.composers = Composer.query();
        vm.programs = Program.query();
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.composition.id !== null) {
                Composition.update(vm.composition, onSaveSuccess, onSaveError);
            } else {
                Composition.save(vm.composition, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('versushipsterApp:compositionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
