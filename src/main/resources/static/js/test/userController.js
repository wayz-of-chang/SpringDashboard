var assert = chai.assert;
describe('UserController', function() {
    beforeEach(module('dashboardApp'));

    describe('register()', function() {
        it('should be able to register new users', inject(["$rootScope", "$controller", function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.create_user = function(data, callback) {
                callback({
                    status: 200,
                    data: {
                        data: {
                            username: 'abc'
                        }
                    }
                });
            };
            var userController = $controller('UserController', {
                $scope: scope,
                $window: window,
                service: service
            });
            userController.user = {
                username: 'abc',
                password: 'def',
                email: 'ghi@jk.com',
                role: 'R_USER'
            };
            userController.confirm_password = 'def';
            userController.register();
            assert.equal(false, userController.success);
            assert.equal(false, userController.error);
            assert.equal(true, userController.mode.register);
            userController.register();
            assert.equal("User successfully registered: abc", userController.success_message);
            assert.equal(true, userController.success);
            assert.equal(false, userController.error);
            assert.equal(true, userController.mode.register);
        }]));

        it('should handle server errors properly', inject(["$rootScope", "$controller", function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.create_user = function(data, callback) {
                callback({
                    status: 400,
                    data: {
                        error: "error status",
                        message: "error message"
                    }
                });
            };
            var userController = $controller('UserController', {
                $scope: scope,
                $window: window,
                service: service
            });
            userController.user = {
                username: 'abc',
                password: 'def',
                email: 'ghi@jk.com',
                role: 'R_USER'
            };
            userController.confirm_password = 'def';
            userController.register();
            assert.equal(false, userController.success);
            assert.equal(false, userController.error);
            assert.equal(true, userController.mode.register);
            userController.register();
            assert.equal(false, userController.success);
            assert.equal(true, userController.error);
            assert.equal("error status: error message", userController.error_message);
            assert.equal(true, userController.mode.register);
        }]));

        it('should validate that username and password are specified', inject(["$rootScope", "$controller", function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.create_user = function(data, callback) {
                callback({
                    status: 200,
                    data: {
                        data: {
                            username: 'abc'
                        }
                    }
                });
            };
            var userController = $controller('UserController', {
                $scope: scope,
                $window: window,
                service: service
            });
            userController.user = {
                username: '',
                password: 'def',
                email: 'ghi@jk.com',
                role: 'RW_USER'
            };
            userController.confirm_password = 'def';
            userController.mode.register = true;
            userController.register();
            assert.equal(false, userController.success);
            assert.equal(true, userController.error);
            assert.equal("Username and Password cannot be blank.", userController.error_message);
            assert.equal(true, userController.mode.register);

            userController.user = {
                username: 'abc',
                password: '',
                email: 'ghi@jk.com',
                role: 'RW_USER'
            };
            userController.register();
            assert.equal(false, userController.success);
            assert.equal(true, userController.error);
            assert.equal("Username and Password cannot be blank.", userController.error_message);
            assert.equal(true, userController.mode.register);
        }]));

        it('should validate that email and role are specified', inject(["$rootScope", "$controller", function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.create_user = function(data, callback) {
                callback({
                    status: 200,
                    data: {
                        data: {
                            username: 'abc'
                        }
                    }
                });
            };
            var userController = $controller('UserController', {
                $scope: scope,
                $window: window,
                service: service
            });
            userController.user = {
                username: 'abc',
                password: 'def',
                email: 'ghi@jk.com',
                role: ''
            };
            userController.confirm_password = 'def';
            userController.mode.register = true;
            userController.register();
            assert.equal(false, userController.success);
            assert.equal(true, userController.error);
            assert.equal("Email and Role cannot be blank.", userController.error_message);
            assert.equal(true, userController.mode.register);

            userController.user = {
                username: 'abc',
                password: 'def',
                email: '',
                role: 'R_USER'
            };
            userController.register();
            assert.equal(false, userController.success);
            assert.equal(true, userController.error);
            assert.equal("Email and Role cannot be blank.", userController.error_message);
            assert.equal(true, userController.mode.register);
        }]));

        it('should validate that password matches confirm password', inject(["$rootScope", "$controller", function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.create_user = function(data, callback) {
                callback({
                    status: 200,
                    data: {
                        data: {
                            username: 'abc'
                        }
                    }
                });
            };
            var userController = $controller('UserController', {
                $scope: scope,
                $window: window,
                service: service
            });
            userController.user = {
                username: 'abc',
                password: 'def',
                email: 'ghi@jk.com',
                role: 'RW_USER'
            };
            userController.confirm_password = 'abc';
            userController.mode.register = true;
            userController.register();
            assert.equal(false, userController.success);
            assert.equal(true, userController.error);
            assert.equal("Please make sure the password matches.", userController.error_message);
            assert.equal(true, userController.mode.register);
        }]));
    });

    describe('login()', function() {
        it('should be able to login users', inject(["$rootScope", "$controller", function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.login = function(data, callback) {
                callback({
                    status: 200,
                    data: {
                        data: {
                            username: 'abc'
                        }
                    }
                });
            };
            var userController = $controller('UserController', {
                $scope: scope,
                $window: window,
                service: service
            });
            userController.user = {
                username: 'abc',
                password: 'def'
            };
            userController.login();
            assert.equal(true, userController.success);
            assert.equal("User successfully logged in: abc", userController.success_message)
            assert.equal(false, userController.error);
            assert.equal(false, userController.mode.register);
            assert.equal(true, userController.mode.login);
        }]));

        it('should handle server errors properly', inject(["$rootScope", "$controller", function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.login = function(data, callback) {
                callback({
                    status: 400,
                    data: {
                        error: "error status",
                        message: "error message"
                    }
                });
            };
            var userController = $controller('UserController', {
                $scope: scope,
                $window: window,
                service: service
            });
            userController.user = {
                username: 'abc',
                password: 'def'
            };
            userController.register();
            assert.equal(false, userController.success);
            assert.equal(false, userController.error);
            assert.equal(true, userController.mode.register);
            userController.login();
            assert.equal(false, userController.success);
            assert.equal(true, userController.error);
            assert.equal("error status: error message", userController.error_message);
            assert.equal(true, userController.mode.login);
        }]));

        it('should validate that username and password are specified', inject(["$rootScope", "$controller", function($rootScope, $controller) {
            var scope = $rootScope.$new();
            var window = {};
            var service = {};
            service.login = function(data, callback) {
                callback({
                    status: 200,
                    data: {
                        data: {
                            username: 'abc'
                        }
                    }
                });
            };
            var userController = $controller('UserController', {
                $scope: scope,
                $window: window,
                service: service
            });
            userController.user = {
                username: '',
                password: 'def'
            };
            userController.login();
            assert.equal(false, userController.success);
            assert.equal(true, userController.error);
            assert.equal("Username and Password cannot be blank.", userController.error_message);
            assert.equal(true, userController.mode.login);

            userController.user = {
                username: 'abc',
                password: ''
            };
            userController.login();
            assert.equal(false, userController.success);
            assert.equal(true, userController.error);
            assert.equal("Username and Password cannot be blank.", userController.error_message);
            assert.equal(true, userController.mode.login);
        }]));
    });
});