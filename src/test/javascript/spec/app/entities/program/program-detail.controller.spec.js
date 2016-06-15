'use strict';

describe('Controller Tests', function() {

    describe('Program Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProgram, MockComposition, MockTranslationKey;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProgram = jasmine.createSpy('MockProgram');
            MockComposition = jasmine.createSpy('MockComposition');
            MockTranslationKey = jasmine.createSpy('MockTranslationKey');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Program': MockProgram,
                'Composition': MockComposition,
                'TranslationKey': MockTranslationKey
            };
            createController = function() {
                $injector.get('$controller')("ProgramDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'versushipsterApp:programUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
