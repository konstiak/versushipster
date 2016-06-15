(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('composer', {
            parent: 'entity',
            url: '/composer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'versushipsterApp.composer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/composer/composers.html',
                    controller: 'ComposerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('composer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('composer-detail', {
            parent: 'entity',
            url: '/composer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'versushipsterApp.composer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/composer/composer-detail.html',
                    controller: 'ComposerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('composer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Composer', function($stateParams, Composer) {
                    return Composer.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('composer.new', {
            parent: 'composer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/composer/composer-dialog.html',
                    controller: 'ComposerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('composer', null, { reload: true });
                }, function() {
                    $state.go('composer');
                });
            }]
        })
        .state('composer.edit', {
            parent: 'composer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/composer/composer-dialog.html',
                    controller: 'ComposerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Composer', function(Composer) {
                            return Composer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('composer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('composer.delete', {
            parent: 'composer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/composer/composer-delete-dialog.html',
                    controller: 'ComposerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Composer', function(Composer) {
                            return Composer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('composer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
