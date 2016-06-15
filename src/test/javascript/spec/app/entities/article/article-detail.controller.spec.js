'use strict';

describe('Controller Tests', function() {

    describe('Article Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockArticle, MockTranslationKey;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockArticle = jasmine.createSpy('MockArticle');
            MockTranslationKey = jasmine.createSpy('MockTranslationKey');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Article': MockArticle,
                'TranslationKey': MockTranslationKey
            };
            createController = function() {
                $injector.get('$controller')("ArticleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'versushipsterApp:articleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
