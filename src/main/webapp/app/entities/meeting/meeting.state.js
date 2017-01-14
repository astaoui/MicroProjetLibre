(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('meeting', {
            parent: 'entity',
            url: '/meeting',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projectMicroServicesApp.meeting.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meeting/meetings.html',
                    controller: 'MeetingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('meeting');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('meeting-detail', {
            parent: 'entity',
            url: '/meeting/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projectMicroServicesApp.meeting.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meeting/meeting-detail.html',
                    controller: 'MeetingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('meeting');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Meeting', function($stateParams, Meeting) {
                    return Meeting.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'meeting',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('meeting-detail.edit', {
            parent: 'meeting-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meeting/meeting-dialog.html',
                    controller: 'MeetingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Meeting', function(Meeting) {
                            return Meeting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meeting.new', {
            parent: 'meeting',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meeting/meeting-dialog.html',
                    controller: 'MeetingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('meeting', null, { reload: 'meeting' });
                }, function() {
                    $state.go('meeting');
                });
            }]
        })
        .state('meeting.edit', {
            parent: 'meeting',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meeting/meeting-dialog.html',
                    controller: 'MeetingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Meeting', function(Meeting) {
                            return Meeting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meeting', null, { reload: 'meeting' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meeting.delete', {
            parent: 'meeting',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meeting/meeting-delete-dialog.html',
                    controller: 'MeetingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Meeting', function(Meeting) {
                            return Meeting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meeting', null, { reload: 'meeting' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
