(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('documentation', {
            parent: 'entity',
            url: '/documentation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projectMicroServicesApp.documentation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/documentation/documentations.html',
                    controller: 'DocumentationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('documentation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('documentation-detail', {
            parent: 'entity',
            url: '/documentation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projectMicroServicesApp.documentation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/documentation/documentation-detail.html',
                    controller: 'DocumentationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('documentation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Documentation', function($stateParams, Documentation) {
                    return Documentation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'documentation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('documentation-detail.edit', {
            parent: 'documentation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/documentation/documentation-dialog.html',
                    controller: 'DocumentationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Documentation', function(Documentation) {
                            return Documentation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('documentation.new', {
            parent: 'documentation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/documentation/documentation-dialog.html',
                    controller: 'DocumentationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                description: null,
                                file: null,
                                fileContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('documentation', null, { reload: 'documentation' });
                }, function() {
                    $state.go('documentation');
                });
            }]
        })
        .state('documentation.edit', {
            parent: 'documentation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/documentation/documentation-dialog.html',
                    controller: 'DocumentationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Documentation', function(Documentation) {
                            return Documentation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('documentation', null, { reload: 'documentation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('documentation.delete', {
            parent: 'documentation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/documentation/documentation-delete-dialog.html',
                    controller: 'DocumentationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Documentation', function(Documentation) {
                            return Documentation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('documentation', null, { reload: 'documentation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
