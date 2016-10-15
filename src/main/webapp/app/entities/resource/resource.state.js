(function() {
    'use strict';

    angular
        .module('test3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('resource', {
            parent: 'entity',
            url: '/resource',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'test3App.resource.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resource/resources.html',
                    controller: 'ResourceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resource');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('resource-detail', {
            parent: 'entity',
            url: '/resource/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'test3App.resource.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resource/resource-detail.html',
                    controller: 'ResourceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resource');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Resource', function($stateParams, Resource) {
                    return Resource.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'resource',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('resource-detail.edit', {
            parent: 'resource-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource/resource-dialog.html',
                    controller: 'ResourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resource', function(Resource) {
                            return Resource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resource.new', {
            parent: 'resource',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource/resource-dialog.html',
                    controller: 'ResourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                size: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('resource', null, { reload: 'resource' });
                }, function() {
                    $state.go('resource');
                });
            }]
        })
        .state('resource.edit', {
            parent: 'resource',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource/resource-dialog.html',
                    controller: 'ResourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resource', function(Resource) {
                            return Resource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resource', null, { reload: 'resource' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resource.delete', {
            parent: 'resource',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource/resource-delete-dialog.html',
                    controller: 'ResourceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Resource', function(Resource) {
                            return Resource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resource', null, { reload: 'resource' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
