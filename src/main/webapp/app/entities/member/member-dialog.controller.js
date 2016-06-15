(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .controller('MemberDialogController', MemberDialogController);

    MemberDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Member', 'TranslationKey'];

    function MemberDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Member, TranslationKey) {
        var vm = this;

        vm.member = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.descriptions = TranslationKey.query({filter: 'member-is-null'});
        $q.all([vm.member.$promise, vm.descriptions.$promise]).then(function() {
            if (!vm.member.description || !vm.member.description.id) {
                return $q.reject();
            }
            return TranslationKey.get({id : vm.member.description.id}).$promise;
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
            if (vm.member.id !== null) {
                Member.update(vm.member, onSaveSuccess, onSaveError);
            } else {
                Member.save(vm.member, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('versushipsterApp:memberUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.memberFrom = false;
        vm.datePickerOpenStatus.memberTo = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
