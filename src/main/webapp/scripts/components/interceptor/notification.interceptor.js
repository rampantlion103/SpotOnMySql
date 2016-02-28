 'use strict';

angular.module('spotonApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-spotonApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-spotonApp-params')});
                }
                return response;
            }
        };
    });
