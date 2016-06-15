'use strict';

describe('Controller Tests', function() {

    describe('Translation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTranslation, MockTranslationKey;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTranslation = jasmine.createSpy('MockTranslation');
            MockTranslationKey = jasmine.createSpy('MockTranslationKey');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Translation': MockTranslation,
                'TranslationKey': MockTranslationKey
            };
            createController = function() {
                $injector.get('$controller')("TranslationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'versushipsterApp:translationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
