'use strict';

describe('Controller Tests', function() {

    describe('Composer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockComposer, MockComposition, MockTranslationKey;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockComposer = jasmine.createSpy('MockComposer');
            MockComposition = jasmine.createSpy('MockComposition');
            MockTranslationKey = jasmine.createSpy('MockTranslationKey');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Composer': MockComposer,
                'Composition': MockComposition,
                'TranslationKey': MockTranslationKey
            };
            createController = function() {
                $injector.get('$controller')("ComposerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'versushipsterApp:composerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
