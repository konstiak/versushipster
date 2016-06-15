(function() {
    'use strict';

    angular
        .module('versushipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('member', {
            parent: 'entity',
            url: '/member?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'versushipsterApp.member.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/member/members.html',
                    controller: 'MemberController',
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
                    $translatePartialLoader.addPart('member');
                    $translatePartialLoader.addPart('voiceType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('member-detail', {
            parent: 'entity',
            url: '/member/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'versushipsterApp.member.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/member/member-detail.html',
                    controller: 'MemberDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('member');
                    $translatePartialLoader.addPart('voiceType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Member', function($stateParams, Member) {
                    return Member.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('member.new', {
            parent: 'member',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/member/member-dialog.html',
                    controller: 'MemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                secondName: null,
                                active: null,
                                voice: null,
                                photo: null,
                                memberFrom: null,
                                memberTo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('member', null, { reload: true });
                }, function() {
                    $state.go('member');
                });
            }]
        })
        .state('member.edit', {
            parent: 'member',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/member/member-dialog.html',
                    controller: 'MemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Member', function(Member) {
                            return Member.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('member', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('member.delete', {
            parent: 'member',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/member/member-delete-dialog.html',
                    controller: 'MemberDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Member', function(Member) {
                            return Member.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('member', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
