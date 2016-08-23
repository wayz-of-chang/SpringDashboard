var assert = chai.assert;
describe('MonitorController', function() {
    beforeEach(module('dashboardApp'));

    describe('add_monitor()', function() {
        it('should be able to add new monitors', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.create_monitor = function(data, callback) {
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
            service.get_user_settings = function() {
                return {
                    current_dashboard: 13
                };
            };
            service.update_monitor_order = function() {
                return;
            };
            var monitorController = $controller('MonitorController', {
                $scope: scope,
                $window: window,
                service: service
            });
            monitorController.add_monitor();
            assert.equal(true, monitorController.new_monitor.success);
            assert.equal("Successfully created monitor", monitorController.new_monitor.success_message);
            assert.equal(false, monitorController.new_monitor.error);
            assert.equal(false, monitorController.new_monitor.error_message);
        }));

        it('should handle server errors properly', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.create_monitor = function(data, callback) {
                callback({
                    status: 400,
                    data: {
                        error: "error status",
                        message: "error message"
                    }
                });
            };
            service.get_user_settings = function() {
                return {
                    current_dashboard: 13
                };
            };
            service.update_monitor_order = function() {
                return;
            };
            var monitorController = $controller('MonitorController', {
                $scope: scope,
                $window: window,
                service: service
            });
            monitorController.add_monitor();
            assert.equal(false, monitorController.new_monitor.success);
            assert.equal('', monitorController.new_monitor.success_message);
            assert.equal(true, monitorController.new_monitor.error);
            assert.equal("error status: error message", monitorController.new_monitor.error_message);
        }));
    });

    describe('copy_monitor()', function() {
        it('should be able to copy monitors', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.copy_monitor = function(data, callback) {
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
            service.get_user_settings = function() {
                return {
                    current_dashboard: 13
                };
            };
            service.update_monitor_order = function() {
                return;
            };
            var monitorController = $controller('MonitorController', {
                $scope: scope,
                $window: window,
                service: service
            });
            monitorController.copy_monitor(12);
            assert.equal(true, monitorController.new_monitor.success);
            assert.equal("Successfully copied monitor", monitorController.new_monitor.success_message);
            assert.equal(false, monitorController.new_monitor.error);
            assert.equal(false, monitorController.new_monitor.error_message);
        }));

        it('should handle server errors properly', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.copy_monitor = function(data, callback) {
                callback({
                    status: 400,
                    data: {
                        error: "error status",
                        message: "error message"
                    }
                });
            };
            service.get_user_settings = function() {
                return {
                    current_dashboard: 13
                };
            };
            service.update_monitor_order = function() {
                return;
            };
            var monitorController = $controller('MonitorController', {
                $scope: scope,
                $window: window,
                service: service
            });
            monitorController.copy_monitor(12);
            assert.equal(false, monitorController.new_monitor.success);
            assert.equal('', monitorController.new_monitor.success_message);
            assert.equal(true, monitorController.new_monitor.error);
            assert.equal("error status: error message", monitorController.new_monitor.error_message);
        }));
    });

    describe('delete()', function() {
        it('should be able to delete monitors', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.delete_monitor = function(data, callback) {
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
            service.get_user_settings = function() {
                return {
                    current_dashboard: 13
                };
            };
            service.update_monitor_order = function() {
                return;
            };
            var monitorController = $controller('MonitorController', {
                $scope: scope,
                $window: window,
                service: service
            });
            monitorController.delete(12);
            assert.equal(true, monitorController.delete_monitor.success);
            assert.equal("Successfully deleted monitor: def", monitorController.delete_monitor.success_message);
            assert.equal(false, monitorController.delete_monitor.error);
            assert.equal(false, monitorController.delete_monitor.error_message);
        }));

        it('should handle server errors properly', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.delete_monitor = function(data, callback) {
                callback({
                    status: 400,
                    data: {
                        error: "error status",
                        message: "error message"
                    }
                });
            };
            service.get_user_settings = function() {
                return {
                    current_dashboard: 13
                };
            };
            service.update_monitor_order = function() {
                return;
            };
            var monitorController = $controller('MonitorController', {
                $scope: scope,
                $window: window,
                service: service
            });
            monitorController.delete(12);
            assert.equal(false, monitorController.delete_monitor.success);
            assert.equal('', monitorController.delete_monitor.success_message);
            assert.equal(true, monitorController.delete_monitor.error);
            assert.equal("error status: error message", monitorController.delete_monitor.error_message);
        }));
    });
});