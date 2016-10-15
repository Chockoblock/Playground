(function() {
    'use strict';

    angular
        .module('test3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('booking-item', {
            parent: 'entity',
            url: '/booking-item',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'test3App.bookingItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/booking-item/booking-items.html',
                    controller: 'BookingItemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bookingItem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('booking-item-detail', {
            parent: 'entity',
            url: '/booking-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'test3App.bookingItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/booking-item/booking-item-detail.html',
                    controller: 'BookingItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bookingItem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BookingItem', function($stateParams, BookingItem) {
                    return BookingItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'booking-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('booking-item-detail.edit', {
            parent: 'booking-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking-item/booking-item-dialog.html',
                    controller: 'BookingItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BookingItem', function(BookingItem) {
                            return BookingItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('booking-item.new', {
            parent: 'booking-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking-item/booking-item-dialog.html',
                    controller: 'BookingItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                price: null,
                                serviceType: null,
                                startTime: null,
                                endTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('booking-item', null, { reload: 'booking-item' });
                }, function() {
                    $state.go('booking-item');
                });
            }]
        })
        .state('booking-item.edit', {
            parent: 'booking-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking-item/booking-item-dialog.html',
                    controller: 'BookingItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BookingItem', function(BookingItem) {
                            return BookingItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('booking-item', null, { reload: 'booking-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('booking-item.delete', {
            parent: 'booking-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/booking-item/booking-item-delete-dialog.html',
                    controller: 'BookingItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BookingItem', function(BookingItem) {
                            return BookingItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('booking-item', null, { reload: 'booking-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
