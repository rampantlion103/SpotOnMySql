'use strict';

angular.module('spotonApp')
    .factory('errorHandlerInterceptor', function ($q, $rootScope) {
        return {
            'responseError': function (response) {
                if (!(response.status == 401 && response.data.path.indexOf("/api/account") == 0 )){
	                $rootScope.$emit('spotonApp.httpError', response);
	            }
                return $q.reject(response);
            }
        };
    });