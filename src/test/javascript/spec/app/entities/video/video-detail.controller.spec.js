'use strict';

describe('Controller Tests', function() {

    describe('Video Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockVideo, MockComposition;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockVideo = jasmine.createSpy('MockVideo');
            MockComposition = jasmine.createSpy('MockComposition');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Video': MockVideo,
                'Composition': MockComposition
            };
            createController = function() {
                $injector.get('$controller')("VideoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'versushipsterApp:videoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
