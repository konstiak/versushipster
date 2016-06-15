(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('composition', {
            parent: 'entity',
            url: '/composition',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'versushipsterApp.composition.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/composition/compositions.html',
                    controller: 'CompositionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('composition');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('composition-detail', {
            parent: 'entity',
            url: '/composition/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'versushipsterApp.composition.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/composition/composition-detail.html',
                    controller: 'CompositionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('composition');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Composition', function($stateParams, Composition) {
                    return Composition.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('composition.new', {
            parent: 'composition',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/composition/composition-dialog.html',
                    controller: 'CompositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                originalLyrics: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('composition', null, { reload: true });
                }, function() {
                    $state.go('composition');
                });
            }]
        })
        .state('composition.edit', {
            parent: 'composition',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/composition/composition-dialog.html',
                    controller: 'CompositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Composition', function(Composition) {
                            return Composition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('composition', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('composition.delete', {
            parent: 'composition',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/composition/composition-delete-dialog.html',
                    controller: 'CompositionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Composition', function(Composition) {
                            return Composition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('composition', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
