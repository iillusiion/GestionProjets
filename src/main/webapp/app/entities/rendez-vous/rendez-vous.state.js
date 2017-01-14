(function() {
    'use strict';

    angular
        .module('gestionProjetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rendez-vous', {
            parent: 'entity',
            url: '/rendez-vous?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestionProjetApp.rendezVous.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rendez-vous/rendez-vous.html',
                    controller: 'RendezVousController',
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
                    $translatePartialLoader.addPart('rendezVous');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('rendez-vous-detail', {
            parent: 'entity',
            url: '/rendez-vous/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestionProjetApp.rendezVous.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rendez-vous/rendez-vous-detail.html',
                    controller: 'RendezVousDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rendezVous');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RendezVous', function($stateParams, RendezVous) {
                    return RendezVous.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'rendez-vous',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('rendez-vous-detail.edit', {
            parent: 'rendez-vous-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rendez-vous/rendez-vous-dialog.html',
                    controller: 'RendezVousDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RendezVous', function(RendezVous) {
                            return RendezVous.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rendez-vous.new', {
            parent: 'rendez-vous',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rendez-vous/rendez-vous-dialog.html',
                    controller: 'RendezVousDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                description: null,
                                ordreDuJour: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rendez-vous', null, { reload: 'rendez-vous' });
                }, function() {
                    $state.go('rendez-vous');
                });
            }]
        })
        .state('rendez-vous.edit', {
            parent: 'rendez-vous',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rendez-vous/rendez-vous-dialog.html',
                    controller: 'RendezVousDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RendezVous', function(RendezVous) {
                            return RendezVous.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rendez-vous', null, { reload: 'rendez-vous' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rendez-vous.delete', {
            parent: 'rendez-vous',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rendez-vous/rendez-vous-delete-dialog.html',
                    controller: 'RendezVousDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RendezVous', function(RendezVous) {
                            return RendezVous.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rendez-vous', null, { reload: 'rendez-vous' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
