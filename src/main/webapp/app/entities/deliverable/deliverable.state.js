(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('deliverable', {
            parent: 'entity',
            url: '/deliverable',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projectMicroServicesApp.deliverable.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/deliverable/deliverables.html',
                    controller: 'DeliverableController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('deliverable');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('deliverable-detail', {
            parent: 'entity',
            url: '/deliverable/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projectMicroServicesApp.deliverable.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/deliverable/deliverable-detail.html',
                    controller: 'DeliverableDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('deliverable');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Deliverable', function($stateParams, Deliverable) {
                    return Deliverable.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'deliverable',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('deliverable-detail.edit', {
            parent: 'deliverable-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deliverable/deliverable-dialog.html',
                    controller: 'DeliverableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Deliverable', function(Deliverable) {
                            return Deliverable.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('deliverable.new', {
            parent: 'deliverable',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deliverable/deliverable-dialog.html',
                    controller: 'DeliverableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                dateBegining: null,
                                dateEnding: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('deliverable', null, { reload: 'deliverable' });
                }, function() {
                    $state.go('deliverable');
                });
            }]
        })
        .state('deliverable.edit', {
            parent: 'deliverable',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deliverable/deliverable-dialog.html',
                    controller: 'DeliverableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Deliverable', function(Deliverable) {
                            return Deliverable.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('deliverable', null, { reload: 'deliverable' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('deliverable.delete', {
            parent: 'deliverable',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deliverable/deliverable-delete-dialog.html',
                    controller: 'DeliverableDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Deliverable', function(Deliverable) {
                            return Deliverable.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('deliverable', null, { reload: 'deliverable' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
