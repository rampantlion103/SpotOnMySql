'use strict';

angular.module('spotonApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


