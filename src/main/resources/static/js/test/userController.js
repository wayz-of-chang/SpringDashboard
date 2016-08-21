var assert = chai.assert;
describe('UserController', function() {
    beforeEach(module('dashboardApp'));

    describe('register()', function() {
        it('should be able to register new users', inject(function($rootScope, $controller) {
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
            assert.equal(true, userController.success);
            assert.equal(false, userController.error);
            assert.equal(true, userController.mode.register);
        }));
    });
});