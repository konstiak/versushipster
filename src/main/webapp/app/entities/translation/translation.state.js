(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('translation', {
            parent: 'entity',
            url: '/translation?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'versushipsterApp.translation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/translation/translations.html',
                    controller: 'TranslationController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('translation');
                    $translatePartialLoader.addPart('localeType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('translation-detail', {
            parent: 'entity',
            url: '/translation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'versushipsterApp.translation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/translation/translation-detail.html',
                    controller: 'TranslationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('translation');
                    $translatePartialLoader.addPart('localeType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Translation', function($stateParams, Translation) {
                    return Translation.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('translation.new', {
            parent: 'translation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/translation/translation-dialog.html',
                    controller: 'TranslationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                locale: null,
                                translation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('translation', null, { reload: true });
                }, function() {
                    $state.go('translation');
                });
            }]
        })
        .state('translation.edit', {
            parent: 'translation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/translation/translation-dialog.html',
                    controller: 'TranslationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Translation', function(Translation) {
                            return Translation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('translation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('translation.delete', {
            parent: 'translation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/translation/translation-delete-dialog.html',
                    controller: 'TranslationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Translation', function(Translation) {
                            return Translation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('translation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
