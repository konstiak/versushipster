'use strict';

describe('Controller Tests', function() {

    describe('Composition Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockComposition, MockVideo, MockTranslationKey, MockComposer, MockProgram, MockEvent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockComposition = jasmine.createSpy('MockComposition');
            MockVideo = jasmine.createSpy('MockVideo');
            MockTranslationKey = jasmine.createSpy('MockTranslationKey');
            MockComposer = jasmine.createSpy('MockComposer');
            MockProgram = jasmine.createSpy('MockProgram');
            MockEvent = jasmine.createSpy('MockEvent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Composition': MockComposition,
                'Video': MockVideo,
                'TranslationKey': MockTranslationKey,
                'Composer': MockComposer,
                'Program': MockProgram,
                'Event': MockEvent
            };
            createController = function() {
                $injector.get('$controller')("CompositionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'versushipsterApp:compositionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
