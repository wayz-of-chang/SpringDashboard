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

    describe('update_chart()', function() {
        it('should be able to update status value', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            var returnValue = null;
            var monitorController = $controller('MonitorController', {
                $scope: scope,
                $window: window,
                service: service
            });
            monitorController.last_updated = {
                13: null
            };
            monitorController.monitors = {
                13: {
                    chart: 'status',
                    chart_render: {
                        update: function(value) {
                            returnValue = value;
                            return;
                        }
                    }
                }
            };
            var date = (new Date()).toString();
            monitorController.update_chart(13, {value: 123, status: 'success'});
            assert.equal(123, returnValue);
            assert.equal(date, monitorController.last_updated[13]);
        }));

        it('should be able to update gauge value', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            var returnValue = null;
            var monitorController = $controller('MonitorController', {
                $scope: scope,
                $window: window,
                service: service
            });
            monitorController.last_updated = {
                13: null
            };
            monitorController.monitors = {
                13: {
                    chart: 'gauge',
                    chart_render: {
                        update: function(value) {
                            returnValue = value;
                            return;
                        }
                    },
                    chart_config: {
                        maxValue: 100
                    }
                }
            };
            var date = (new Date()).toString();
            monitorController.update_chart(13, {value: 123, max: 1000, unit: 'aaa', mediumThreshold: 500, highThreshold: 800});
            assert.equal(123, returnValue);
            assert.equal(1000, monitorController.monitors[13].chart_config.maxValue);
            assert.equal('aaa', monitorController.monitors[13].chart_config.displayUnit);
            assert.equal(date, monitorController.last_updated[13]);

            monitorController.last_updated = {
                13: null
            };
            date = (new Date()).toString();
            monitorController.update_chart(13, {value: 234, max: 1000, unit: '%', mediumThreshold: 500, highThreshold: 800});
            assert.equal(23, returnValue);
            assert.equal(100, monitorController.monitors[13].chart_config.maxValue);
            assert.equal('%', monitorController.monitors[13].chart_config.displayUnit);
            assert.equal(date, monitorController.last_updated[13]);
        }));

        it('should be able to update bar values', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            var returnValue = null;
            var monitorController = $controller('MonitorController', {
                $scope: scope,
                $window: window,
                service: service
            });
            monitorController.last_updated = {
                13: null
            };
            monitorController.monitors = {
                13: {
                    chart: 'bar',
                    chart_render: {
                        update: function(values) {
                            returnValue = values;
                            return;
                        }
                    },
                    chart_config: {
                        maxValue: 100
                    }
                }
            };
            var date = (new Date()).toString();
            monitorController.update_chart(13, {values: [1,2,3], max: 1000, unit: 'aaa', mediumThreshold: 500, highThreshold: 800});
            assert.equal([1,2,3].toString(), returnValue.toString());
            assert.equal(1000, monitorController.monitors[13].chart_config.maxValue);
            assert.equal('aaa', monitorController.monitors[13].chart_config.displayUnit);
            assert.equal(date, monitorController.last_updated[13]);
        }));

        it('should be able to update pie values', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            var returnValue = null;
            var monitorController = $controller('MonitorController', {
                $scope: scope,
                $window: window,
                service: service
            });
            monitorController.last_updated = {
                13: null
            };
            monitorController.monitors = {
                13: {
                    chart: 'pie',
                    chart_render: {
                        update: function(values) {
                            returnValue = values;
                            return;
                        }
                    },
                    chart_config: {
                    }
                }
            };
            var date = (new Date()).toString();
            monitorController.update_chart(13, [1,2,3]);
            assert.equal([1,2,3].toString(), returnValue.toString());
            assert.equal(date, monitorController.last_updated[13]);
        }));

        it('should be able to update line values', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            var returnDate = null;
            var returnValue = null;
            var monitorController = $controller('MonitorController', {
                $scope: scope,
                $window: window,
                service: service
            });
            monitorController.last_updated = {
                13: null
            };
            monitorController.monitors = {
                13: {
                    chart: 'line',
                    chart_render: {
                        update: function(dates, values) {
                            returnDate = dates;
                            console.log(values);
                            returnValue = values;
                            return;
                        }
                    },
                    chart_config: {
                        dates: [],
                        values: []
                    }
                }
            };
            var date = (new Date()).toString();
            monitorController.update_chart(13, {'a': {value: 1, color: 'red'},'b': {value: 2, color: 'green'},'c': {value: 3, color: 'blue'}});
            assert.equal(date.toString(), returnDate[0].toString());
            assert.equal('red', returnValue[0].color);
            assert.equal('a', returnValue[0].key);
            assert.equal(1, returnValue[0].values[0].y);
            assert.equal('green', returnValue[1].color);
            assert.equal('b', returnValue[1].key);
            assert.equal(2, returnValue[1].values[0].y);
            assert.equal('blue', returnValue[2].color);
            assert.equal('c', returnValue[2].key);
            assert.equal(3, returnValue[2].values[0].y);
            assert.equal(date, monitorController.last_updated[13]);

            monitorController.last_updated = {
                13: null
            };
            var date = (new Date()).toString();
            monitorController.update_chart(13, {'a': {value: 4, color: 'red'},'b': {value: 5, color: 'green'},'c': {value: 6, color: 'blue'}});
            assert.equal(date.toString(), returnDate[1].toString());
            assert.equal('red', returnValue[0].color);
            assert.equal('a', returnValue[0].key);
            assert.equal(4, returnValue[0].values[1].y);
            assert.equal('green', returnValue[1].color);
            assert.equal('b', returnValue[1].key);
            assert.equal(5, returnValue[1].values[1].y);
            assert.equal('blue', returnValue[2].color);
            assert.equal('c', returnValue[2].key);
            assert.equal(6, returnValue[2].values[1].y);
            assert.equal(date, monitorController.last_updated[13]);
        }));

        it('should be able to update raw value', inject(function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            var returnValue = null;
            var monitorController = $controller('MonitorController', {
                $scope: scope,
                $window: window,
                service: service
            });
            monitorController.last_updated = {
                13: null
            };
            monitorController.monitors = {
                13: {
                    chart: 'raw', //can be any string that isn't status, gauge, bar, pie, or line
                    chart_render: {
                        update: function(values) {
                            returnValue = values;
                            return;
                        }
                    },
                    chart_config: {
                    }
                }
            };
            var date = (new Date()).toString();
            monitorController.update_chart(13, [1,2,3]);
            assert.equal(true, monitorController.monitors[13].show_raw);
            assert.equal(date, monitorController.last_updated[13]);
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