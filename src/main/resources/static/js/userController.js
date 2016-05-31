app.controller('UserController', function($http) {
    var user = this;
    user.user = {
        id: '1',
        username: 'User 1',
        password: 'abc',
        email: 'abc@a.com'
    };
    user.create = function() {
        var data = {
            username: 'User 1',
            password: 'password',
            email: 'abc@d.efg',
            role: 'R_USER'
        };
        $http.post('/users/create', data).then(function() {
            //success
        }, function() {
            //fail
        })
    };
});