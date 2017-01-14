(function() {
    'use strict';

    angular
        .module('gestionProjetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('projet', {
            parent: 'entity',
            url: '/projet?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestionProjetApp.projet.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/projet/projets.html',
                    controller: 'ProjetController',
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
                    $translatePartialLoader.addPart('projet');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('projet-detail', {
            parent: 'entity',
            url: '/projet/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestionProjetApp.projet.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/projet/projet-detail.html',
                    controller: 'ProjetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projet');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Projet', function($stateParams, Projet) {
                    return Projet.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'projet',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('projet-detail.edit', {
            parent: 'projet-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/projet/projet-dialog.html',
                    controller: 'ProjetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Projet', function(Projet) {
                            return Projet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('projet.new', {
            parent: 'projet',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/projet/projet-dialog.html',
                    controller: 'ProjetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelleProjet: null,
                                typeProjet: null,
                                dateDebut: null,
                                dateFin: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('projet', null, { reload: 'projet' });
                }, function() {
                    $state.go('projet');
                });
            }]
        })
        .state('projet.edit', {
            parent: 'projet',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/projet/projet-dialog.html',
                    controller: 'ProjetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Projet', function(Projet) {
                            return Projet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('projet', null, { reload: 'projet' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('projet.delete', {
            parent: 'projet',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/projet/projet-delete-dialog.html',
                    controller: 'ProjetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Projet', function(Projet) {
                            return Projet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('projet', null, { reload: 'projet' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
