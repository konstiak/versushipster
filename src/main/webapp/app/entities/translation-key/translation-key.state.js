(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('translation-key', {
            parent: 'entity',
            url: '/translation-key',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'versushipsterApp.translationKey.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/translation-key/translation-keys.html',
                    controller: 'TranslationKeyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('translationKey');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('translation-key-detail', {
            parent: 'entity',
            url: '/translation-key/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'versushipsterApp.translationKey.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/translation-key/translation-key-detail.html',
                    controller: 'TranslationKeyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('translationKey');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TranslationKey', function($stateParams, TranslationKey) {
                    return TranslationKey.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('translation-key.new', {
            parent: 'translation-key',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/translation-key/translation-key-dialog.html',
                    controller: 'TranslationKeyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                key: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('translation-key', null, { reload: true });
                }, function() {
                    $state.go('translation-key');
                });
            }]
        })
        .state('translation-key.edit', {
            parent: 'translation-key',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/translation-key/translation-key-dialog.html',
                    controller: 'TranslationKeyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TranslationKey', function(TranslationKey) {
                            return TranslationKey.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('translation-key', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('translation-key.delete', {
            parent: 'translation-key',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/translation-key/translation-key-delete-dialog.html',
                    controller: 'TranslationKeyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TranslationKey', function(TranslationKey) {
                            return TranslationKey.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('translation-key', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
