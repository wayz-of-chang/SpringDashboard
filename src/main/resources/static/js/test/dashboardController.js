var assert = chai.assert;
describe('DashboardController', function() {
    beforeEach(module('dashboardApp'));

    describe('create_new_dashboard()', function() {
        it('should be able to create new dashboards', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.create_dashboard = function(data, callback) {
                callback({
                    status: 200,
                    data: {
                        data: {
                            id: 9,
                            name: 'abc'
                        }
                    }
                });
            };
            service.get_user_property = function(id) {
                return 10;
            };
            service.select_dashboard = function(id) {
                return;
            };
            var dashboardController = $controller('DashboardController', {
                $scope: scope,
                $window: window,
                service: service
            });
            dashboardController.new_dashboard = {
                name: 'abc'
            };
            dashboardController.create_new_dashboard();
            assert.equal(true, dashboardController.new_dashboard.success);
            assert.equal("Successfully created dashboard: abc", dashboardController.new_dashboard.success_message);
            assert.equal(false, dashboardController.new_dashboard.error);
            assert.equal(false, dashboardController.new_dashboard.error_message);
        }));

        it('should handle server errors properly', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.create_dashboard = function(data, callback) {
                callback({
                    status: 400,
                    data: {
                        error: "error status",
                        message: "error message"
                    }
                });
            };
            service.get_user_property = function(id) {
                return 10;
            };
            service.select_dashboard = function(id) {
                return;
            };
            var dashboardController = $controller('DashboardController', {
                $scope: scope,
                $window: window,
                service: service
            });
            dashboardController.new_dashboard = {
                name: 'abc'
            };
            dashboardController.create_new_dashboard();
            assert.equal(false, dashboardController.new_dashboard.success);
            assert.equal(null, dashboardController.new_dashboard.success_message);
            assert.equal(true, dashboardController.new_dashboard.error);
            assert.equal("error status: error message", dashboardController.new_dashboard.error_message);
        }));
    });

    describe('update()', function() {
        it('should be able to update dashboards', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.edit_dashboard = function(data, callback) {
                callback({
                    status: 200,
                    data: {
                        data: {
                            id: 11,
                            name: 'def'
                        }
                    }
                });
            };
            service.get_user_property = function(id) {
                return 10;
            };
            service.select_dashboard = function(id) {
                return;
            };
            var dashboardController = $controller('DashboardController', {
                $scope: scope,
                $window: window,
                service: service
            });
            dashboardController.current = 9;
            dashboardController.dashboards = {
                9: {
                    name: 'def'
                }
            };
            dashboardController.update();
            assert.equal(true, dashboardController.edit_dashboard.success);
            assert.equal("Successfully updated dashboard: def", dashboardController.edit_dashboard.success_message);
            assert.equal(false, dashboardController.edit_dashboard.error);
            assert.equal(false, dashboardController.edit_dashboard.error_message);
        }));

        it('should handle server errors properly', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.edit_dashboard = function(data, callback) {
                callback({
                    status: 400,
                    data: {
                        error: "error status",
                        message: "error message"
                    }
                });
            };
            service.get_user_property = function(id) {
                return 10;
            };
            service.select_dashboard = function(id) {
                return;
            };
            var dashboardController = $controller('DashboardController', {
                $scope: scope,
                $window: window,
                service: service
            });
            dashboardController.current = 9;
            dashboardController.dashboards = {
                9: {
                    name: 'def'
                }
            };
            dashboardController.update();
            assert.equal(false, dashboardController.edit_dashboard.success);
            assert.equal('', dashboardController.edit_dashboard.success_message);
            assert.equal(true, dashboardController.edit_dashboard.error);
            assert.equal("error status: error message", dashboardController.edit_dashboard.error_message);
        }));
    });

    describe('delete()', function() {
        it('should be able to delete dashboards', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.delete_dashboard = function(data, callback) {
                callback({
                    status: 200,
                    data: {
                        data: {
                            id: 11,
                            name: 'def'
                        }
                    }
                });
            };
            service.get_user_property = function(id) {
                return 10;
            };
            service.select_dashboard = function(id) {
                return;
            };
            var dashboardController = $controller('DashboardController', {
                $scope: scope,
                $window: window,
                service: service
            });
            dashboardController.current = 9;
            dashboardController.dashboards = {
                9: {
                    name: 'def'
                }
            };
            dashboardController.delete();
            assert.equal(true, dashboardController.delete_dashboard.success);
            assert.equal("Successfully deleted dashboard: def", dashboardController.delete_dashboard.success_message);
            assert.equal(false, dashboardController.delete_dashboard.error);
            assert.equal(false, dashboardController.delete_dashboard.error_message);
        }));

        it('should handle server errors properly', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.delete_dashboard = function(data, callback) {
                callback({
                    status: 400,
                    data: {
                        error: "error status",
                        message: "error message"
                    }
                });
            };
            service.get_user_property = function(id) {
                return 10;
            };
            service.select_dashboard = function(id) {
                return;
            };
            var dashboardController = $controller('DashboardController', {
                $scope: scope,
                $window: window,
                service: service
            });
            dashboardController.current = 9;
            dashboardController.dashboards = {
                9: {
                    name: 'def'
                }
            };
            dashboardController.delete();
            assert.equal(false, dashboardController.delete_dashboard.success);
            assert.equal('', dashboardController.delete_dashboard.success_message);
            assert.equal(true, dashboardController.delete_dashboard.error);
            assert.equal("error status: error message", dashboardController.delete_dashboard.error_message);
        }));
    });
});