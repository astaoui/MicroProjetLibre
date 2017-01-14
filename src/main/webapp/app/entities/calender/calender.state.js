(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('calender', {
            parent: 'entity',
            url: '/calender',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projectMicroServicesApp.calender.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/calender/calenders.html',
                    controller: 'CalenderController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('calender');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('calender-detail', {
            parent: 'entity',
            url: '/calender/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projectMicroServicesApp.calender.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/calender/calender-detail.html',
                    controller: 'CalenderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('calender');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Calender', function($stateParams, Calender) {
                    return Calender.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'calender',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('calender-detail.edit', {
            parent: 'calender-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/calender/calender-dialog.html',
                    controller: 'CalenderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Calender', function(Calender) {
                            return Calender.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('calender.new', {
            parent: 'calender',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/calender/calender-dialog.html',
                    controller: 'CalenderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                dateBegining: null,
                                dateEnding: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('calender', null, { reload: 'calender' });
                }, function() {
                    $state.go('calender');
                });
            }]
        })
        .state('calender.edit', {
            parent: 'calender',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/calender/calender-dialog.html',
                    controller: 'CalenderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Calender', function(Calender) {
                            return Calender.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('calender', null, { reload: 'calender' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('calender.delete', {
            parent: 'calender',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/calender/calender-delete-dialog.html',
                    controller: 'CalenderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Calender', function(Calender) {
                            return Calender.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('calender', null, { reload: 'calender' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
