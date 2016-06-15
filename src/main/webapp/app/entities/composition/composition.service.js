(function() {
    'use strict';
    angular
        .module('versushipsterApp')
        .factory('Composition', Composition);

    Composition.$inject = ['$resource'];

    function Composition ($resource) {
        var resourceUrl =  'api/compositions/:id';

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
