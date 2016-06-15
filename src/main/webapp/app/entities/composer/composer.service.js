(function() {
    'use strict';
    angular
        .module('versushipsterApp')
        .factory('Composer', Composer);

    Composer.$inject = ['$resource'];

    function Composer ($resource) {
        var resourceUrl =  'api/composers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
